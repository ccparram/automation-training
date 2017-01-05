package tests.pages;

import frameworks.web.BasePageObject;

/**
 * @author Juan Krzemien
 */

public class SamplePage extends BasePageObject {

    @Override
    protected String getPageUrl() {
        return "http://www.nyaa.se/?cats=1_37&filter=2";
    }

}
