package com.comp3350.webudget.persistence;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.objects.Account;

import java.util.ArrayList;

public interface IAccountDatabase {
    public void insertUser(String username, String fName, String lname, String password)  throws AccountException;
    public Account getAccount(String username) throws AccountException;
    public ArrayList<Account> getAllAccounts() throws AccountException;
}
