package com.comp3350.webudget.persistence;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.objects.Wallet;

public interface IWalletDatabase {

    public int insertWallet(String username);
    public Wallet getWallet(int id) throws WalletException;
    public void deposit(int walletID, int amount) throws WalletException;
    public void withdraw(int walletID, int amount) throws WalletException;
}
