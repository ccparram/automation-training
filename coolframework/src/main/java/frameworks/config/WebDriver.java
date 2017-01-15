package frameworks.config;

/**
 * @author Juan Krzemien
 */
public interface WebDriver {
    int getExplicitTimeOut();

    int getImplicitTimeOut();

    long getPageLoadTimeout();

    long getScriptTimeout();

    int getPollingEveryMs();

    String getRemoteURL();

    boolean isUseSeleniumGrid();

    boolean isUseListener();
}
