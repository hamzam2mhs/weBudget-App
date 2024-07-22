package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.WalletException;

import java.util.ArrayList;

public interface IUserWalletLogic {
    public int getAmount(String username) throws AccountException, WalletException;
    public void deposit(String username, int amount) throws AccountException, WalletException;
    public void deposit( String username, String amount ) throws AccountException, WalletException;
    public void withdraw(String username, int amount) throws AccountException, WalletException;
}
