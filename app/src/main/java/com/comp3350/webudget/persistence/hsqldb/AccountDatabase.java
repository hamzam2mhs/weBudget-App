package com.comp3350.webudget.persistence.hsqldb;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.objects.Account;
import com.comp3350.webudget.persistence.IAccountDatabase;
import com.comp3350.webudget.persistence.IWalletDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;


public class AccountDatabase implements IAccountDatabase {

    private final String dbPath;
    private IWalletDatabase walletDatabase;


    public AccountDatabase(final String dbPath){
        this.dbPath = dbPath;
        this.walletDatabase = Services.walletPersistence();
    }

    public AccountDatabase(final String dbPath, IWalletDatabase injectedWalletDatabase){
        this.dbPath = dbPath;
        walletDatabase = injectedWalletDatabase;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:"+ dbPath +";shutdown=true", "SA", "");
    }

    @Override
    public void insertUser(String username, String fName, String lName, String password) throws AccountException {

        int walletID = walletDatabase.insertWallet(username);
        try(final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement(
                    "insert into account (username,password, fName,lName,walletid) values (?, ?,?,?,?);"
            );

            st.setString(1, username );
            st.setString(2, password );
            st.setString(3, fName);
            st.setString(4, lName );
            st.setInt(5, walletID );
            st.executeUpdate();
            st.close();
        } catch (final SQLException sqlException) {
            throw new AccountException("Account Creation Fail in Database");
        }
    }

    @Override
    public Account getAccount(String username) throws AccountException{
        Account forReturn = null;
        try(final Connection c = connection()){
            final PreparedStatement st = c.prepareStatement(
                    "select * from account where username = ?"
            );
            st.setString(1, username);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()){
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("fName");
                String lastName = resultSet.getString("lName");
                int walletid = resultSet.getInt("walletid");
                forReturn =  new Account(firstName,lastName,userName,password,walletid);
            }
            st.close();
        }
        catch (SQLException sqlException) {
            throw new AccountException("Fail to Get Account in Database");
        }

        return forReturn;
    }

    public ArrayList<Account> getAllAccounts() throws AccountException{
        ArrayList<Account> accounts = new ArrayList<>();
        try(final Connection c = connection()){
            final PreparedStatement st = c.prepareStatement(
                    "select * from account"
            );
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()){
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("fName");
                String lastName = resultSet.getString("lName");
                int walletid = resultSet.getInt("walletid");
                accounts.add(new Account(firstName,lastName,userName,password,walletid));
            }
            st.close();
        }
        catch (SQLException sqlException) {
            throw new AccountException("Cannot Get All Accounts from Database");
        }

        return accounts;
    }

}
