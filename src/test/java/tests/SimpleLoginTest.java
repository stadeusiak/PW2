package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Link;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import utils.BaseTest;

@Epic("Testy Automatyczne Q4 2024")
@Slf4j
class SimpleLoginTest extends BaseTest {

    @Link(name = "jira", value = "123")
    @Test
    public void LoginTest2() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(this);
        loginPage.verifySuccessfulLogin();
    }
}
