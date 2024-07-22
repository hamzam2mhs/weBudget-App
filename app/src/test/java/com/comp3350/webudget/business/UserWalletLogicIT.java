package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.persistence.IAccountDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;
import com.comp3350.webudget.persistence.hsqldb.AccountDatabase;
import com.comp3350.webudget.persistence.hsqldb.WalletDatabase;
import com.comp3350.webudget.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UserWalletLogicIT {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private IAccountDatabase testAccountDB;
    private IWalletDatabase testWalletDB;
    private IUserWalletLogic userWalletLogic;

    private File tempDB;

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        testWalletDB = new WalletDatabase(this.tempDB.getAbsolutePath().replace(".script", ""));
        testAccountDB = new AccountDatabase(this.tempDB.getAbsolutePath().replace(".script", ""), testWalletDB);
        userWalletLogic = new UserWalletLogic(testAccountDB, new WalletLogic(testWalletDB));

    }

    //test: get AccountError if the user searched for does not exist: get, deposit, withdraw, and get transactions
    @Test(expected = AccountException.class)
    public void testAccountDNEGet() throws AccountException, WalletException {
        this.userWalletLogic.getAmount("admin");
    }


    //test: balance = 0 on creation
    @Test
    public void testStartBalance() throws AccountException, WalletException {
        this.testAccountDB.insertUser("user1","u","u","admin");
        assertEquals(0, this.userWalletLogic.getAmount("user1"));
    }

    //test: depositing -ve amounts invalid
    @Test(expected = WalletException.class)
    public void testNegativeDeposit() throws AccountException, WalletException {
        this.testAccountDB.insertUser("user2","u","u","admin");
        this.userWalletLogic.deposit("user2", -100);
    }

    //test: wallet balance increases after successful deposit
    @Test
    public void testGoodDeposit() throws AccountException, WalletException {
        this.testAccountDB.insertUser("user3","u","u","admin");
        this.userWalletLogic.deposit("user3", 100);
        assertEquals(100, this.userWalletLogic.getAmount("user3"));
    }

    //test: wallet balance increases after successful String deposit
    @Test
    public void testGoodStringDeposit() throws AccountException, WalletException {
        this.testAccountDB.insertUser("user3","u","u","admin");
        this.userWalletLogic.deposit("user3", "100");
        assertEquals(100, this.userWalletLogic.getAmount("user3"));
    }

    //test: withdrawing -ve amounts invalid
    @Test(expected = WalletException.class)
    public void testNegativeWithdraw() throws AccountException, WalletException {
        this.testAccountDB.insertUser("user4","u","u","admin");
        this.userWalletLogic.withdraw("user4", -100);
    }

    //test: withdrawing more than is in the account invalid
    @Test(expected = WalletException.class)
    public void testOverdraftWithdraw() throws AccountException, WalletException {
        this.testAccountDB.insertUser("user5","u","u","admin");
        this.userWalletLogic.deposit("user5", 100);
        this.userWalletLogic.withdraw("user5", 200);
    }

    //test: wallet balance decreases after successful withdraw
    @Test
    public void testGoodWithdraw() throws AccountException, WalletException {
        this.testAccountDB.insertUser("user6","u","u","admin");
        this.userWalletLogic.deposit("user6", 100);
        this.userWalletLogic.withdraw("user6", 73);
        assertEquals(27,userWalletLogic.getAmount("user6"));
    }

    //test: two different users accounts are separate
    @Test
    public void testSeparateWallets() throws AccountException, WalletException {
        this.testAccountDB.insertUser("user7","u","u","admin");
        this.testAccountDB.insertUser("user8","u","u","admin");
        this.userWalletLogic.deposit("user7", 333);
        this.userWalletLogic.deposit("user8", 8301);
        assertEquals(333,userWalletLogic.getAmount("user7"));
        assertEquals(8301,userWalletLogic.getAmount("user8"));
    }


    @After
    public void tearDown() {
        // reset DB
        this.tempDB.delete();
    }

}
