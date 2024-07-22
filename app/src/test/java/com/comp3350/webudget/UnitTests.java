package com.comp3350.webudget;

import com.comp3350.webudget.business.GroupLogicTest;

import com.comp3350.webudget.business.GroupWalletLogicTest;
import com.comp3350.webudget.business.TransactionLogicTest;
import com.comp3350.webudget.business.UserLogicTest;
import com.comp3350.webudget.business.UserWalletLogicTest;
import com.comp3350.webudget.business.WalletLogicTest;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(Suite.class)
@Suite.SuiteClasses({
        GroupLogicTest.class,
        UserLogicTest.class,

        WalletLogicTest.class,
        GroupWalletLogicTest.class,
        UserWalletLogicTest.class,

        TransactionLogicTest.class,
})
public class UnitTests
{

}
