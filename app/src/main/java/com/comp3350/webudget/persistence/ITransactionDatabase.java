package com.comp3350.webudget.persistence;

import com.comp3350.webudget.Exceptions.TransactionException;
import com.comp3350.webudget.objects.Transaction;

import java.util.ArrayList;

public interface ITransactionDatabase {
    public void insertTransaction(int fromWalletid, int toWalletid, int amount) throws TransactionException;
    public ArrayList<Transaction> getInputTransaction(int walletID) throws TransactionException;
    public ArrayList<Transaction> getOutputTransaction(int walletID) throws TransactionException;
}
