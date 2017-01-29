package pluggable.watcher;

import com.globant.automation.trainings.logging.Logging;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author Juan Krzemien
 */
public class SimpleDirectoryWatchService implements DirectoryWatchService, Logging {

    private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(r -> new Thread(r, getClass().getSimpleName()));
    private final Set<OnFileChangeListener> listeners = new HashSet<>();
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final WatchService watcher;
    private final Path watchedPath;

    public SimpleDirectoryWatchService(Path path) throws IOException {
        this.watchedPath = path;
        this.watcher = watchedPath.getFileSystem().newWatchService();
        watchedPath.register(watcher,
                ENTRY_CREATE,
                ENTRY_DELETE,
                ENTRY_MODIFY);
    }

    @Override
    public void register(OnFileChangeListener onFileChangeListener) {
        listeners.add(onFileChangeListener);
    }

    @Override
    public void start() {
        if (running.get()) {
            return;
        }
        running.set(true);
        getLogger().info(format("Starting [%s] watcher service on [%s]...", getClass().getSimpleName(), watchedPath.toAbsolutePath().toString()));
        singleThreadExecutor.submit(() -> {
            WatchKey key;

            while (running.get()) {
                // wait for key to be signaled
                try {
                    key = watcher.take();
                } catch (InterruptedException ie) {
                    getLogger().warn(ie.getLocalizedMessage(), ie);
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    // The filename is the context of the event.
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filePath = watchedPath.resolve(ev.context().getFileName());

                    // This key is registered only for ENTRY_CREATE events, but an OVERFLOW event can occur regardless if events are lost or discarded.
                    if (kind != OVERFLOW) {
                        for (OnFileChangeListener listener : listeners) {
                            if (kind == ENTRY_CREATE) {
                                listener.onFileCreate(filePath);
                            } else if (kind == ENTRY_DELETE) {
                                listener.onFileDelete(filePath);
                            } else if (kind == ENTRY_MODIFY) {
                                listener.onFileModify(filePath);
                            }
                        }
                    }
                }

                // Reset the key -- this step is critical if you want to receive further watch events.
                // If the key is no longer valid, the directory is inaccessible so exit the loop.
                if (!key.reset()) {
                    break;
                }
            }
        });
    }

    @Override
    public void stop() {
        getLogger().info(format("Stopping %s watcher service for [%s]...", getClass().getSimpleName(), watchedPath.toAbsolutePath().toString()));
        running.set(false);
    }

}
