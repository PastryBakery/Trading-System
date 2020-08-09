package phase2.trade.user;

import phase2.trade.callback.Callback;
import phase2.trade.callback.StatusCallback;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.PermissionGroupFactory;
import phase2.trade.user.command.Login;
import phase2.trade.user.command.Register;

public class AccountManager {

    private User loggedInUser;

    private EntityBundle entityBundle;

    private Login loginCommand;

    private Register registerCommand;

    public AccountManager(GatewayBundle gatewayBundle) {
        this.loginCommand = new Login(gatewayBundle);
        this.registerCommand = new Register(gatewayBundle);
    }

    public void login(StatusCallback<User> callback, String usernameOrEmail, String password) {
        loginCommand.execute((result, status) -> {
            loggedInUser = result;
            callback.call(result, status);
        }, usernameOrEmail, password);
    }


    public void logOut() {
        loggedInUser = null;
    }

    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void register(StatusCallback<User> callback, String userName, String email, String password, String country, String city) {
        registerCommand.execute((result, status) -> {
            loggedInUser = result;
            callback.call(result, status);
        }, userName, email, password, country, city);
    }
}
