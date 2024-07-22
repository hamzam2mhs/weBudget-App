package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.objects.Account;
import com.comp3350.webudget.objects.Transaction;
import com.comp3350.webudget.objects.Wallet;
import com.comp3350.webudget.persistence.IAccountDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;

import java.util.ArrayList;

public class UserWalletLogic implements IUserWalletLogic {

    private IAccountDatabase accountPersistence;
    private IWalletLogic walletLogic;


    //default constructor
    public UserWalletLogic() {
        accountPersistence = Services.accountPersistence();
        walletLogic = Services.walletLogic();
    }

    //injectable constructor
    public UserWalletLogic(final IAccountDatabase accountPersistence, final IWalletLogic walletLogic) {
        this.accountPersistence = accountPersistence;
        this.walletLogic = walletLogic;
    }

    private int getWalletID(String id) throws AccountException{
        Account user = this.accountPersistence.getAccount(id);
        if(user == null){
            throw new AccountException("No Account with username " + id + " found.");
        }
        return user.getWalletID();
    }

    @Override
    public int getAmount(String id) throws AccountException, WalletException {
        int walletID = getWalletID(id);
        return walletLogic.getAmount(walletID);
    }

    @Override
    public void deposit(String id, int amount) throws AccountException, WalletException {
        int walletID = getWalletID(id);
        walletLogic.deposit(walletID, amount);
    }

    @Override
    public void deposit(String id, String amount) throws AccountException, WalletException {
        int walletID = getWalletID(id);
        walletLogic.deposit(walletID, Integer.parseInt(amount));
    }

    @Override
    public void withdraw(String id, int amount) throws AccountException, WalletException {
        int walletID = getWalletID(id);
        walletLogic.withdraw(walletID, amount);
    }
}
