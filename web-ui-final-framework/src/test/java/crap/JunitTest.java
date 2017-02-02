package crap;

import org.junit.Test;

/**
 * @author Juan Krzemien
 */

public class JunitTest extends BaseJUnit {

    SamplePage samplePage;

    @Test
    public void aSampleTest() throws Exception {
        getLogger().info("Current browser is " + browser);
        getLogger().info("Current POM is " + samplePage.toString());
    }

}
