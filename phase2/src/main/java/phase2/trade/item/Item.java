package phase2.trade.item;


import phase2.trade.inventory.ItemList;

import javax.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String name;
    private String description;
    private Category category;

    private Ownership ownership;

    private Willingness willingness = Willingness.NOPE;

    private int quantity;

    private double price;

    @ManyToOne
    private ItemList itemList;

    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList inventory) {
        this.itemList = inventory;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    public Willingness getWillingness() {
        return willingness;
    }

    public void setWillingness(Willingness willingness) {
        this.willingness = willingness;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
