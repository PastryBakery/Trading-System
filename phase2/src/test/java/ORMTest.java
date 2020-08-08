import org.hibernate.cfg.Configuration;
import org.junit.Test;
import phase2.trade.gateway.*;
import phase2.trade.gateway.database.*;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.ItemManager;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.logging.Level;

public class ORMTest {

    private final DatabaseResourceBundle bundle;

    private UserDAO userDAO;
    private ItemDAO itemDAO;
    private DAO<ItemList> itemListDAO;

    public ORMTest() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        bundle = new DatabaseResourceBundleImpl();

        userDAO = new UserDAO(bundle);
        itemDAO = new ItemDAO(bundle);
        itemListDAO = new DAO<>(ItemList.class, bundle);
    }

    @Test
    public void testAccount() {

        PersonalUser user = new PersonalUser("name", "email", "password", "country", "city");

        userDAO.submitSessionSync(() -> userDAO.add(user));
    }

    @Test
    public void testItemManager() {

        Item item = new Item();
        item.setName("test item2");
        item.setDescription("test description2");

        UserDAO userDAO = new UserDAO(bundle);
        userDAO.openCurrentSessionWithTransaction();
        User user = userDAO.findById(1L);
        userDAO.update(user);
        userDAO.closeCurrentSessionWithTransaction();


        // item.setOwner(user);

        // ItemDAO itemDAO = new ItemDAO(Item.class);
        // itemDAO.openCurrentSession();
        // itemDAO.add(item);
        // itemDAO.closeCurrentSession();
    }

    private User getTestUser() {
        UserDAO userDAO = new UserDAO(bundle);
        userDAO.openCurrentSession();
        User user = userDAO.findById(1L);
        userDAO.closeCurrentSession();
        return user;
    }

    @Test
    public void getItemFromUser() throws InterruptedException {
        PersonalUser user = new PersonalUser("name", "email", "password", "country", "city");

        userDAO.submitSessionSync(() -> userDAO.add(user));

        ItemManager itemManager = new ItemManager(bundle, user);

        itemManager.createAndAddItemTo(InventoryType.INVENTORY, new Callback<Item>() {
            @Override
            public void call(Item result) {
                System.out.println(result.getOwnership());
            }
        }, Category.BOOK, "TestName", "TestDescription");

        itemManager.getInventory(InventoryType.INVENTORY, new Callback<ItemList>() {
            @Override
            public void call(ItemList result) {
                System.out.println(result.size());
            }
        });
        // itemDAO.openCurrentSessionWithTransaction();
        // System.out.println(user.getItemListMap());
        // itemDAO.closeCurrentSessionWithTransaction();
    }
}
