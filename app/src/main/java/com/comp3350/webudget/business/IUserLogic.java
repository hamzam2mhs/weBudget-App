package com.comp3350.webudget.business;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.SignupException;
import com.comp3350.webudget.objects.Account;

import javax.security.auth.login.LoginException;

public interface IUserLogic {
    void signUp(String[] info) throws SignupException;
    void login(String[] info) throws LoginException;
    void logout();
    String getCurrentUser();
    Account getAccount(String username) throws AccountException;
}
