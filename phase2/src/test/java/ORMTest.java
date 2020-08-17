import org.junit.Test;
import phase2.trade.config.ConfigBundle;
import phase2.trade.gateway.*;
import phase2.trade.database.*;
import phase2.trade.item.Item;
import phase2.trade.user.User;

import java.util.logging.Level;

public class ORMTest {

    private final EntityGatewayBundle bundle;

    private UserGateway userDAO;
    private ItemGateway itemDAO;

    public ORMTest() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        ConfigBundle configBundle = new ConfigBundle();
        bundle = new DatabaseResourceBundle(configBundle.getDatabaseConfig()).getEntityGatewayBundle();

        userDAO = bundle.getUserGateway();
        itemDAO = bundle.getItemGateway();
    }

    @Test
    public void testItemCommands() {

    }


    @Test
    public void testItemManager() {

        Item item = new Item();
        item.setName("test item2");
        item.setDescription("test description2");

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
        userDAO.openCurrentSession();
        User user = userDAO.findById(1L);
        userDAO.closeCurrentSession();
        return user;
    }

}
