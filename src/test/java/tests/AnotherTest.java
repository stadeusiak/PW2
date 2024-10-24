package tests;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Step;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import pages.LoginPage;
import utils.BaseTest;


class AnotherTest extends BaseTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
    public void LoginTest2(String username, String password) {
        performLoginParams(username, password);
        verifyLogin();
    }

    @Step("Perform login with username and password")
    protected void performLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(this);
    }

    @Step("Perform login with username and password")
    protected void performLoginParams(String user, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginPage.enterCredentials(user, password);
        loginPage.submitLogin();
    }

    @Step("Verify login was successful")
    protected void verifyLogin() {
        PlaywrightAssertions.assertThat(page.locator("[data-test=\"inventory-container\"]")).isVisible();
    }
}
