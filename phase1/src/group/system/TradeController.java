package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.trade.Trade;
import group.trade.TradeManager;
import group.user.PersonalUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Glitch List: Trade ID 0 works even when this trade, pressing exit immediately exists program, normal to keep removing personalUser.csv?
public class TradeController {
    private final TradeManager tradeManager;
    final Repository<Trade> tradeRepository;
    final Repository<PersonalUser> personalUserRepository;
    final UserController userController;
    final PersonalUser currUser;

    /**
     * Takes requests from a user and turns it into information that can be used by TradeManager
     * @param dispatcher The controller dispatcher
     */
    public TradeController(ControllerDispatcher dispatcher, UserController userController){
        tradeRepository = dispatcher.tradeRepository;
        personalUserRepository = dispatcher.personalUserRepository;
        this.userController = userController;
        currUser = userController.getCurrUser();
        tradeManager = new TradeManager(tradeRepository, personalUserRepository, dispatcher.tradeProperties, userController.getItemManager());
        dispatcher.menuController.supportTrade(this);
    }

    /**
     * Creates a new trade
     * @param request All the user's input
     * @return A description of the success of the creation of the trade
     */
    public Response addTrade(Request request) {
        Integer item1;
        Integer item2;
        Integer user2 =  request.getInteger("respondent");

        PersonalUser trader2 = personalUserRepository.get(user2);

        // Check if items are in their inventories or are null
        if (request.get("lendingItem").equals("null")){
            item1 = null;
        } else if (currUser.getInventory().contains(request.getInteger("lendingItem"))){
            item1 = request.getInteger("lendingItem");
        } else {
            return new Response.Builder(false).translatable("failed.create.trade").build();
        }
        if (request.get("borrowingItem").equals("null")){
            item2 = null;
        } else if (trader2.getInventory().contains(request.getInteger("borrowingItem"))){
            item2 = request.getInteger("borrowingItem");
        } else {
            return new Response.Builder(false).translatable("failed.create.trade").build();
        }

        Boolean isPermanent = request.getBoolean("isPermanent");
        String[] data = request.get("dateAndTime").split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        String location =  request.get("location");
        return tradeManager.createTrade(currUser.getUid(), user2, item1, item2, isPermanent, dateAndTime, location);
    }

    /**
     * Edits the date and time of a trade
     * @param request All the user's input
     * @return A description of the success of editing date and time
     */
    public Response editMeetingDateAndTime(Request request){
        Integer tradeID = request.getInt("tradeID");
        String[] data = request.get("dateAndTime").split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        return tradeManager.editDateAndTime(tradeID, (int) currUser.getUid(), dateAndTime);
    }

    /**
     * Edits the location of a trade
     * @param request All the user's input
     * @return A description of the success of editing location
     */
    public Response editMeetingLocation(Request request){
        Integer tradeID = request.getInt("tradeID");
        String location = request.get("location");
        return tradeManager.editLocation(tradeID, (int) currUser.getUid(), location);
    }

    /**
     * Confirms that a trade will occur
     * @param request All the user's input
     * @return A description of the state of confirmation
     */
    public Response confirmingTradeOpen(Request request){
        Integer tradeID = request.getInt("tradeID");
        return tradeManager.confirmTrade(tradeID, (int) currUser.getUid());
    }

    /**
     * Confirms the a trade has happened
     * @param request All the user's input
     * @return A description of the state of confirmation
     */
    public Response confirmingTradeComplete(Request request){
        Integer tradeID = request.getInt("tradeID");
        return tradeManager.confirmTradeComplete(tradeID, (int) currUser.getUid());
    }

    /**
     * @param input The user's input
     * @return True iff the input can be parsed into a Boolean
     */
    public boolean isBool(String input){
       return (input.equals("true") || input.equals("false"));
    }

    /**
     * @param input The user's input
     * @return True iff the input is correct item input
     */
    public boolean isAnItem(String input){
        try {
            if (input.equals("null")){
                return true;
            }
            Integer.parseInt(input);
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    public Response getRecentTrades(Request request){
        ArrayList<Integer> recentCompleteTrades = currUser.getRecentCompleteTrades();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : recentCompleteTrades) {
            Trade trade = tradeRepository.get(i);
            stringBuilder.append(trade.toString()).append("\n");
        }
        return new Response.Builder(true).translatable("recentTrades", stringBuilder.toString()).build();
    }

    public Response getAllTrades(Request request){
        List<Integer> allTrades = currUser.getTrades();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : allTrades) {
            Trade trade = tradeRepository.get(i);
            stringBuilder.append(trade.toString()).append("\n");
        }
        return new Response.Builder(true).translatable("allTrades", stringBuilder.toString()).build();
    }
}
