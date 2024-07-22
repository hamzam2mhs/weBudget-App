package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.Exceptions.TransactionException;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.objects.Account;
import com.comp3350.webudget.objects.Group;
import com.comp3350.webudget.objects.Transaction;
import com.comp3350.webudget.persistence.IAccountDatabase;
import com.comp3350.webudget.persistence.IGroupDatabase;
import com.comp3350.webudget.persistence.IMembershipDatabase;
import com.comp3350.webudget.persistence.ITransactionDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;
import com.comp3350.webudget.persistence.hsqldb.AccountDatabase;
import com.comp3350.webudget.persistence.hsqldb.GroupDatabase;
import com.comp3350.webudget.persistence.hsqldb.MembershipDatabase;
import com.comp3350.webudget.persistence.hsqldb.TransactionDatabase;
import com.comp3350.webudget.persistence.hsqldb.WalletDatabase;
import com.comp3350.webudget.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TransactionLogicIT {



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

    private File tempDB;

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        testWalletDB = new WalletDatabase(this.tempDB.getAbsolutePath().replace(".script", ""));
        testAccountDB = new AccountDatabase(this.tempDB.getAbsolutePath().replace(".script", ""), testWalletDB);
        testMembershipDB = new MembershipDatabase(this.tempDB.getAbsolutePath().replace(".script", ""), testAccountDB);
        testGroupDB = new GroupDatabase(this.tempDB.getAbsolutePath().replace(".script", ""), testWalletDB, testMembershipDB);
        testTransactionDB = new TransactionDatabase(this.tempDB.getAbsolutePath().replace(".script", ""));
        IUserLogic userLogic = new UserLogic(testAccountDB);

        testTransactionLogic = new TransactionLogic(testWalletDB, testAccountDB, testGroupDB, testTransactionDB, userLogic);
    }

    @Test(expected = AccountException.class)
    public void invalidAccountTransaction() throws AccountException, GroupException, WalletException, TransactionException {
        testTransactionLogic.groupToUserTransaction("000000","doesntExist","100");
    }

    @Test(expected = GroupException.class)
    public void invalidGroupTransaction() throws AccountException, GroupException, WalletException, TransactionException {
        testAccountDB.insertUser("dev","developer","software","securepwrd");
        testTransactionLogic.groupToUserTransaction("000000","dev","100");
    }

    @Test
    public void successfulUserToGroupTransaction() throws AccountException, GroupException, WalletException, TransactionException{
        testAccountDB.insertUser("dev","developer","software","securepwrd");
        Account me = testAccountDB.getAccount("dev");
        testWalletDB.deposit(me.getWalletID(),100);
        String groupID = String.valueOf(testGroupDB.insertGroup("devgru"));
        testTransactionLogic.userToGroupTransaction("dev",groupID,"100");
        ArrayList<Transaction> transactions  = testTransactionLogic.getTransactionsOut("dev");
        assertEquals(100,transactions.get(0).getAmount(),0);
        assertEquals(0,testWalletDB.getWallet(me.getWalletID()).getBalance(),0);
    }

    @Test
    public void successfulGroupToUserTransaction() throws AccountException, GroupException, WalletException, TransactionException{
        testAccountDB.insertUser("dev","developer","software","securepwrd");
        int gID = testGroupDB.insertGroup("devgru");
        Group g = testGroupDB.getGroup(gID);
        testWalletDB.deposit(g.getWallet(),100);
        String groupID = String.valueOf(gID);
        testTransactionLogic.groupToUserTransaction(groupID,"dev","100");
        ArrayList<Transaction> transactions  = testTransactionLogic.getTransactionsIn("dev");
        assertEquals(100,transactions.get(0).getAmount(),0);
        assertEquals(0,testWalletDB.getWallet(g.getWallet()).getBalance(),0);
    }

    @Test
    public void successfulUserToUserTransaction() throws AccountException, GroupException, WalletException, TransactionException{
        testAccountDB.insertUser("dev","developer","software","securepwrd");
        testAccountDB.insertUser("robin_hood","Rob","Guderian","secure");
        Account dev = testAccountDB.getAccount("dev");
        testWalletDB.deposit(dev.getWalletID(),100);
        testTransactionLogic.userToUserTransaction("dev","robin_hood","100");
        ArrayList<Transaction> transactions  = testTransactionLogic.getTransactionsOut("dev");
        assertEquals(100,transactions.get(0).getAmount(),0);
        assertEquals(0,testWalletDB.getWallet(dev.getWalletID()).getBalance(),0);
    }

    @After
    public void tearDown() {
        // reset DB
        this.tempDB.delete();
    }
}
