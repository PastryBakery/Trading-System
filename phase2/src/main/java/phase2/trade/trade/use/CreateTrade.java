package phase2.trade.trade.use;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.item.Item;
import phase2.trade.itemlist.TradeItemHolder;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Create trade.
 *
 * @author Dan Lyu
 */
public class CreateTrade {

    private static final Logger logger = LogManager.getLogger(CreateTrade.class);

    private final Map<User, Collection<Item>> usersToItemsToGet;

    private final Trade trade = new Trade();

    private final Collection<Item> allItems = new HashSet<>();

    private final Set<User> usersInvolved = new HashSet<>();

    /**
     * Constructs a new Create trade.
     *
     * @param usersToItemsToGet the users to items to get
     */
    public CreateTrade(Map<User, Collection<Item>> usersToItemsToGet) {
        this.usersToItemsToGet = usersToItemsToGet;
        usersToItemsToGet.values().forEach(allItems::addAll);
    }


    /**
     * Create two way user order bundle.
     *
     * @param initiator          the initiator
     * @param target             the target
     * @param itemsInitiatorWant the items initiator want
     * @param itemsTargetWant    the items target want
     */
    public void createTwoWayUserOrderBundle(User initiator, User target, Collection<Item> itemsInitiatorWant, Collection<Item> itemsTargetWant) {
        // find out what initiator will provide to target and vice versa
        if (initiator == target) return;
        if (trade.ifUserPairInOrder(initiator, target)) return;
        UserOrderBundle initiatorBundle = new UserOrderBundle();
        initiatorBundle.setUser(initiator);
        UserOrderBundle targetBundle = new UserOrderBundle();
        targetBundle.setUser(target);
        TradeItemHolder initiatorHolder = new TradeItemHolder();
        TradeItemHolder targetHolder = new TradeItemHolder();

        initiatorBundle.setTradeItemHolder(initiatorHolder);
        targetBundle.setTradeItemHolder(targetHolder);

        for (Item item : allItems) {
            if (item.getOwner().getUid().equals(initiator.getUid()) && itemsTargetWant.contains(item)) { // initiator owns the item and target wants it
                initiatorHolder.addItem(item);
            } else if (item.getOwner().getUid().equals(target.getUid()) && itemsInitiatorWant.contains(item)) {
                targetHolder.addItem(item);
            }
        }
        TradeOrder order = new TradeOrder();
        order.setRightBundle(targetBundle);
        order.setLeftBundle(initiatorBundle);
        trade.getOrders().add(order);
    }

    /**
     * Cleanup.
     */
// this cleanup doesn't remove order because even if two users do not have transaction, there is a chance user select both of them in the controller since
    // the number of users involved in arbitrary
    public void cleanup() {
        for (TradeOrder order : trade.getOrders()) {
            if (order.getRightBundle().getTradeItemHolder().size() == 0 && order.getLeftBundle().getTradeItemHolder().size() == 0) {
            } else {
                usersInvolved.add(order.getRightUser());
                usersInvolved.add(order.getLeftUser());
            }
        }
        // trade.getOrders().removeAll(ordersToRemove);

        logger.debug("Order Size: " + trade.getOrders().size());
    }

    /**
     * Create order.
     */
    public void createOrder() {
        for (Map.Entry<User, Collection<Item>> entry : usersToItemsToGet.entrySet()) {
            for (Map.Entry<User, Collection<Item>> entry2 : usersToItemsToGet.entrySet()) {
                createTwoWayUserOrderBundle(entry.getKey(), entry2.getKey(), entry.getValue(), entry2.getValue());
                // only need combinations here. But whatever
            }
        }
        cleanup();
    }

    /**
     * Gets trade.
     *
     * @return the trade
     */
    public Trade getTrade() {
        return trade;
    }

    /**
     * Gets users involved.
     *
     * @return the users involved
     */
    public Set<User> getUsersInvolved() {
        return usersInvolved;
    }
}
