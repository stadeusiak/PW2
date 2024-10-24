package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Step;
import utils.BaseTest;

public class LoginPage {

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void enterCredentials(String login, String password) {
        page.fill("#user-name", login);
        page.fill("#password", password);
    }

    public void submitLogin() {
        page.click("#login-button");
    }

    @Step("Login with hidden credentials")
    public void login(BaseTest baseTest) {
        String[] credentials = baseTest.getCredentials();
        enterCredentials(credentials[0], credentials[1]);
        submitLogin();
    }

    @Step("Verify login was successful")
    public void verifySuccessfulLogin() {
        PlaywrightAssertions.assertThat(page.locator("[data-test=\"inventory-container\"]")).isVisible();
    }
}