package phase2.trade.item.command;

import phase2.trade.callback.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_PERSONAL_ITEMS})
public class AlterItemInInventory extends ItemCommand<Item> {

    private Long itemId;

    @Override
    public void execute(ResultStatusCallback<Item> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            Item item = operator.getItemList(ItemListType.INVENTORY).findByUid(itemId);
            item.setName(args[0]);
            item.setDescription(args[1]);
            gateway.update(item);
            addEffectedEntity(Item.class, itemId);
            save();
            if (callback != null)
                callback.call(item, new StatusSucceeded());
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            gateway.delete(itemId);
            updateUndo();
        });
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
