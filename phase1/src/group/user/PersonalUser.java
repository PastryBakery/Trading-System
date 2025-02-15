package group.user;

import group.item.Item;
import group.menu.data.Response;
import group.repository.UniqueId;

import java.lang.reflect.Array;
import java.util.*;

public class PersonalUser extends User {

    private List<Integer> wishlist;
    private List<Integer> inventory;
    private List<Integer> trades;
    private List<Integer> supportTickets;
    private Boolean isFrozen;
    private Integer lendCount;
    private Integer borrowCount;
    private Integer numTransactions;
    private List<Integer> recentTrades;
    private List<Integer> addToInventoryRequest;
    private Boolean requestToUnfreeze;
    //private int incompleteTrades;

    /**
     * Creates a PersonalUser with the given userName, email, telephone, password
     * and initializes all other instance variables.
     *
     * @param userName  username of this user
     * @param email     email of this user
     * @param telephone telephone number of this user
     * @param password  password of this user
     */
    public PersonalUser(String userName, String email, String telephone, String password) {
        super(userName, email, telephone, password);
        wishlist = new ArrayList<>();
        inventory = new ArrayList<>();
        trades = new ArrayList<>();
        isFrozen = false;
        lendCount = 0;
        borrowCount = 0;
        numTransactions = 0;
        recentTrades = new ArrayList<>();
        addToInventoryRequest = new ArrayList<>();
        requestToUnfreeze = false;
        //incompleteTrades = 0;
    }

    //public int getIncompleteTrades() {
        //return incompleteTrades;
    //}

    public PersonalUser(List<String> record) {
        super(record);
    }

    public List<Integer> getWishlist() {
        return wishlist;
    }

    public void addToWishList(Integer newItem) {
        wishlist.add(newItem);
    }

    public void removeFromWishList(Integer oldItem) {
        wishlist.remove(oldItem);
    }

    public List<Integer> getInventory() {
        return inventory;
    }

    public void addToInventory(Integer newItem) {
        inventory.add(newItem);
    }

    public void removeFromInventory(Integer oldItem) {
        inventory.remove(oldItem);
    }

    public List<Integer> getTrades() {
        return trades;
    }

    public void addToTrades(Integer newItem) {
        trades.add(newItem);
    }

    public void removeFromTrade(Integer oldItem) {
        trades.remove(oldItem);
    }

    public boolean getIsFrozen() {
        return isFrozen;
    }

    public void setIsFrozen(boolean isFrozen) {
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

    public boolean getShouldBeFreezedUser() {
        return lendCount < borrowCount;
    }

    public int getNumTransactions() {
        return numTransactions;
    }

    public void setNumTransactions(int numTransactions) {
        this.numTransactions = numTransactions;
    }

    public List<Integer> getAddToInventoryRequest() {
        return addToInventoryRequest;
    }

    public void removeAddToInventoryRequest(Integer itemID) {
        addToInventoryRequest.remove(itemID);
    }

    public void addItemToAddToInventoryRequest(Integer item) {
        addToInventoryRequest.add(item);
    }

    public boolean getAddToInventoryRequestIsNotEmpty() {
        return !addToInventoryRequest.isEmpty();
    }

    public void setRequestToUnfreeze(boolean state) {
        requestToUnfreeze = state;
    }

    public boolean getRequestToUnfreeze() {
        return requestToUnfreeze;
    }

    public List<Integer> getSupportTickets() {
        return supportTickets;
    }

    public void addRecentTrades(Integer tradeID) {
        if (recentTrades.size() >= 3) {
            recentTrades.remove(0);
        }
        recentTrades.add(tradeID);
    }

    public List<Integer> getRecentCompleteTrades() {
        return recentTrades;
    }


    /*
     * returns the user IDs of the top three most frequent traders for this user as a map.
     * if this user has not traded with three different users, returns top 2 or the top
     * trader accordingly.
     * @return map of the top 3 most frequent traders for this user
  public void setTraderFrequency(Integer userID) {
        if (traderFrequency.containsKey(userID)){
            traderFrequency.put(userID, traderFrequency.get(userID) + 1);
        } else {
            traderFrequency.put(userID, 1);
        }
    }
    public Map<Integer, Integer> getTopThreeTraders() {
        int len = traderFrequency.keySet().toArray().length;
        if (traderFrequency.isEmpty()) {
            return null;
        } else if (len <= 3) {
            return traderFrequency;
        } else {
            Map<Integer, Integer> ans = new HashMap<>();
            List<Integer> v = (ArrayList<Integer>) traderFrequency.values();
            Collections.sort(v);
            v = v.subList(v.size() - 3, v.size());
            for (int i = 0; i < 3; i++) {
                ans.put(keyFromValue(v.get(i)), v.get(i));
            }
            return ans;
        }
    }

    /*
     * helper method that returns a key from a value only for a one-to-one Map<Integer, Integer></>
     * type map.
     * @param value times traded with someone else
     * @return key mapped to the given value

   private Long keyFromValue(Integer value) {
        for (Map.Entry<Integer , Integer> entry : traderFrequency.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }*/

    @Override
    public String toString() {
        return "PersonalUser" + super.toString();
    }


}
