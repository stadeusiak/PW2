import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.nio.file.Path;
import java.util.Objects;

abstract class BaseTest {

    protected static final String BASE_URL = "https://www.saucedemo.com/";
    protected static final String LOCAL_CHROME_PATH = "C:\\Program Files\\Google\\Chrome1\\Application\\chrome.exe";
    protected Playwright playwright;
    protected Page page;

    @BeforeEach
    public void setupTest() {
        playwright = Playwright.create();
        var launchOptions = new BrowserType.LaunchOptions();

        String chromePath = Objects.requireNonNullElse(System.getenv("CHROME_PATH"),
                LOCAL_CHROME_PATH);

        launchOptions.setExecutablePath(Path.of(chromePath));
        launchOptions.setHeadless(false);
        Browser browser = playwright.chromium().launch(launchOptions);
        page = browser.newPage();
        page.navigate(BASE_URL);
    }

    @AfterEach
    public void tearDown() {
        playwright.close();
    }
}
