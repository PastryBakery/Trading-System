package phase2.trade.user;

import phase2.trade.config.PermissionConfig;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class UserFactory {

    private static final Logger logger = LogManager.getLogger(UserFactory.class);

    private PermissionConfig permissionConfig;

    public UserFactory(PermissionConfig permissionConfig) {
        this.permissionConfig = permissionConfig;
    }

    public User createByPermissionGroup(String userName, String email, String password, String permissionGroupString) { // country and city are not necessary for all users
        PermissionGroup permissionGroup = PermissionGroup.valueOf(permissionGroupString);
        User user;
        switch (permissionGroup) {
            case ADMIN:
            case HEAD_ADMIN:
                user = new AdministrativeUser(userName, email, password);
                break;

            case REGULAR:
                user = new RegularUser(userName, email, password);
                break;

            case SYSTEM:
                user = new SystemUser();
                break;

            default:
                user = new Guest();
                break;
        }
        user.setPermissionGroup(permissionGroup);
        PermissionSet fromConfig = permissionConfig.getDefaultPermissions().get(permissionGroup);
        if (fromConfig != null) {
            user.setUserPermission(new PermissionSet(fromConfig.getPerm()));
        } else {
            logger.error("Permission Not Set For Group: " + permissionGroup);
        }
        return user;
    }

    public User configureSystemUser() { // system user will not be persistent
        return createByPermissionGroup("SYSTEM", "SYSTEM", "", PermissionGroup.SYSTEM.name());
    }

    public User configureGuest() { // system user will not be persistent
        return createByPermissionGroup("Guest", "guest@example.com", "", PermissionGroup.GUEST.name());
    }
}
