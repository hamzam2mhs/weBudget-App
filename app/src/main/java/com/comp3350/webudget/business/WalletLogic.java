package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.objects.Wallet;
import com.comp3350.webudget.persistence.IWalletDatabase;

public class WalletLogic implements IWalletLogic {

    private IWalletDatabase walletPersistence;

    //default constructor
    public WalletLogic() {
        walletPersistence = Services.walletPersistence();
    }

    //injectable constructor
    public WalletLogic(final IWalletDatabase walletPersistence) {
        this.walletPersistence = walletPersistence;
    }

    public int getAmount(int id) throws WalletException {
        Wallet wallet = walletPersistence.getWallet(id);
        return wallet.getBalance();
    }

    public void deposit(int id, int amount) throws WalletException {
        if(amount <= 0 || amount > 99999){
            throw new WalletException("Deposit to wallet must be within range $0 = $99,999");
        }
        walletPersistence.deposit(id, amount);
    }

    public void withdraw(int id, int amount) throws WalletException {
        if(amount <= 0){
            throw new WalletException("Withdraw from wallet must be positive");
        }
        Wallet wallet = walletPersistence.getWallet(id);
        if(amount > wallet.getBalance()){
            throw new WalletException("Cannot withdraw more money than is in the wallet");
        }
        walletPersistence.withdraw(id, amount);
    }
}
