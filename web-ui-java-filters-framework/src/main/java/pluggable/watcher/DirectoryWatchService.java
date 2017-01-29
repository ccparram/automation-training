package pluggable.watcher;

import java.nio.file.Path;

/**
 * @author Juan Krzemien
 */
public interface DirectoryWatchService {

    void register(OnFileChangeListener onFileChangeListener);

    void start();

    void stop();

    interface OnFileChangeListener {
        void onFileCreate(Path filePath);

        void onFileModify(Path filePath);

        void onFileDelete(Path filePath);
    }

}
