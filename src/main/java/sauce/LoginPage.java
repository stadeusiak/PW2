package sauce;

import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void login(String login, String password) {
        page.fill("#user-name", login);
        page.fill("#password", password);
        page.click("#login-button");
    }
}
