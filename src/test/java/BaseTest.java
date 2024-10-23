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
    protected Page page;

    @BeforeEach
    public void setupTest() {
        loadProperties();

        playwright = Playwright.create();
        var launchOptions = new BrowserType.LaunchOptions();

        String chromePath = Objects.requireNonNullElse(System.getenv("CHROME_PATH"),
                LOCAL_CHROME_PATH);

        launchOptions.setExecutablePath(Path.of(chromePath));
        launchOptions.setHeadless(false);
        try (Browser browser = playwright.chromium().launch(launchOptions)) {
            page = browser.newPage();
            page.navigate(BASE_URL);
        } catch (Exception e) {
            log.error("Failed to launch the browser or navigate to the URL.", e);
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        if (playwright != null) {
            playwright.close();
        }
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (var input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Can't find config.properties");
            }
            properties.load(input);
            BASE_URL = properties.getProperty("base.url");
            LOCAL_CHROME_PATH = properties.getProperty("chrome.path");
        } catch (IOException e) {
            logger.error("Error during properties loading.", e);
            throw new RuntimeException("Error during properties loading.", e);
        }
    }
}
