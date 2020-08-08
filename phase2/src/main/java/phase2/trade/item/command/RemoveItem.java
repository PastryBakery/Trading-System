package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
public class RemoveItem extends ItemCommand {

    private Long itemId;

    private transient RegularUser operator;

    private Ownership oldOwnership;

    private InventoryType inventoryType;

    public RemoveItem(EntityBundle entityBundle, RegularUser operator, InventoryType inventoryType, Long itemId) {
        super(entityBundle, operator);
        this.itemId = itemId;
        this.operator = operator;
        this.inventoryType = inventoryType;
    }

    public RemoveItem() {
        super();
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) { //
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        entityBundle.getUserGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInItemGateway(itemId);
            operator.getItemList(inventoryType).removeItem(item);

            addEffectedId(itemId);
            save();
            if (callback != null)
                callback.call(item, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
        entityBundle.getItemGateway().submitTransaction(() -> {
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }


    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }
}
