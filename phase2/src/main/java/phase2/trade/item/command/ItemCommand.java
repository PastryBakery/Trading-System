package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.Command;
import phase2.trade.command.PermissionBased;
import phase2.trade.command.UserPermissionChecker;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.item.Item;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public abstract class ItemCommand<T> extends Command<T> implements PermissionBased {

    @OneToOne
    User operator;

    public ItemCommand(GatewayBundle gatewayBundle, User operator) {
        super(gatewayBundle);
        this.operator = operator;
    }

    public ItemCommand() {

    }

    Item findItemByIdSyncInsideItemGateway(Long itemId) {
        return getEntityBundle().getItemGateway().findById(itemId);
    }

    Item findItemByIdSyncOutsideItemGateway(Long itemId) {
        getEntityBundle().getItemGateway().openCurrentSession();
        Item item = getEntityBundle().getItemGateway().findById(itemId);
        getEntityBundle().getItemGateway().closeCurrentSession();
        return item;
    }


    @Override
    public Class<Item> getClassToOperateOn() {
        return Item.class;
    }

    @Override
    public boolean checkPermission() {
        return new UserPermissionChecker(operator, getPermissionRequired()).checkPermission();
    }

    public boolean checkPermission(StatusCallback<?> statusCallback) {
        boolean result = checkPermission();
        if(!result){
            statusCallback.call(null, ResultStatus.NO_PERMISSION);
        }
        return result;
    }

    @Override
    public abstract PermissionSet getPermissionRequired();
}
