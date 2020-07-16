package group.system;

import group.menu.MenuBuilder;
import group.menu.MenuBuilder.OperationType;
import group.menu.MenuBuilder.ValidatingType;
import group.menu.MenuLogicController;
import group.menu.processor.PasswordEncryption;
import group.menu.validator.*;
import group.notification.SupportTicket;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;
import group.user.User;

/**
 * {\__/}
 * (●_●)
 * ( >🌮
 */

// TODO: succeeded / failed / master can all be ignored if you don't use them.
//   The addon can also be ignored as long as there are no two options that have same class and operation type
// When Response success == true, the "master.support.ticket" will be the next menu
// When Response success == false, the "master.account" will be the next menu
// When you define "master.support.trade" in your response object, "master.support.trade" will be next. To use custom masters, they have to be put in here

public class MenuController {

    private final MenuBuilder menuBuilder;
    private final ValidatorFactory validatorFactory;

    public MenuController() {
        menuBuilder = new MenuBuilder();
        validatorFactory = new ValidatorFactory();
    }

    // re building menu from scratch, see menu design at bottom of google doc
    public void mainMenu(UserController userController, AdministrativeUserController administrativeUserController) {
        menuBuilder.option(User.class, OperationType.verification, 1, "login")
                .input("username")
                .submit("password", userController::loginUser)
                .succeeded("master.userAccess").failed("master.account").master("master.account");

        menuBuilder.option(AdministrativeUser.class, OperationType.verification, 2, "login")
                .input("username")
                .submit("password", administrativeUserController::loginAdminUser)
                .succeeded("master.adminAccess").failed("master.account").master("master.account");

        menuBuilder.option(User.class, OperationType.add, 3, "register")
                .input("username", validatorFactory.getValidator(ValidatorFactory.Type.USER_NAME))
                .input("email", new EmailValidator())
                .input("telephone", validatorFactory.getValidator(ValidatorFactory.Type.TELEPHONE))
                .submit("password", validatorFactory.getValidator(ValidatorFactory.Type.PASSWORD), ValidatingType.invalid, userController::registerUser)
                .succeeded("master.account").failed("master.account").master("master.account");


        menuBuilder.option(AdministrativeUser.class, OperationType.add, 4, "register")
                .input("username", validatorFactory.getValidator(ValidatorFactory.Type.USER_NAME))
                .input("email", new EmailValidator())
                .input("telephone", validatorFactory.getValidator(ValidatorFactory.Type.TELEPHONE))
                .input("password", new PasswordEncryption(), validatorFactory.getValidator(ValidatorFactory.Type.PASSWORD), ValidatingType.invalid)
                .submit("isHead", administrativeUserController::registerAdminUser)
                .succeeded("master.account").failed("master.account").master("master.account");

        menuBuilder.construct("master.account", true);

    }

    public void personalUserAccess(UserController controller) {

        menuBuilder.option(PersonalUser.class, OperationType.query, 1, "account")
                .submit("enter", controller::checkFrozen)
                .succeeded("master.view.account").failed("master.view.account").master("master.account");

        menuBuilder.option(PersonalUser.class, OperationType.query, 2, "trade")
                .submit("enter", controller::checkFrozen)
                .succeeded("master.support.trade").failed("master.userAccess").master("master.account");

        menuBuilder.construct("master.userAccess", false);
    }

