package phase2.trade.inventory;

import phase2.trade.item.Item;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity

public class Cart extends ItemList {

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    protected Set<Item> setOfItems = new HashSet<>();

    @Override
    public Set<Item> getSetOfItems() {
        return setOfItems;
    }

    @Override
    public void setSetOfItems(Set<Item> items) {
        this.setOfItems = items;
    }
}
