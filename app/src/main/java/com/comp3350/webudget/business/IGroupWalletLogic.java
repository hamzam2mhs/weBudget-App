package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.Exceptions.WalletException;

public interface IGroupWalletLogic {
    public int getAmount(String id) throws GroupException, WalletException;
    public void deposit(String id, int amount) throws GroupException, WalletException;
    public void deposit( String id, String amount ) throws GroupException, WalletException;
    public void withdraw(String id, int amount) throws GroupException, WalletException;
}
