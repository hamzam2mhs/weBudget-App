package com.comp3350.webudget.objects;

public class Wallet {

    private int walletID;
    private int balance;

    public Wallet(int walletID, int balance){
        this.walletID = walletID;
        this.balance = balance;
    }

    public int getWalletID(){
        return this.walletID;
    }


    public int getBalance(){
        return this.balance;
    }

    public String toString(){
        return " wallet contains "+balance;
    }

}
