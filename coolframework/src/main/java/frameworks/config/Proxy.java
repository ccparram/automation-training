package frameworks.config;

/**
 * @author Juan Krzemien
 */
public interface Proxy {

    boolean isEnabled();

    String getHost();

    int getPort();
}
