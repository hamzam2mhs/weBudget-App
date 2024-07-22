package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.objects.Wallet;
import com.comp3350.webudget.persistence.IWalletDatabase;
import com.comp3350.webudget.persistence.testDatabases.TestWalletDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WalletLogicTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private IWalletLogic walletLogic;
    private IWalletDatabase mockWalletPersistence;
    private Wallet mockWallet;

    @Before
    public void setUp() {
        mockWalletPersistence = mock(TestWalletDatabase.class);
        mockWallet = mock(Wallet.class);
        walletLogic = new WalletLogic(mockWalletPersistence);
    }

    @Test
    public void testBalance() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getBalance()).thenReturn(0);
        when(mockWallet.getWalletID()).thenReturn(0);
        assertEquals(0, walletLogic.getAmount(mockWallet.getWalletID()));
        verify(mockWallet).getBalance();
        verify(mockWalletPersistence).getWallet(anyInt());
    }

    @Test(expected = WalletException.class)
    public void testNegativeDeposit() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getWalletID()).thenReturn(0);
        Mockito.doThrow(new WalletException("Negative Deposit")).when(mockWalletPersistence).deposit(anyInt(), eq(-100));
        walletLogic.deposit(mockWallet.getWalletID(), -100);
        verify(mockWalletPersistence).deposit(anyInt(), anyInt());
    }

    @Test(expected = WalletException.class)
    public void testZeroDeposit() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getWalletID()).thenReturn(0);
        Mockito.doThrow(new WalletException("Zero Deposit")).when(mockWalletPersistence).deposit(anyInt(), eq(0));
        walletLogic.deposit(mockWallet.getWalletID(), 0);
        verify(mockWalletPersistence).deposit(anyInt(), anyInt());
    }

    @Test(expected = WalletException.class)
    public void testLargeDeposit() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getWalletID()).thenReturn(0);
        Mockito.doThrow(new WalletException("Large Deposit")).when(mockWalletPersistence).deposit(anyInt(), eq(987654321));
        walletLogic.deposit(mockWallet.getWalletID(), 987654321);
        verify(mockWalletPersistence).deposit(anyInt(), anyInt());
    }

    @Test
    public void testGoodDeposit() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getWalletID()).thenReturn(0);
        walletLogic.deposit(mockWallet.getWalletID(), 100);
        verify(mockWalletPersistence).deposit(anyInt(), anyInt());
    }

    @Test(expected = WalletException.class)
    public void testNegativeWithdraw() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getWalletID()).thenReturn(0);
        Mockito.doThrow(new WalletException("Negative Withdraw")).when(mockWalletPersistence).withdraw(anyInt(), eq(-100));
        walletLogic.withdraw(mockWallet.getWalletID(), -100);
        verify(mockWalletPersistence).withdraw(anyInt(), anyInt());
        verify(mockWalletPersistence).getWallet(anyInt());
    }

    @Test(expected = WalletException.class)
    public void testZeroWithdraw() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getWalletID()).thenReturn(0);
        Mockito.doThrow(new WalletException("Zero Withdraw")).when(mockWalletPersistence).withdraw(anyInt(), eq(0));
        walletLogic.withdraw(mockWallet.getWalletID(), 0);
        verify(mockWalletPersistence).withdraw(anyInt(), anyInt());
        verify(mockWalletPersistence).getWallet(anyInt());
    }

    @Test(expected = WalletException.class)
    public void testLargeWithdraw() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getWalletID()).thenReturn(0);
        Mockito.doThrow(new WalletException("Large Withdraw")).when(mockWalletPersistence).withdraw(anyInt(), eq(987654321));
        walletLogic.withdraw(mockWallet.getWalletID(), 987654321);
        verify(mockWalletPersistence).withdraw(anyInt(), anyInt());
        verify(mockWalletPersistence).getWallet(anyInt());
    }

    @Test(expected = WalletException.class)
    public void testOverdraftWithdraw() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getWalletID()).thenReturn(0);
        when(mockWallet.getBalance()).thenReturn(100);
        walletLogic.withdraw(mockWallet.getWalletID(), 150);
        verify(mockWallet).getBalance();
        verify(mockWalletPersistence).withdraw(anyInt(), anyInt());
        verify(mockWalletPersistence).getWallet(anyInt());
    }

    @Test
    public void testGoodWithdraw() throws WalletException {
        when(mockWalletPersistence.getWallet(anyInt())).thenReturn(mockWallet);
        when(mockWallet.getWalletID()).thenReturn(0);
        when(mockWallet.getBalance()).thenReturn(100);
        walletLogic.withdraw(mockWallet.getWalletID(), 50);
        verify(mockWallet).getBalance();
        verify(mockWalletPersistence).withdraw(anyInt(), anyInt());
        verify(mockWalletPersistence).getWallet(anyInt());
    }
}
