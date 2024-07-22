package com.comp3350.webudget;

import com.comp3350.webudget.business.GroupLogicIT;
import com.comp3350.webudget.business.GroupWalletLogicIT;
import com.comp3350.webudget.business.TransactionLogicIT;
import com.comp3350.webudget.business.UserLogicIT;
import com.comp3350.webudget.business.UserWalletLogicIT;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserLogicIT.class,
        UserWalletLogicIT.class,
        GroupLogicIT.class,
        GroupWalletLogicIT.class,
        TransactionLogicIT.class,
})
public class IntegrationTests {
}
