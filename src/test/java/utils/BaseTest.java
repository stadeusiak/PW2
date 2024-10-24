package utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@ExtendWith(TestWatcherExtension.class)
public abstract class BaseTest {

    protected BrowserContext context;
    private String BASE_URL;
    private String LOCAL_CHROME_PATH;
    protected static Playwright playwright;
    protected static Browser browser;
    protected Page page;
    private static String USERNAME;
    private static String PASSWORD;
    private enum EnvironmentType {
        LOCAL, JENKINS
    }

    @BeforeEach
    public void setupTest() {
        loadProperties();
        playwright = Playwright.create();
        var launchOptions = new BrowserType.LaunchOptions();

        String chromePath = Objects.requireNonNullElse(System.getenv("CHROME_PATH"), LOCAL_CHROME_PATH);
        log.info("Using Chrome executable path: " + chromePath);

        launchOptions.setExecutablePath(Path.of(chromePath));
        launchOptions.setHeadless(false);

        EnvironmentType environment = determineEnvironment();

        switch (environment) {
            case LOCAL -> {
                log.info("Running in LOCAL mode (headful)");
                launchOptions.setHeadless(false);
            }
            case JENKINS -> {
                log.info("Running in JENKINS mode (headless)");
                launchOptions.setHeadless(true);
            }
            default -> throw new IllegalStateException("Unknown environment type: " + environment);
        }

        try {
            browser = playwright.chromium().launch(launchOptions);
            page = browser.newPage();
            page.navigate(BASE_URL);
        } catch (Exception e) {
            log.warn("Failed to launch the browser or navigate to the URL.", e);
            throw new RuntimeException(e);
        }

        USERNAME = System.getenv("USERNAME");
        PASSWORD = System.getenv("PASSWORD");

        if (USERNAME == null || PASSWORD == null) {

           log.warn("Login credentials are not set in environment variables.");
           log.warn(    "Login credentials are not set in environment variables.");

        }
    }

    private EnvironmentType determineEnvironment() {
        if (System.getenv("CI") != null || System.getenv("CHROME_PATH") != null) {
            return EnvironmentType.JENKINS;
        }
        return EnvironmentType.LOCAL;
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
            log.error("Error during properties loading.", e);
            throw new RuntimeException("Error during properties loading.", e);
        }
    }

    public String[] getCredentials() {
        return new String[]{USERNAME, PASSWORD};
    }

    @AfterEach
    public void tearDown() {
        if (TestWatcherExtension.isTestFailed()) {
            Optional.ofNullable(page).ifPresent(Page::close);
            Optional.ofNullable(context).ifPresent(BrowserContext::close);
        }
    }

    @AfterAll
    public static void tearDownAll() {
        Optional.ofNullable(browser).ifPresent(Browser::close);
        Optional.ofNullable(playwright).ifPresent(Playwright::close);
    }
}
