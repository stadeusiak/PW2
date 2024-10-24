package tests;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import sauce.LoginPage;
import utils.BaseTest;

@Slf4j
class SimpleLoginTest extends BaseTest {

    @Test
    public void LoginTest() {
        performLogin();
        verifyLogin();
    }

    @Step("Perform login with username and password")
    protected void performLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(getUsername(), getPassword());
    }

    @Step("Verify login was successful")
    protected void verifyLogin() {
        PlaywrightAssertions.assertThat(page.locator("[data-test=\"inventory-container\"]")).isVisible();
    }
}
