import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.*;

public class BaseTest {

    protected static final String BASE_URL = "https://www.saucedemo.com/";
    protected Playwright playwright;
    protected Page page;

    @BeforeEach
    public void setupTest() {
        playwright = Playwright.create();
        var launchOptions = new BrowserType.LaunchOptions();
        launchOptions.setExecutablePath(Path.of("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"));
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
