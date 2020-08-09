package phase2.trade.user;


import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.database.UserDAO;
import phase2.trade.inventory.Cart;
import phase2.trade.inventory.Inventory;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddToCart;
import phase2.trade.item.command.ItemCommand;
import phase2.trade.user.command.UserCommand;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;


public class PersonalUserManager{ //extends UserCommand<User>{

    private RegularUser loggedInUser;

    //private ItemCommand itemCommand;

    //private EntityBundle entityBundle;

    public PersonalUserManager(RegularUser loggedInUser, EntityBundle entityBundle, ItemCommand itemCommand) {
        this.loggedInUser = loggedInUser;
        //super((GatewayBundle) entityBundle);
    }

    public void changeUsername(String username){
        loggedInUser.setUserName(username);
    }

    public void changeEmail(String email){
        loggedInUser.setEmail(email);
    }

    public void changePassword(String password){
        loggedInUser.setPassword(password);
    }

    public void changeCity(String city){
        loggedInUser.setCity(city);
    }

    public void changeCountry(String country){
        loggedInUser.setCountry(country);
    }



    public void addToBeReviewedItem(Category category, String itemName, String description) {
        Item item = new Item();
        item.setCategory(category);
        item.setName(itemName);
        item.setDescription(description);
        loggedInUser.getInventory().addItem(item);
    }

    public void removeItemFromInventory(Long itemId) {
        loggedInUser.getInventory().removeItemByUid(itemId);
    }

    public void addItemToWishlist(Item item) {
        loggedInUser.getCart().addItem(item);
    }

    public void removeItemFromWishlist(Long itemId) {
        loggedInUser.getCart().removeItemByUid(itemId);
    }

    public void UnfreezeRequest(RegularUser user) {
        user.setAccountState(AccountState.REQUEST_UNFROZEN);
    }

    public Inventory getUserInventory(RegularUser user) {
        return user.getInventory();
    }

    public Cart getUserWishlist(RegularUser user) {
        return user.getCart();
    }

    /*public List<RegularUser> suggest(){
        List<Tuple> ans = new ArrayList<>();
        List<User> pu = new ArrayList<>();
        List<User> allUser = userDAO.findAllUser();
        for (User user : allUser){
            if (user instanceof PersonalUser){
                pu.add((PersonalUser) user);
            }
        }
        for (Item i : loggedInUser.getWishList()) {
             for (PersonalUser p : pu) {
                if (p.getInventory().contains(i)){
                    ans.add(new tuple(i,p))
                }
            }
        return ans;
    }

    /*public void suggest (PersonalUser p) {
        for (PersonalUser x : personalUserRepository) {
            for (Item i : x.getInventory()) {
                if (p.getWishlist().contains(i)) {
                    p.suggest(i);
                }
            }
        }
    }*/

}