    public void viewAccount(UserController userController) {

        menuBuilder.option(User.class, OperationType.verification, 1, "browseItems")
                .submit("browseAllItems", userController::browseAllItems)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.view, 2, "wishlist")
                .submit("browseWishlist", userController::browseWishlist)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.add, 3, "wishlist")
                .submit("item", userController::AddItemToWishlist)
                .succeeded("master.view.account").failed("master.view.account").master("");

        menuBuilder.option(User.class, OperationType.remove, 4, "wishlist")
                .submit("itemname", userController::removeItemFromWishlist)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.view, 5, "inventory")
                .submit("browseInventory", userController::browseInventory)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.add, 6, "inventory")
                .input("item", null, ValidatingType.invalid)
                .submit("description", userController::RequestAddNewItem)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.remove, 7, "inventory")
                .submit("itemname", userController::removeItemFromInventory)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.add, 8, "requestUnfreeze")
                .submit("unfreeze", userController::RequestUnfreeze)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.construct("master.view.account", false);
    }

    public void supportTrade(TradeController controller) {
        menuBuilder.option(Trade.class, OperationType.add, 1)
                .input("initiator", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.exists)
                .input("respondent", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.exists)
                .input("lendingItem", null, controller::isAnItem, ValidatingType.invalid)
                .input("borrowingItem", null, controller::isAnItem, ValidatingType.invalid)
                .input("isPermanent", String::toLowerCase, controller::isBool, ValidatingType.invalid)
                .input("dateAndTime", String::toUpperCase, new DateValidator(), ValidatingType.invalid)
                .input("location", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::addTrade)
                .succeeded("master.support.trade").failed("master.support.trade").master("submit.trade.represent",
                "failed.create.trade");

        menuBuilder.option(Trade.class, OperationType.edit, 2, "Date/Time")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .input("dateAndTime", String::toUpperCase, new DateValidator(), ValidatingType.invalid)
                .submit("confirm", controller::editMeetingDateAndTime)
                .succeeded("master.support.trade").failed("master.support.trade").master("submit.trade.represent",
                "failed.edit.trade");

        menuBuilder.option(Trade.class, OperationType.edit, 3, "Location")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .input("location", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::editMeetingLocation)
                .succeeded("master.support.trade").failed("master.support.trade").master("submit.trade.represent",
                "failed.edit.trade");

        menuBuilder.option(Trade.class, OperationType.verification, 4, "Open")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .submit("confirm", controller::confirmingTradeOpen)
                .succeeded("master.support.trade").failed("master.support.trade").master("success.confirm.trade.open",
                "success.confirm.trade.wait", "failed.confirm.trade");

        menuBuilder.option(Trade.class, OperationType.verification, 5, "Complete")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .submit("confirm", controller::confirmingTradeComplete)
                .succeeded("master.support.trade").failed("master.support.trade").master("failed.confirm.trade",
                "success.confirm.trade.complete.perm", "success.confirm.trade.complete.temp",
                "success.confirm.trade.wait");

        menuBuilder.construct("master.support.trade", false);
    }

    public void supportTicket(SupportTicketController controller) {
        menuBuilder.option(SupportTicket.class, OperationType.add, 1)
                .input("content", controller::ifTicketContentNotExist, ValidatingType.exists)
                .input("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid)
                .input("priority", String::toUpperCase, new EnumValidator<>(SupportTicket.Priority.class), ValidatingType.invalid)
                .submit("confirm", controller::addTicket)
                .succeeded("master.support.ticket");

        menuBuilder.option(SupportTicket.class, OperationType.query, 2)
                .submit("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid, controller::getTicketsByCategory)
                .succeeded("master.support.ticket").failed("master.account").master("master.support.trade");

        menuBuilder.construct("master.support.ticket", false);
    }

    public void adminUserAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 1, "addSunadmin")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .input("email", null, null, ValidatingType.invalid)
                .input("telephone", null, null, ValidatingType.notexist)
                .submit("password", password -> password.length() > 8, ValidatingType.invalid, controller::addSubAdmin)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccess");

        /*menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "findUser")
                .submit("username", null, ValidatingType.notexist, controller::findUserForAdmin)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");*/

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "confirmAdd")
                .submit("confirm", null, ValidatingType.notexist, controller::loginAdminUser)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 3, "removeItem")
                .input("username", null, ValidatingType.notexist)
                .submit("item", controller::removeItemInUserInventory)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 4, "confirmFreeze")
                .submit("confirm", null, ValidatingType.notexist, controller::loginAdminUser)
                .succeeded("master.adminUserFreezeAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 5, "confirmUnfreeze")
                .submit("confirm", null, ValidatingType.notexist, controller::loginAdminUser)
                .succeeded("master.adminUserUnfreezeAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.option(AdministrativeUser.class, OperationType.edit, 6, "limit")
                .submit("confirm", null, ValidatingType.notexist, controller::loginAdminUser)
                .succeeded("master.adminUserLimitAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.construct("master.adminAccess", false);
    }


    public void adminUserAddItemAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.view, 1, "allAddItemRequest")
                .submit("confirm", controller::viewAddItemRequest)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminUserAddItemAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "confirmAddItem")
                .input("fine.username", null, ValidatingType.notexist)
                .submit("item", controller::confirmAddItemRequest)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminUserAddItemAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 3, "confirmAddItemAUser")
                .submit("username", controller::confirmAddAllItemRequestForAUser)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminUserAddItemAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 4, "confirmAddAllUser")
                .submit("confirm", controller::confirmAddAllItemRequest)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminUserAddItemAccess").master("adminAccount");

        menuBuilder.construct("master.adminUserAddItemAccess", false);
    }


    public void adminUserFreezeAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.view, 1, "freezelist")
                .submit("confirm", null, ValidatingType.notexist, controller::viewFreezeUserList)
                .succeeded("master.adminUserFreezeAccess").failed("master.adminUserFreezeAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 4, "confirmFreezeUser")
                .submit("username", controller::confirmFreezeUser)
                .succeeded("master.adminUserFreezeAccess").failed("master.adminUserFreezeAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 5, "confirmFreezeAllUser") //TODO
                .submit("confirm", controller::confirmFreezeAllUser)
                .succeeded("master.adminUserFreezeAccess").failed("master.adminUserFreezeAccess").master("adminAccount");

        menuBuilder.construct("master.adminUserFreezeAccess", false);
    }

    public void adminUserUnfreezeAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.view, 1, "unfreezeRequest")
                .submit("confirm", controller::viewUnfreezeRequest)
                .succeeded("master.adminUserUnfreezeAccess").failed("master.adminUserUnfreezeAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "confirmUnFreezeUser")
                .submit("username", controller::confirmUnFreezeUser)
                .succeeded("master.adminUserUnfreezeAccess").failed("master.adminUserUnfreezeAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 3, "confirmUnFreezeAllUser")
                .submit("confirm", controller::confirmUnFreezeAllUser)
                .succeeded("master.adminUserUnfreezeAccess").failed("master.adminUserUnfreezeAccess").master("adminAccount");

        menuBuilder.construct("master.adminUserUnfreezeAccess", false);
    }


    public void adminUserLimitAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.view, 1, "viewLendBeforeBorrowLimit")
                .submit("confirm", controller::viewLendBeforeBorrowLimit)
                .succeeded("master.adminLimitAccess").failed("master.adminLimitAccess").master("adminAccount");


        menuBuilder.option(AdministrativeUser.class, OperationType.view, 2, "TransLimit")
                .submit("confirm", controller::viewTransactionLimit)
                .succeeded("master.adminLimitAccess").failed("master.adminLimitAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.edit, 3, "LandBeforeBorrowLimit")
                .submit("limit", controller::setLendBeforeBorrowLimit)
                .succeeded("master.adminLimitAccess").failed("master.adminLimitAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.edit, 4, "TransLimit")
                .submit("limit", controller::setTransactionLimit)
                .succeeded("master.adminLimitAccess").failed("master.adminLimitAccess").master("adminAccount");

        menuBuilder.construct("master.adminUserLimitAccess", false);
    }

    public MenuLogicController generateMenuLogicController() {
        return new MenuLogicController(menuBuilder.constructFinal());
    }


}
