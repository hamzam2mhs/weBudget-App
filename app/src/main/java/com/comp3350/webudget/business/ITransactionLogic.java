package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.Exceptions.TransactionException;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.objects.Transaction;

import java.util.ArrayList;

public interface ITransactionLogic {

    void userToGroupTransaction( String username, String group, String amount ) throws WalletException, AccountException, GroupException, TransactionException;
    void groupToUserTransaction( String group, String username, String amount ) throws WalletException, AccountException, GroupException, TransactionException;
    void userToUserTransaction( String sender, String receiver, String amount ) throws WalletException, AccountException, TransactionException;
    ArrayList<Transaction> getTransactionsIn( String username ) throws WalletException, AccountException, TransactionException;
    ArrayList<Transaction> getTransactionsOut( String username ) throws WalletException, AccountException, TransactionException;

}
