import org.junit.jupiter.api.Test;
import sauce.LoginPage;

class SauceDemoTest extends BaseTest {

    @Test
    public void SimpleLoginTest() {
        LoginPage loginPage = new LoginPage(page);
        long startTime = System.currentTimeMillis();

        loginPage.login("standard_user", "secret_sauce");

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Test execution time: " + duration + " milliseconds");
    }
}
