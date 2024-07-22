package com.comp3350.webudget.persistence.hsqldb;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.objects.Account;
import com.comp3350.webudget.objects.Group;
import com.comp3350.webudget.persistence.IAccountDatabase;
import com.comp3350.webudget.persistence.IGroupDatabase;
import com.comp3350.webudget.persistence.IMembershipDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GroupDatabase implements IGroupDatabase {

    private final String dbPath;
    private IWalletDatabase walletDatabase;
    private IMembershipDatabase membershipDatabase;

    public GroupDatabase(final String dbPath){
        this.dbPath = dbPath;
        this.walletDatabase = Services.walletPersistence();
        membershipDatabase = Services.membershipPersistence();
    }

    public GroupDatabase(final String dbPath, IWalletDatabase injectedWalletDatabase, IMembershipDatabase injectedMembershipDatabase){
        this.dbPath = dbPath;
        walletDatabase = injectedWalletDatabase;
        membershipDatabase = injectedMembershipDatabase;
    }


    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:"+ dbPath +";shutdown=true", "SA", "");
    }

    @Override
    public int insertGroup(String groupName) throws GroupException {
        int walletID = walletDatabase.insertWallet(groupName);
        int groupID = -1;
        try(final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement(
                    "insert into groupTable (name,walletid) values (?, ?);"
            );

            st.setString(1, groupName );
            st.setInt(2, walletID );
            st.executeUpdate();
            st.close();

            final PreparedStatement st2 = c.prepareStatement(
                    "select MAX(groupid) as maxID from groupTable;"
            );
            ResultSet resultSet = st2.executeQuery();
            if (resultSet.next()){
                groupID = resultSet.getInt("maxID");
            }
            st2.close();


        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();
            throw new GroupException("Failed to insert group");
        }

        return groupID;
    }

    @Override
    public Group getGroup(int id) throws GroupException {
        try(final Connection c = connection()){
            final PreparedStatement st = c.prepareStatement(
                    "select * from groupTable where groupid = ? order by name;"
            );
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()){
                String groupName = resultSet.getString("name");
                int walletid = resultSet.getInt("walletid");
                return new Group(groupName,id,walletid);
            }
            st.close();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new GroupException("Failed to get group");
        }
        return null;
    }

    @Override
    public ArrayList<Group> getAllGroups() throws GroupException {
        ArrayList<Group> groups = new ArrayList<>();

        try(final Connection c = connection()){
            final PreparedStatement st = c.prepareStatement(
                    "select * from groupTable order by name;"
            );
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()){
                int groupID = resultSet.getInt("groupid");
                String groupName = resultSet.getString("name");
                int walletid = resultSet.getInt("walletid");
                groups.add(new Group(groupName,groupID,walletid));
            }
            st.close();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new GroupException("Failed to get groups");
        }

        return groups;
    }

    @Override
    public ArrayList<Group> getGroups(String username) throws GroupException {
        ArrayList<Integer> groupIDs = membershipDatabase.getUserGroupIDs(username);
        ArrayList<Group> groups = new ArrayList<>();
        for(int i = 0; i < groupIDs.size(); i++){
            try(final Connection c = connection()){
                final PreparedStatement st = c.prepareStatement(
                        "select * from groupTable where groupid = ?"
                );
                st.setInt(1, groupIDs.get(i));
                ResultSet resultSet = st.executeQuery();
                while (resultSet.next()){
                    String groupName = resultSet.getString("name");
                    int walletid = resultSet.getInt("walletid");
                    groups.add(new Group(groupName,groupIDs.get(i),walletid));
                }
                st.close();
            }
            catch (SQLException sqlException) {
                sqlException.printStackTrace();
                throw new GroupException("Failed to get user groups");
            }
        }
        return groups;
    }

}
