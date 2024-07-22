package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.WalletException;

public interface IWalletLogic {
    public int getAmount(int id) throws WalletException;
    public void deposit(int id, int amount) throws WalletException;
    public void withdraw(int id, int amount) throws WalletException;
}

