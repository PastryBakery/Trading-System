package group.user;

import java.util.*;
import group.item.Item;

public class PersonalUser extends User {

    private List<Item> wishlist;       //TODO: replace Object with item when item is available
    private List<Item> inventory;
    private List<Item> trades;
    private boolean isFrozen;
    private int lendCount;
    private int borrowCount;
    private int numTransactions;
    private Map<String, Integer> traderFrequency;

    /**
     * Creates a PersonalUser with the given userName, email, telephone, password
     * and initializes all other instance variables.
     *
     * @param userName username of this user
     * @param email email of this user
     * @param telephone telephone number of this user
     * @param password password of this user
     */
    public PersonalUser (String userName, String email, String telephone, String password) {
        super(userName, email, telephone, password);
        wishlist = new ArrayList<>();
        inventory = new ArrayList<>();
        trades = new ArrayList<>();
        isFrozen = false;
        lendCount = 0;
        borrowCount = 0;
        numTransactions = 0;
        traderFrequency = new HashMap<>();
    }

    public PersonalUser (String userName, String email, String password) {
        super(userName, email, password);
        wishlist = new ArrayList<>();
        inventory = new ArrayList<>();
        trades = new ArrayList<>();
        isFrozen = false;
        lendCount = 0;
        borrowCount = 0;
        numTransactions = 0;
        traderFrequency = new HashMap<>();
    }

    public PersonalUser(List<String> record){super(record);}

    public List<Item> getWishlist() {
        return wishlist;
    }

    public void removeFromWishList(Long oldItem){
        Item removeItem = null;
        for (Item item: wishlist){
            if (item.getUid() == oldItem){
                removeItem = item;
            }
        }
        wishlist.remove(removeItem);}

    public List<Item> getInventory() {
        return inventory;
    }

    // public void addToInventory(Item newItem){ inventory.add(newItem);}

    public void removeFromInventory(Long oldItem){
        Item removeItem = null;
        for (Item item: inventory){
            if (item.getUid() == oldItem){
                removeItem = item;
            }
        }
        inventory.remove(removeItem);
    }

    public List<Item> getTrades() {
        return trades;
    }

    public boolean getIsFrozen() {
        return isFrozen;
    }

    public void setIsFrozen (boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    public int getLendCount() {
        return lendCount;
    }

    public void setLendCount(int lendCount) {
        this.lendCount = lendCount;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public int getNumTransactions() {
        return numTransactions;
    }

    public void setNumTransactions(int numTransactions) {
        this.numTransactions = numTransactions;
    }

    public Map<String, Integer> getTraderFrequency() {
        return traderFrequency;
    }

    /**
     * returns the usernames of the top three most frequent traders for this user as a map.
     * if this user has not traded with three different users, returns top 2 or the top
     * trader accordingly.
     * @return map of the top 3 most frequent traders for this user
     */
    public Map<String, Integer> getTopThreeTraders() {
        int len = traderFrequency.keySet().toArray().length;
        if (traderFrequency.isEmpty()) {
            return null;
        } else if (len <= 3) {
            return traderFrequency;
        } else {
            Map<String, Integer> ans = new HashMap<>();
            List<Integer> v = (ArrayList<Integer>) traderFrequency.values();
            Collections.sort(v);
            v = v.subList(v.size() - 3, v.size());
            for (int i = 0; i < 3; i++) {
                ans.put(keyFromValue(v.get(i)), v.get(i));
            }
            return ans;
        }
    }

    /**
     * helper method that returns a key from a value only for a one-to-one Map<String, Integer></>
     * type map.
     * @param value
     * @return key mapped to the given value
     */
   private String keyFromValue(Integer value) {
        for (Map.Entry<String , Integer> entry : traderFrequency.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "PersonalUser" + super.toString();
    }
}
