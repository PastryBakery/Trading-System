package group.user;

import group.item.Item;
import group.menu.data.Response;
import group.repository.Repository;

import java.util.*;

public class AdministrativeManager { //TODO where to find request of unfreeze user and request of adding book


    private Repository<AdministrativeUser> administrators;
    private Repository<PersonalUser> personalUserRepository;
    private Iterator<PersonalUser> needToFreezelist;
    private int transactionLimit = 100; //what is the init limit?
    private int lendBeforeBorrow = 1;
    private AdministrativeUser currAdmin;
    private PersonalUser currPersonalUser;
    private Iterator<PersonalUser> needToConfirmAddItem;
    private Iterator<PersonalUser> userRequestToUnfreeze;


    public AdministrativeManager(Repository<AdministrativeUser> administrativeUserRepository,
                                 Repository<PersonalUser> personalUserRepository){
        this.administrators = administrativeUserRepository;
        this.personalUserRepository = personalUserRepository;
        needToFreezelist = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
        needToConfirmAddItem = personalUserRepository.iterator(PersonalUser::getAddToInventoryRequestIsNotEmpty);
        userRequestToUnfreeze = personalUserRepository.iterator(PersonalUser::getRequestToUnfreeze);
    }

    public Response createAdministrator(String username, String email, String telephone, String password, boolean isHead){
        AdministrativeUser admin = new AdministrativeUser(username, email, telephone, password, isHead);
        administrators.add(admin);
        return new Response.Builder(true).translatable("success.create.new").build();
    }

    public Response verifyLogin(String username, String password){
         if (administrators.ifExists(
                 AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                         && AdministrativeUser.getPassword().equals(password))){
              currAdmin = administrators.getFirst(
                      AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                      && AdministrativeUser.getPassword().equals(password));
             return new Response.Builder(true).translatable("success.login.user").build();
         }
         return new Response.Builder(false).translatable("failed.login.user").build();
    }

    public Response addSubAdmin(AdministrativeUser head, String username, String email, String telephone, String password){
        if (head.getIsHead()){
            createAdministrator(username, email, telephone, password, false);
            return new Response.Builder(true).translatable("success.add.subadmin").build();
        } else{
            return new Response.Builder(false).translatable("failed.add.subadmin").build();
        }
    }
    public AdministrativeUser getCurrAdmin(){
        return currAdmin;
    }

    public PersonalUser getCurrPersonalUser(){
        currPersonalUser = needToFreezelist.next();
        return currPersonalUser;
    }

    public Iterator<PersonalUser> getListUserShouldBeFreezed(){
        return needToFreezelist;
    }

    public void freezeUser(PersonalUser user){
        user.setIsFrozen(true);
    }

    public void unfreezeUser(PersonalUser user){
        user.setIsFrozen(false);
        user.setRequestToUnfreeze(false);
    }

    public boolean removeUserItem(PersonalUser user, Long item){
        return (user.getInventory()).remove(item);
    }

    public Response confirmAddItem(PersonalUser user) { //TODO where do admin get the request of adding item
        for (Long item : user.getAddToInventoryRequest()) {
            user.addToInventory(item);
            user.getAddToInventoryRequest().remove(item);
        }
        return new Response.Builder(true).translatable("success.confirm.AddItem").build();
    }

    public Response confirmAddAllItemRequest(){
        while (needToConfirmAddItem.hasNext()){
            PersonalUser curruser = needToConfirmAddItem.next();
            for (Long item : curruser.getAddToInventoryRequest()){
                confirmAddItem(curruser);
            }
        }
        return new Response.Builder(true).translatable("success.confirm.allAddItem").build();
    }

    public Iterator<PersonalUser> getUserRequestToUnfreeze(){
        return userRequestToUnfreeze;
    }

    public Response confirmToUnfreezeUser(PersonalUser user){
        unfreezeUser(user);
        return new Response.Builder(true).translatable("success.confirm.unfreeze").build();
    }

    public Response confirmToUnfreezeAllUser(){
        while (userRequestToUnfreeze.hasNext()){
            unfreezeUser(userRequestToUnfreeze.next());
        }
        return new Response.Builder(true).translatable("success.confirm.unfreezeAll").build();
    }

    public Response confirmFreezeCurrUser() {
        freezeUser(currPersonalUser);
        return new Response.Builder(true).translatable("success.confirm.freeze").build();
    }

    public Response confirmFreezeAllUser(){
        while (needToFreezelist.hasNext()){
            currPersonalUser = needToFreezelist.next();
            freezeUser(currPersonalUser);
        }
        return new Response.Builder(true).translatable("success.confirm.freezeAll").build();
    }

    public int getTransactionLimit(){
        return transactionLimit;
    }

    public void setTransactionLimit(int limit){
        transactionLimit = limit;
    }

    public int getLendBeforeBorrowLimit(){
        return lendBeforeBorrow;
    }

    public void setLendBeforeBorrowLimit(int limit){
        lendBeforeBorrow = limit;
    }



        //public void findFreezeUser() {
        //r}


}
