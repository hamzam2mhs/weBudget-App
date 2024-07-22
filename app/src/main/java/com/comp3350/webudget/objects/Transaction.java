package com.comp3350.webudget.objects;

public class Transaction {

    private int id;
    private int fromWalletid;
    private int toWalletid;
    private int amount;
    private String date;


    public Transaction(int id, int fromWalletid, int toWalletid, int amount, String date){
        this.id = id;
        this.amount = amount;
        this.fromWalletid = fromWalletid;
        this.toWalletid = toWalletid;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getFromWalletid() {
        return fromWalletid;
    }

    public int getToWalletid() {
        return toWalletid;
    }

    public String getDate() {
        return date;
    }

    public String toString(){
        return "$"+this.amount;
    }

}
