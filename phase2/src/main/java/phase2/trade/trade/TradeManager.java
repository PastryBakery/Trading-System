package phase2.trade.trade;

import phase2.trade.config.property.TradeProperties;
import phase2.trade.database.Callback;
import phase2.trade.database.TradeDAO;
import phase2.trade.item.Item;
import phase2.trade.user.Address;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
public class TradeManager {
    private TradeProperties tradeProperties;
    private Integer editLimit;
    private Integer timeLimit;
    private TradeBuilder tb;
    private TradeEditor te;

    public TradeManager(TradeProperties tradeProperties){
        this.tradeProperties = tradeProperties;
        editLimit = tradeProperties.getInt("editLimit");
        timeLimit = tradeProperties.getInt("timeLimit");
        tb = new TradeBuilder();
        te = new TradeEditor(editLimit);
    }

    public Trade createTrade(){
        return tb.buildTrade();
    }

    public Trade editDateAndTime(Trade currTrade, Long orderID, User editingUser, LocalDateTime dateAndTime) {
        TradeEditor te = new TradeEditor(editLimit);
        return te.editDateAndTime(currTrade, orderID, editingUser, dateAndTime);
    }

    public Trade editLocation(Trade currTrade, Long orderID, User editingUser, Address location) {
        TradeEditor te = new TradeEditor(editLimit);
        return te.editLocation(currTrade, orderID, editingUser, location);
    }

//    public void confirmTrade(int editingUser, Trade currTrade) { currTrade.getStrategy().confirmTrade(editingUser);}
}

