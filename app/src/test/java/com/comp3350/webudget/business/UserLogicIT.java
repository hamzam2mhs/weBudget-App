package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.SignupException;
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

import javax.security.auth.login.LoginException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserLogicIT {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private IAccountDatabase testAccountDB;
    private IWalletDatabase testWalletDB;
    private IUserLogic testUserLogic;

    private final String[] user1Input = {"user1", "xx", "xx", "password1"};
    private final String[] user2Input = {"user2", "yy", "yy", "password2"};

    private File tempDB;

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        testWalletDB = new WalletDatabase(this.tempDB.getAbsolutePath().replace(".script", ""));
        testAccountDB = new AccountDatabase(this.tempDB.getAbsolutePath().replace(".script", ""), testWalletDB);
        testUserLogic = new UserLogic(testAccountDB);

    }

    @Test
    public void signUpMultiple() throws SignupException{
        testUserLogic.signUp(user1Input);
        testUserLogic.signUp(user2Input);
    }

    @Test(expected = LoginException.class)
    public void testNoUserLogin() throws LoginException{
        String[] info = {"user1","password1"};
        testUserLogic.login(info);
    }

    @Test
    public void test_valid_login() throws SignupException, LoginException {
        String[] info = {"user1","password1"};
        testUserLogic.signUp(user1Input);
        testUserLogic.login(info);
        assertEquals("user1",testUserLogic.getCurrentUser());
    }


    @Test(expected = LoginException.class)
    public void test_invalid_login() throws SignupException, LoginException { //neither username or password or ok
        String[] info = {"non_user","fake_password"};
        testUserLogic.signUp(user1Input);
        testUserLogic.login(info);
    }

    @Test(expected = LoginException.class)
    public void test_invalid_username() throws SignupException, LoginException { //password is ok, but username isn't
        String[] info = {"non_user","password1"};
        testUserLogic.signUp(user1Input);
        testUserLogic.login(info);
    }

    @Test(expected = LoginException.class)
    public void test_invalid_password() throws SignupException, LoginException { //username is ok, but has the wrong password
        String[] info = {"user1","wrong_password"};
        testUserLogic.signUp(user1Input);
        testUserLogic.login(info);
    }

    @Test(expected = LoginException.class)
    public void test_user_pass_mismatch() throws SignupException, LoginException { //both username and password are in the system, but belong to different accounts
        String[] info = {"user1","password2"};
        testUserLogic.signUp(user1Input);
        testUserLogic.login(info);
    }

    @Test
    public void test_logout() throws SignupException, LoginException{
        String[] info = {"user1","password1"};
        testUserLogic.signUp(user1Input);
        testUserLogic.login(info);
        assertEquals("user1",testUserLogic.getCurrentUser());
        testUserLogic.logout();
        assertNull(testUserLogic.getCurrentUser());
    }

    @After
    public void tearDown() {
        // reset DB
        this.tempDB.delete();
    }

}