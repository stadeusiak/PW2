import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;
import sauce.LoginPage;

class SimpleLoginTest extends BaseTest {

    @Test
    public void LoginTest() {
        performLogin();
        verifyLogin();
    }

    @Step("Perform login with username and password")
    private void performLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(getUsername(), getPassword());
    }

    @Step("Verify login was successful")
    private void verifyLogin() {
        PlaywrightAssertions.assertThat(page.locator("[data-test=\"inventory-container\"]")).isVisible();
    }
}
