package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.Exceptions.TransactionException;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.objects.Account;
import com.comp3350.webudget.objects.Group;
import com.comp3350.webudget.objects.Transaction;
import com.comp3350.webudget.objects.Wallet;
import com.comp3350.webudget.persistence.IAccountDatabase;
import com.comp3350.webudget.persistence.IGroupDatabase;
import com.comp3350.webudget.persistence.IMembershipDatabase;
import com.comp3350.webudget.persistence.ITransactionDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;
import com.comp3350.webudget.persistence.testDatabases.TestAccountDatabase;
import com.comp3350.webudget.persistence.testDatabases.TestGroupDatabase;
import com.comp3350.webudget.persistence.testDatabases.TestMembershipDatabase;
import com.comp3350.webudget.persistence.testDatabases.TestTransactionDatabase;
import com.comp3350.webudget.persistence.testDatabases.TestWalletDatabase;


import org.checkerframework.checker.units.qual.A;
import org.hsqldb.rights.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionLogicTest {


    @Rule
    public ExpectedException exception = ExpectedException.none();

    private IWalletDatabase testWalletDB;
    private IAccountDatabase testAccountDB;
    private IGroupDatabase testGroupDB;
    private IMembershipDatabase testMembershipDB;
    private ITransactionLogic testTransactionLogic;
    private ITransactionDatabase testTransactionDB;
    private IUserLogic userLogic;
    private IGroupLogic groupLogic;
    private IUserWalletLogic walletLogic;

    private Account mockAccount;
    private Group mockGroup;
    @Before
    public void setUp(){
        testWalletDB = mock(IWalletDatabase.class);
        testAccountDB = mock(IAccountDatabase.class);
        testGroupDB = mock(IGroupDatabase.class);
        testTransactionDB = mock(ITransactionDatabase.class);
        userLogic = mock(IUserLogic.class);
        groupLogic = mock(IGroupLogic.class);
        walletLogic = mock(IUserWalletLogic.class);

        testTransactionLogic = new TransactionLogic(testWalletDB,testAccountDB, testGroupDB, testTransactionDB ,userLogic);

        mockAccount = mock(Account.class);
        mockGroup = mock(Group.class);
    }

    @Test(expected = AccountException.class)
    public void invalidAccountTransaction() throws AccountException, GroupException, WalletException, TransactionException {
        when(testAccountDB.getAccount("doesntExist")).thenReturn(null);
        testTransactionLogic.groupToUserTransaction("000000","doesntExist","100");
        verify(testAccountDB).getAccount("doesntExist");
    }


    @Test(expected = GroupException.class)
    public void invalidGroupTransaction() throws AccountException, GroupException, WalletException, TransactionException {
        when(testAccountDB.getAccount("dev")).thenReturn(mockAccount);
        when(testGroupDB.getGroup(0)).thenReturn(null);
        testTransactionLogic.groupToUserTransaction("000000","dev","100");
    }


    @Test
    public void successfulUserToGroupTransaction() throws AccountException, GroupException, WalletException, TransactionException{
        Account user = new Account("developer", "software","dev","securepwrd",1);
        Group group = new Group("devgru",0,2);

        Wallet userWallet = new Wallet(1,100);
        Wallet groupWallet = new Wallet(2, 0);

        when(testAccountDB.getAccount("dev")).thenReturn(user);
        when(testGroupDB.getGroup(0)).thenReturn(group);
        when(testWalletDB.getWallet(1)).thenReturn(userWallet);
        when(testWalletDB.getWallet(2)).thenReturn((groupWallet));

        testTransactionLogic.userToGroupTransaction("dev","0","100");

        verify(testWalletDB).deposit(2,100);
        verify(testWalletDB).withdraw(1,100);
        verify(testTransactionDB).insertTransaction(1,2,100);
    }

    @Test
    public void successfulGroupToUserTransaction() throws AccountException, GroupException, WalletException, TransactionException{
        Account user = new Account("developer", "software","dev","securepwrd",1);
        Group group = new Group("devgru",0,2);

        Wallet userWallet = new Wallet(1,0);
        Wallet groupWallet = new Wallet(2, 100);

        when(testAccountDB.getAccount("dev")).thenReturn(user);
        when(testGroupDB.getGroup(0)).thenReturn(group);
        when(testWalletDB.getWallet(1)).thenReturn(userWallet);
        when(testWalletDB.getWallet(2)).thenReturn((groupWallet));

        testTransactionLogic.groupToUserTransaction("0","dev","100");

        verify(testWalletDB).deposit(1,100);
        verify(testWalletDB).withdraw(2,100);
        verify(testTransactionDB).insertTransaction(2,1,100);
    }

    @Test
    public void successfulUserToUserTransaction() throws AccountException, GroupException, WalletException, TransactionException{
        Account user = new Account("developer", "software","dev","securepwrd",1);
        Account user2 = new Account("Guderian","Rob","robin_hood","secure",2);

        Wallet userWallet = new Wallet(1,100);
        Wallet user2Wallet = new Wallet(2, 1);

        when(testAccountDB.getAccount("dev")).thenReturn(user);
        when(testAccountDB.getAccount("robin_hood")).thenReturn(user2);
        when(testWalletDB.getWallet(1)).thenReturn(userWallet);
        when(testWalletDB.getWallet(2)).thenReturn(user2Wallet);

        testTransactionLogic.userToUserTransaction("dev","robin_hood","100");

        verify(testWalletDB).deposit(2,100);
        verify(testWalletDB).withdraw(1,100);
        verify(testTransactionDB).insertTransaction(1,2,100);
    }

    @Test
    public void successfulGetInTransaction() throws AccountException, GroupException, WalletException, TransactionException{
        Account user = new Account("developer", "software","dev","securepwrd",1);

        when(testAccountDB.getAccount("dev")).thenReturn(user);


        testTransactionLogic.getTransactionsIn("dev");

        verify(testAccountDB).getAccount("dev");
        verify(testTransactionDB).getInputTransaction(1);
    }

    @Test
    public void successfulGetOutTransaction() throws AccountException, GroupException, WalletException, TransactionException{
        Account user = new Account("developer", "software","dev","securepwrd",1);

        when(testAccountDB.getAccount("dev")).thenReturn(user);


        testTransactionLogic.getTransactionsOut("dev");

        verify(testAccountDB).getAccount("dev");
        verify(testTransactionDB).getOutputTransaction(1);
    }


}
