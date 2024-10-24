package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestWatcherExtension implements TestWatcher {

    @Getter
    private static boolean testFailed = false;
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        testFailed = true;
        BaseTest testInstance = (BaseTest) context.getRequiredTestInstance();
        if (testInstance.page != null && !testInstance.page.isClosed()) {
            String testName = context.getDisplayName();
            String screenshotPath = "build/screenshots/" + testName + ".png";
            Path screenshotDir = Paths.get("build/screenshots");

            if (!screenshotDir.toFile().exists()) {
                boolean dirCreated = screenshotDir.toFile().mkdirs();
                if (!dirCreated) {
                    System.err.println("Failed to create directory for screenshots: " + screenshotDir);
                }
            }
            try {
                byte[] screenshot = testInstance.page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)).setFullPage(true));
                System.out.println("Screenshot taken: " + screenshotPath);
                Allure.addAttachment("Failed_Test: " +testName, new ByteArrayInputStream(screenshot));


            } catch (Exception e) {
                System.err.println("Failed to take screenshot: " + e.getMessage());
            } finally {
                testInstance.page.close();
                testInstance.context.close();
            }
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("### TEST PRZESZEDŁ - UDAŁO SIĘ! ###");
    }
}
