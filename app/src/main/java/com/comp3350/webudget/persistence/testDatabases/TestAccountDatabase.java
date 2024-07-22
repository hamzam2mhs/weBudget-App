package com.comp3350.webudget.persistence.testDatabases;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.objects.Account;
import com.comp3350.webudget.persistence.IAccountDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;

import java.util.ArrayList;

public class TestAccountDatabase implements IAccountDatabase {
    ArrayList<Account> database;
    IWalletDatabase walletDatabase = null;

    public TestAccountDatabase(){
        database = new ArrayList<>();
        walletDatabase = Services.walletPersistence();
    }

    public TestAccountDatabase(IWalletDatabase injectedWalletDatabase){
        database = new ArrayList<>();
        walletDatabase = injectedWalletDatabase;
    }

    @Override
    public void insertUser(String username, String fName, String lName, String password) throws AccountException {
        int walletID = walletDatabase.insertWallet(username);
        database.add(new Account(fName, lName, username, password, walletID));
    }

    @Override
    public Account getAccount(String username) throws AccountException {
        for(int i = 0; i < database.size(); i++){
            Account temp = database.get(i);
            if(temp.getUsername().equals(username))
                return temp;
        }
        return null;
    }

    @Override
    public ArrayList<Account> getAllAccounts() throws AccountException {
        return new ArrayList<>(database);
    }


}
