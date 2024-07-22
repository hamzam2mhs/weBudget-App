package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.objects.Group;
import com.comp3350.webudget.persistence.IGroupDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;
import com.comp3350.webudget.persistence.testDatabases.TestGroupDatabase;
import com.comp3350.webudget.persistence.testDatabases.TestWalletDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

// tests for GroupWalletLogic using mockito
public class GroupWalletLogicTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private IGroupDatabase testGroupDB;
    private IWalletDatabase testWalletDB;
    private IGroupWalletLogic groupWalletLogic;
    private IWalletLogic mockWalletLogic;

    @Before
    public void setUp() {
        testWalletDB = new TestWalletDatabase();
        testGroupDB = new TestGroupDatabase(testWalletDB);
        mockWalletLogic = mock(WalletLogic.class);
        groupWalletLogic = new GroupWalletLogic(testGroupDB, mockWalletLogic);
    }

    //test: get AccountError if the user searched for does not exist: get, deposit, withdraw, and get transactions
    @Test(expected = GroupException.class)
    public void testGroupDNEGet() throws GroupException, WalletException {
        this.groupWalletLogic.getAmount("423");
        verify(mockWalletLogic).getAmount(423);
    }

    //test: balance = 0 on creation
    @Test
    public void testStartBalance() throws GroupException, WalletException {
        when(mockWalletLogic.getAmount(anyInt())).thenReturn(0);
        int group_id = this.testGroupDB.insertGroup("group");
        int wallet_id = testGroupDB.getGroup(group_id).getWallet();
        assertEquals(0, this.groupWalletLogic.getAmount(Integer.toString(group_id)));
        verify(mockWalletLogic).getAmount(wallet_id);
    }

    @Test
    public void testUpdateBalanceWithDeposit() throws GroupException, WalletException {
        int group_id = this.testGroupDB.insertGroup("group");
        int wallet_id = testGroupDB.getGroup(group_id).getWallet();
        groupWalletLogic.deposit(Integer.toString(group_id), 100);
        groupWalletLogic.getAmount(Integer.toString(group_id));
        verify(mockWalletLogic).deposit(wallet_id, 100);
        verify(mockWalletLogic).getAmount(wallet_id);
    }

    //test: depositing -ve amounts invalid
    @Test(expected = WalletException.class)
    public void testNegativeDeposit() throws GroupException, WalletException {
            Mockito.doThrow(new WalletException("Invalid amount.")).when(mockWalletLogic).deposit(anyInt(), eq(-100));
            int group_id = this.testGroupDB.insertGroup("group");
            int wallet_id = testGroupDB.getGroup(group_id).getWallet();
            groupWalletLogic.deposit(Integer.toString(group_id), -100);
            verify(mockWalletLogic).deposit(wallet_id, -100);
    }

    @Test
    public void testGoodDeposit() throws GroupException, WalletException {
        int group_id = this.testGroupDB.insertGroup("group");
        groupWalletLogic.deposit(Integer.toString(group_id), 100);
        int wallet_id = testGroupDB.getGroup(group_id).getWallet();
        verify(mockWalletLogic).deposit(wallet_id, 100);
    }

    @Test(expected = WalletException.class)
    public void testTooLargeDeposit() throws GroupException, WalletException {
        Mockito.doThrow(new WalletException("Invalid amount.")).when(mockWalletLogic).deposit(anyInt(), eq(987654321));
        int group_id = this.testGroupDB.insertGroup("group");
        groupWalletLogic.deposit(Integer.toString(group_id), 987654321);
        int wallet_id = testGroupDB.getGroup(group_id).getWallet();
        verify(mockWalletLogic).deposit(wallet_id, 987654321);
    }

    @Test(expected = WalletException.class)
    public void testOverdraftWithdraw() throws GroupException, WalletException  {
        Mockito.doThrow(new WalletException("Invalid amount.")).when(mockWalletLogic).withdraw(anyInt(), eq(200));
        int group_id = this.testGroupDB.insertGroup("group");
        groupWalletLogic.deposit(Integer.toString(group_id), 100);
        groupWalletLogic.withdraw(Integer.toString(group_id), 200);
        int wallet_id = testGroupDB.getGroup(group_id).getWallet();
        verify(mockWalletLogic).deposit(wallet_id, 100);
        verify(mockWalletLogic).withdraw(wallet_id, 200);
    }

    @Test(expected = WalletException.class)
    public void testZeroWithdraw() throws GroupException, WalletException {
        Mockito.doThrow(new WalletException("Invalid amount.")).when(mockWalletLogic).withdraw(anyInt(), eq(0));
        int group_id = this.testGroupDB.insertGroup("group");
        groupWalletLogic.withdraw(Integer.toString(group_id), 0);
        int wallet_id = testGroupDB.getGroup(group_id).getWallet();
        verify(mockWalletLogic).withdraw(wallet_id, 0);
    }

    @Test
    public void testGoodWithdraw() throws GroupException, WalletException {
        int group_id = this.testGroupDB.insertGroup("group");
        groupWalletLogic.deposit(Integer.toString(group_id), 100);
        groupWalletLogic.withdraw(Integer.toString(group_id), 100);
        assertEquals(0, this.groupWalletLogic.getAmount(Integer.toString(group_id)));
        int wallet_id = testGroupDB.getGroup(group_id).getWallet();
        verify(mockWalletLogic).deposit(wallet_id, 100);
        verify(mockWalletLogic).withdraw(wallet_id, 100);
        verify(mockWalletLogic).getAmount(wallet_id);
    }

    @Test
    public void testStringDeposit() throws GroupException, WalletException {
        testWalletDB = mock(IWalletDatabase.class);
        testGroupDB = mock(IGroupDatabase.class);
        mockWalletLogic = new WalletLogic(testWalletDB);
        groupWalletLogic = new GroupWalletLogic(testGroupDB, mockWalletLogic);

        Group grp = new Group("group",10,666);
        when(testGroupDB.insertGroup("group")).thenReturn(10);
        when(testGroupDB.getGroup(10)).thenReturn(grp);


        int group_id = this.testGroupDB.insertGroup("group");
        groupWalletLogic.deposit(Integer.toString(group_id), "100");
        verify(testWalletDB).deposit(666, 100);
    }

}
