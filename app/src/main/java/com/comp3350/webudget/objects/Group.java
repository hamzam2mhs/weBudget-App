package com.comp3350.webudget.objects;
import java.util.ArrayList;
public class Group {


    private String name;
    private int id;
    private int walletID;

    public Group(String name, int id, int walletID){
        this.name = name;
        this.id = id;
        this.walletID = walletID;
    }

    public String getName(){
        return this.name;
    }


    public int getId(){
        return this.id;
    }

    public int getWallet(){
        return this.walletID;
    }

    public String toString(){
        return this.name;
    }
}