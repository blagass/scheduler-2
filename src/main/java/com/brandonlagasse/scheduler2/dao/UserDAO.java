package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is a DAO for retriving User data from the database
 */
public class UserDAO implements DAOInterface<User>{
    /**
     * Pulls all available Users from the database
     * @return list of all users
     * @throws SQLException database error catch
     */
    @Override
    public ObservableList<User> getList() throws SQLException {

        JDBC.openConnection();

        ObservableList<User> allUsers = FXCollections.observableArrayList();

        String sql = "SELECT * FROM users";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        //System.out.println("connection working");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            User user = new User(userId,userName,password);
            allUsers.add(user);}
        JDBC.closeConnection();
        return allUsers;

    }

    /**
     * Inserts a User into the database
     * @param user object to insert
     * @return boolean for success
     * @throws SQLException
     */
    @Override
    public boolean insert(User user) throws SQLException {
        JDBC.openConnection();
        String sql = "INSERT INTO USERS(User_ID, User_Name, Password) VALUES(?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false;
        }
        JDBC.closeConnection();
        return true;
    }

    /**
     * Updates a User in the database
     * @param user User to update
     * @return boolean for success
     * @throws SQLException for database errors
     */
    @Override
    public boolean update(User user) throws SQLException {
        JDBC.openConnection();
        String sql = "UPDATE users SET User_Name = ?, Password = ? WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1,user.getName());
        ps.setString(2,user.getPassword());
        ps.setInt(3,user.getId());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false;
        }
        JDBC.closeConnection();
        return true;

    }

    /**
     * Removes selected User from the DB
     * @param id the id of the User to delete
     * @return boolean showing success or failure
     * @throws SQLException for database error handling
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false;
        }
        return true;
    }

    /**
     * Method to retrieve a specific User by their ID
     * @param id id of the User to retrieve
     * @return returns User that matches the ID
     * @throws SQLException
     */
    @Override
    public User getById(int id) throws SQLException {
        User returnedUser = null;
        String sql = "SELECT * FROM users WHERE User_ID =?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String userPassword = rs.getString("Password");
            User user = new User(userId,userName,userPassword);
            returnedUser = user;
        }
        
        return returnedUser;

    }

    /**
     * This boolean method checks to make sure a user exits
     * @param userId the id to use to see if they exist
     * @return boolean for true/false
     * @throws SQLException database error handling
     */
    public boolean userExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }
}
