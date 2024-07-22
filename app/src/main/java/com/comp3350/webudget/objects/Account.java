package com.comp3350.webudget.objects;

import java.util.ArrayList;

public class Account {

    private String fName;
    private String lName;
    private String username;
    private String password;
    private int walletID;

    public Account(String fName, String lName, String username, String password, int walletID){
        this.fName = fName;
        this.lName = lName;
        this.walletID = walletID;
        this.username = username;
        this.password = password;
    }

    public String getFirstName() {
        return this.fName;
    }

    public String getLastName() {
        return this.lName;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public int getWalletID() {
        return this.walletID;
    }

}
