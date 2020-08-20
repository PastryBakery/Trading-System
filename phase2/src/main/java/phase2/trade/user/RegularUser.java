package phase2.trade.user;

import phase2.trade.itemlist.Cart;
import phase2.trade.itemlist.Inventory;
import phase2.trade.itemlist.ItemList;
import phase2.trade.itemlist.ItemListType;
import phase2.trade.permission.PermissionGroup;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class RegularUser extends User {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Inventory inventory;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cart cart;

    private int accountBalance;

    //private int incompleteTrades;

    public RegularUser(String userName, String email, String password) {
        super(userName, email, password);
        //incompleteTrades = 0;

        inventory = new Inventory();
        inventory.setOwner(this);

        cart = new Cart();
        cart.setOwner(this);

        this.accountBalance = 0;

        setPermissionGroup(PermissionGroup.REGULAR);
    }

    public RegularUser() {

    }

    @Override
    public void lazyLoad() {
        super.lazyLoad();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public ItemList getItemList(ItemListType itemListType) {
        switch (itemListType) {
            case CART:
                return cart;
            case INVENTORY:
                return inventory;
        }
        return null;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }
}
