package com.comp3350.webudget.persistence.testDatabases;

import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.objects.Group;
import com.comp3350.webudget.persistence.IGroupDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;

import java.util.ArrayList;

public class TestGroupDatabase implements IGroupDatabase {

    ArrayList<Group> database;
    IWalletDatabase walletDatabase = null;
    int groupID = -1;


    public TestGroupDatabase(){
        database = new ArrayList<>();
        walletDatabase = Services.walletPersistence();
    }

    public TestGroupDatabase(IWalletDatabase injectedWalletDatabase){
        database = new ArrayList<>();
        walletDatabase = injectedWalletDatabase;
    }

    @Override
    public int insertGroup(String groupName) throws GroupException{
        groupID++;
        int walletID = walletDatabase.insertWallet(groupName);
        database.add(new Group(groupName, groupID, walletID));
        return groupID;
    }

    @Override
    public Group getGroup(int id) throws GroupException {
        try {
            Group myGroup = database.get(id);
            return myGroup;
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }

    @Override
    public ArrayList<Group> getAllGroups() throws GroupException {
        return new ArrayList<>(database);
    }

    @Override
    public ArrayList<Group> getGroups(String username)throws GroupException {
        return null;
    }




}
