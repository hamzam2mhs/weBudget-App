package com.comp3350.webudget.persistence.testDatabases;

import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.objects.Wallet;
import com.comp3350.webudget.persistence.IWalletDatabase;

import java.util.ArrayList;

public class TestWalletDatabase implements IWalletDatabase {
    ArrayList<Wallet> database;
    static int walletID = -1;

    public TestWalletDatabase(){
        database = new ArrayList<>();
    }

    @Override
    public int insertWallet(String ownerName) {
        walletID++;
        database.add(new Wallet(walletID, 0));
        return walletID;
    }

    @Override
    public Wallet getWallet(int id) throws WalletException{
        for(int i = 0; i < database.size(); i++){
            Wallet temp = database.get(i);
            if(temp.getWalletID() == id) {
                return temp;
            }
        }

        throw new WalletException("Wallet not found");
    }


    @Override //NOTE: do not base database code off of this code segment.
    public void deposit(int walletID, int amount) throws WalletException {
        boolean walletFound = false;
        for(int i = 0; i < database.size(); i++){
            Wallet temp = database.get(i);
            if(temp.getWalletID() == walletID) {
                walletFound = true;
                Wallet newWallet = new Wallet(walletID,  temp.getBalance() + amount);
                database.set(i, newWallet);
            }
        }
        if (!walletFound) {
            throw new WalletException("Wallet not found");
        }
    }

    @Override //again, do not base database code off of these test methods; they are not written to do anything more than test the logic, and are not robust
    public void withdraw(int walletID, int amount) throws WalletException {
        boolean walletFound = false;
        for(int i = 0; i < database.size(); i++){
            Wallet temp = database.get(i);
            if(temp.getWalletID() == walletID) {
                walletFound = true;
                Wallet newWallet = new Wallet(walletID,  temp.getBalance() - amount);
                database.set(i, newWallet);
            }
        }
        if (!walletFound) {
            throw new WalletException("Wallet not found");
        }
    }
}
