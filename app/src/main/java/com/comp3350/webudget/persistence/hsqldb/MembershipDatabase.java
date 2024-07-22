package com.comp3350.webudget.persistence.hsqldb;


import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.application.Services;

import com.comp3350.webudget.objects.Account;
import com.comp3350.webudget.objects.Group;
import com.comp3350.webudget.persistence.IAccountDatabase;
import com.comp3350.webudget.persistence.IMembershipDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MembershipDatabase implements IMembershipDatabase {

    private final String dbPath;
    private IAccountDatabase accountDatabase;


    public MembershipDatabase(final String dbPath){
        this.dbPath = dbPath;
        this.accountDatabase = Services.accountPersistence();
    }

    public MembershipDatabase(final String dbPath, IAccountDatabase injectedAccountDatabase){
        this.dbPath = dbPath;
        accountDatabase = injectedAccountDatabase;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:"+ dbPath +";shutdown=true", "SA", "");
    }

    @Override
    public Boolean isUserInGroup(String username, int groupID) throws GroupException{
        try(final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement(
                    "select username from membership where groupid=? and username=?;"
            );
            st.setInt(1, groupID);
            st.setString(2,username);
            ResultSet resultSet = st.executeQuery();
            st.close();
            if(resultSet.next()){
                return true;
            }
        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();
            throw new GroupException("Failed to get group information");
        }
        return false;
    }

    @Override
    public void addUserToGroup(String username, int groupID) throws GroupException{
        try(final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement(
                    "insert into membership (username, groupid) values (?, ?);"
            );

            st.setString(1, username );
            st.setInt(2, groupID );
            st.executeUpdate();
            st.close();
        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();
            throw new GroupException("Failed to add user to group");
        }
    }

    @Override
    public void removeUserFromGroup(String username, int groupID)throws GroupException {
        try(final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement(
                    "delete from membership where username=? and groupid=?;"
            );
            st.setString(1, username );
            st.setInt(2, groupID );
            st.executeUpdate();
            st.close();
        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();
            throw new GroupException("Failed to remove user from group");
        }
    }

    @Override
    public ArrayList<Group> getUserGroups(String username) throws  GroupException {
        ArrayList<Group> groups = new ArrayList<>();

        try(final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement(
                    "select groupid, name, walletid from membership natural join groupTable where username=? order by name;"
            );
            st.setString(1, username );
            ResultSet resultSet = st.executeQuery();
            st.close();
            while(resultSet.next()){
                int groupID = resultSet.getInt("groupid");
                String groupName = resultSet.getString("name");
                int walletID = resultSet.getInt("walletID");
                groups.add(new Group(groupName, groupID, walletID));
            }

        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();
            throw new GroupException("Failed to get groups");
        }
        return groups;
    }

    @Override
    public ArrayList<Integer> getUserGroupIDs(String username) throws GroupException{
        ArrayList<Integer> groupIDs = new ArrayList<>();

        try(final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement(
                    "select groupid, name from membership natural join groupTable where username=? order by name;"
            );
            st.setString(1, username );
            ResultSet resultSet = st.executeQuery();
            st.close();
            while(resultSet.next()){
                groupIDs.add(resultSet.getInt("groupid"));
            }

        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();
            throw new GroupException("failed to get group ids");
        }
        return groupIDs;
    }

    @Override
    public ArrayList<Account> getGroupUsers(int groupID) throws AccountException{
        ArrayList<Account> accounts = new ArrayList<>();

        try(final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement(
                    "select username from membership where groupid=?;"
            );
            st.setInt(1, groupID);
            ResultSet resultSet = st.executeQuery();
            st.close();
            while(resultSet.next()){
                accounts.add(accountDatabase.getAccount(resultSet.getString("username")));
            }

        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();
        }catch (final AccountException accountException){
            throw new AccountException("Error in Membership Database When Getting User Account");
        }
        return accounts;
    }
}