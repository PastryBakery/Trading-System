package group.item;

import group.item.Item;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.PersonalUser;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private Repository<Item> items;
    private List<Integer> itemsArrayList = new ArrayList<>(); //temp solution

    public ItemManager(Repository<Item> items){
        this.items = items;
    }

    public Response add (Item item) {
        items.add(item);
        return new Response.Builder(true).translatable("itemAdd").build();
    }

    public Response remove(Item item) {
        items.remove(item);
        return new Response.Builder(true).translatable("itemAdd").build();
    }

    public void addAvailable (Integer itemID) {
        itemsArrayList.add(itemID);
    }

    public void removeAvailable (Integer itemID) {
        itemsArrayList.remove(itemID);
    }

    public Item findItemByUid(Integer uid) {
        return items.get(uid);
    }


    public  Response browseItemsDisplay() { StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : itemsArrayList) {
            Item item = items.get(i);
            stringBuilder.append(item.toString()).append("\n");
        }
        return new Response.Builder(true).translatable("allItems", stringBuilder.toString()).build();
    }
}