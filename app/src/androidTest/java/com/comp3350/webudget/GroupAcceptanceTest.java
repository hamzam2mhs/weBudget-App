package com.comp3350.webudget;


import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.Exceptions.SignupException;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.presentation.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GroupAcceptanceTest {

    private String username,password,groupName;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initValues(){
        username = "dev";
        password = "pass";
        groupName = "testGroup";
        String[] arr = {username,"tester","tester",password};
        try{
            Services.userLogic().signUp(arr);
        }catch( SignupException s ){
            //this exception means that acct alraedy exists, thast fine handle it silently
            System.out.println("Account already exists, continuing with existing account");
        }
        try{
            Services.groupLogic().createEmptyGroup("existingGroup");
        }catch ( GroupException e ){
            System.out.println("group already exists, proceeding with existing group");
        }



    }


    @Test
    public void createGroup(){

        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){

        }

        //signin with account
        Espresso.onView(withId(R.id.username)).perform(replaceText(username));
        Espresso.onView(withId(R.id.password_input)).perform(replaceText(password)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.login_button)).perform(click());

        //navigate to group screen
        Espresso.onView(withId(R.id.navigation_groups)).perform(click());

        //click create group
        Espresso.onView(withId(R.id.group_create_group_button)).perform(click());

        //enter group name and group description
        Espresso.onView(withId(R.id.group_name)).perform(replaceText(groupName)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.group_description)).perform(replaceText("a group made for automated testing")).perform(closeSoftKeyboard());
        //click create group
        Espresso.onView(withId(R.id.create_group_create_group_button)).perform(click());

        //verify the group is created
        Espresso.onData(anything()).inAdapterView(withId(R.id.group_list)).atPosition(0).check(matches(withText(groupName)));


    }

    @Test
    public void joinExistingGroup(){

        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){

        }

        //signin with account
        Espresso.onView(withId(R.id.username)).perform(replaceText(username));
        Espresso.onView(withId(R.id.password_input)).perform(replaceText(password)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.login_button)).perform(click());

        //navigate to group screen
        Espresso.onView(withId(R.id.navigation_groups)).perform(click());

        //enter group ID and join group
        Espresso.onView(withId(R.id.group_name_field)).perform(replaceText("0")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.join_group)).perform(click());



        //verify the group is added
        Espresso.onData(anything()).inAdapterView(withId(R.id.group_list)).atPosition(0).check(matches(withText("existingGroup")));


    }

}
