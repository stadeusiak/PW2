import org.junit.jupiter.api.Test;
import sauce.LoginPage;

class SimpleLoginTest extends BaseTest {

    @Test
    public void LoginTest() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(getUsername(), getPassword());
    }
}
