package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false,
        persistent = false, permissionSet = {Permission.CREATE_USER})
public class GetMarketItems extends ItemCommand<List<Item>> {

    public GetMarketItems() {
    }

    @Override
    public void execute(StatusCallback<List<Item>> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getItemGateway().submitSession((gateway) -> callback.call(gateway.findMarketItems(), new StatusSucceeded()));
    }

    @Override
    public void undo() {

    }
}
