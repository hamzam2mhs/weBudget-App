package com.comp3350.webudget;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.SignupException;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.presentation.LoginActivity;
import com.comp3350.webudget.presentation.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginAcceptanceTest {

    private String username,password,inputValue,currentBal;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initValues(){
        username = "dev";
        password = "pass";
        inputValue = "1024";

        String[] arr = {username,"tester","tester",password};
        try{
            Services.userLogic().signUp(arr);
        }catch( SignupException s ){
            //this exception means that acct alraedy exists, thast fine handle it silently
            System.out.println("Account already exists, continuing with existing account");
        }

    }

    @Test
    public void validLoginTest(){

        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){

        }
        //signin with account
        Espresso.onView(withId(R.id.username)).perform(replaceText(username));
        Espresso.onView(withId(R.id.password_input)).perform(replaceText(password)).perform(closeSoftKeyboard());
        try{
            Thread.sleep(250);
        }catch(InterruptedException e){

        }
        Espresso.onView(withId(R.id.login_button)).perform(click());
        //assert something
    }

}
