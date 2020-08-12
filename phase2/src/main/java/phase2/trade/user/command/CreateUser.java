package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.StatusExist;
import phase2.trade.callback.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = true,
        persistent = true, permissionSet = {Permission.CREATE_USER})
public class CreateUser extends UserCommand<User> {

    private Long userId;

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) { // username, email, password, permission_group
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            List<User> usersByName = gateway.findByUserName(args[0]);
            List<User> usersByEmail = gateway.findByEmail(args[1]);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                User user = new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig()).createByPermissionGroup(args[0], args[1], args[2], args[3], args[4], args[5]);
                gateway.add(user);
                userId = user.getUid();
                addEffectedEntity(User.class, user.getUid());
                save();
                callback.call(user, new StatusSucceeded());
            } else {
                callback.call(null, new StatusExist());
            }
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            gateway.delete(userId);
        });
    }
}
