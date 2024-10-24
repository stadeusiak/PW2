import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.nio.file.Path;
import java.util.Objects;

@Slf4j
abstract class BaseTest {

    protected static final String BASE_URL = "https://www.saucedemo.com/";
    protected static final String LOCAL_CHROME_PATH = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
    protected Playwright playwright;
    protected Browser browser;
    protected Page page;

    @BeforeEach
    public void setupTest() {
        playwright = Playwright.create();
        var launchOptions = new BrowserType.LaunchOptions();

        String chromePath = Objects.requireNonNullElse(System.getenv("CHROME_PATH"), LOCAL_CHROME_PATH);

        launchOptions.setExecutablePath(Path.of(chromePath));
        launchOptions.setHeadless(true);

        try {
            browser = playwright.chromium().launch(launchOptions);
            page = browser.newPage();
            page.navigate(BASE_URL);
        } catch (Exception e) {
            log.error("Failed to launch the browser or navigate to the URL.", e);
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}