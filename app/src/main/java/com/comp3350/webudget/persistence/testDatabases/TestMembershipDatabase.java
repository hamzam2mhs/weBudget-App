package com.comp3350.webudget.persistence.testDatabases;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.objects.Account;
import com.comp3350.webudget.objects.Group;
import com.comp3350.webudget.persistence.IAccountDatabase;
import com.comp3350.webudget.persistence.IGroupDatabase;
import com.comp3350.webudget.persistence.IMembershipDatabase;

import java.util.ArrayList;

public class TestMembershipDatabase implements IMembershipDatabase {

    IAccountDatabase accountDatabase = null;
    IGroupDatabase groupDatabase = null;

    ArrayList<Integer> groupIDColumn;
    ArrayList<String> usernameColumn;

    public TestMembershipDatabase(){
        accountDatabase = Services.accountPersistence();
        groupDatabase = Services.groupPersistence();
        groupIDColumn = new ArrayList<>();
        usernameColumn = new ArrayList<>();
    }

    public TestMembershipDatabase(IAccountDatabase injectedAccountDatabase, IGroupDatabase injectedGroupDatabase){
        accountDatabase = injectedAccountDatabase;
        groupDatabase = injectedGroupDatabase;
        groupIDColumn = new ArrayList<>();
        usernameColumn = new ArrayList<>();
    }

    public Boolean isUserInGroup(String username, int groupID) throws AccountException, GroupException {
        Group group = groupDatabase.getGroup(groupID);

        //test database only really needs to test one relationship. Real database would ideally only need to check that the single row is in the membershipDB
        ArrayList<String> members = getGroupMembers(groupID);
        return (members.indexOf(username) != -1);
    }

    @Override
    public void addUserToGroup(String username, int groupID)  throws AccountException, GroupException {
        Account user = accountDatabase.getAccount(username);
        Group group = groupDatabase.getGroup(groupID);
        //here, we can do it by a simple change of the object. In the real database, we need to access the Membership table.
        groupIDColumn.add(groupID);
        usernameColumn.add(username);
    }

    @Override
    public void removeUserFromGroup(String username, int groupID)  throws AccountException, GroupException{
        for(int i = 0; i < groupIDColumn.size(); i++){
            if (groupIDColumn.get(i) == groupID && usernameColumn.get(i).equals(username)){
                groupIDColumn.remove(i);
                usernameColumn.remove(i);
                break;
            }
        }
    }

    @Override
    public ArrayList<Group> getUserGroups(String username)  throws AccountException, GroupException{

        ArrayList<Group> userGroups = new ArrayList<>();
        ArrayList<Integer> userGroupIDs = getUserGroupIDs(username);

        for(int i = 0; i < userGroupIDs.size(); i++){
            userGroups.add(groupDatabase.getGroup(userGroupIDs.get(i)));
        }

        return userGroups;
    }

    @Override
    public ArrayList<Account> getGroupUsers(int groupID)  throws AccountException, GroupException{

        ArrayList<Account> groupUsers = new ArrayList<>();
        ArrayList<String> memberNames = getGroupMembers(groupID);

        for(int i = 0; i < memberNames.size(); i++){
            groupUsers.add(accountDatabase.getAccount((memberNames.get(i))));
        }

        return groupUsers;
    }

    @Override
    public ArrayList<Integer> getUserGroupIDs(String username) throws GroupException {
        ArrayList<Integer> groups = new ArrayList<>();
        for(int i = 0; i < usernameColumn.size(); i++){
            if (usernameColumn.get(i).equals(username)){
                groups.add(groupIDColumn.get(i));
            }
        }
        return groups;
    }

    private ArrayList<String> getGroupMembers(int groupID){
        ArrayList<String> members = new ArrayList<>();
        for(int i = 0; i < groupIDColumn.size(); i++){
            if (groupIDColumn.get(i) == groupID){
                members.add(usernameColumn.get(i));
            }
        }
        return members;
    }
}
