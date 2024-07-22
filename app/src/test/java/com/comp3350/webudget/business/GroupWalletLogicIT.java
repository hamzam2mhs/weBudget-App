package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.persistence.IGroupDatabase;
import com.comp3350.webudget.persistence.IMembershipDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;
import com.comp3350.webudget.persistence.hsqldb.GroupDatabase;
import com.comp3350.webudget.persistence.hsqldb.MembershipDatabase;
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
import static org.junit.Assert.assertTrue;

public class GroupWalletLogicIT {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private IGroupDatabase testGroupDB;
    private IMembershipDatabase testMembershipDB;
    private IWalletDatabase testWalletDB;
    private IGroupWalletLogic groupWalletLogic;

    private File tempDB;

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        testWalletDB = new WalletDatabase(this.tempDB.getAbsolutePath().replace(".script", ""));
        testMembershipDB = new MembershipDatabase(this.tempDB.getAbsolutePath().replace(".script", ""));
        testGroupDB = new GroupDatabase(this.tempDB.getAbsolutePath().replace(".script", ""), testWalletDB, testMembershipDB);
        groupWalletLogic = new GroupWalletLogic(testGroupDB, new WalletLogic(testWalletDB));
    }

    @Test(expected = GroupException.class)
    public void testGroupDNEGet() throws GroupException, WalletException {
        this.groupWalletLogic.getAmount("0");
    }


    //test: balance = 0 on creation
    @Test
    public void testStartBalance() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group");
        assertTrue("Group Id >= 0", id >= 0);
        assertEquals(id, this.groupWalletLogic.getAmount(Integer.toString(id)));
    }

    // int deposit
    //test: depositing -ve amounts invalid
    @Test(expected = WalletException.class)
    public void testNegativeDeposit() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group1");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), -100);
    }

    @Test(expected = WalletException.class)
    public void testZeroDeposit() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group1");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), 0);
    }

    @Test(expected = WalletException.class)
    public void testLargeDeposit() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group1");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), 987654321);
    }

    //test: wallet balance increases after successful deposit
    @Test
    public void testGoodDeposit() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group3");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), 100);
        assertEquals(100, this.groupWalletLogic.getAmount(Integer.toString(id)));
    }

    // string deposit
    //test: depositing -ve amounts invalid
    @Test(expected = WalletException.class)
    public void testNegativeDepositString() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group1");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), "-100");
    }

    @Test(expected = WalletException.class)
    public void testZeroDepositString() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group1");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), "0");
    }

    @Test(expected = WalletException.class)
    public void testLargeDepositString() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group1");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), "987654321");
    }

    //test: wallet balance increases after successful deposit
    @Test
    public void testGoodDepositString() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group3");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), "100");
        assertEquals(100, this.groupWalletLogic.getAmount(Integer.toString(id)));
    }

    //test: withdrawing -ve amounts invalid
    @Test(expected = WalletException.class)
    public void testNegativeWithdraw() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group4");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.withdraw(Integer.toString(id), -100);
    }

    //test: withdrawing more than is in the wallet invalid
    @Test(expected = WalletException.class)
    public void testOverdraftWithdraw() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group5");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), 100);
        this.groupWalletLogic.withdraw(Integer.toString(id), 200);
    }

    @Test(expected = WalletException.class)
    public void testLargeWithdraw() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group5");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), 100);
        this.groupWalletLogic.withdraw(Integer.toString(id), 987654321);
    }

    //test: wallet balance decreases after successful withdraw
    @Test
    public void testGoodWithdraw() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group6");
        assertTrue("Group Id >= 0", id >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), 100);
        this.groupWalletLogic.withdraw(Integer.toString(id), 73);
        assertEquals(27, groupWalletLogic.getAmount(Integer.toString(id)));
    }

    //test: two different groups are separate
    @Test
    public void testSeparateWallets() throws GroupException, WalletException {
        int id = this.testGroupDB.insertGroup("group7");
        int id2 = this.testGroupDB.insertGroup("group8");
        assertTrue("Group Id >= 0", id >= 0);
        assertTrue("Group Id >= 0", id2 >= 0);
        this.groupWalletLogic.deposit(Integer.toString(id), 333);
        this.groupWalletLogic.deposit(Integer.toString(id2), 8301);
        assertEquals(333, groupWalletLogic.getAmount(Integer.toString(id)));
        assertEquals(8301, groupWalletLogic.getAmount(Integer.toString(id2)));
    }

    @After
    public void tearDown() {
        // reset DB
        this.tempDB.delete();
    }

}
