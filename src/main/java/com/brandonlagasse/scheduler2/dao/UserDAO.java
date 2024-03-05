package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements DAOInterface<User>{

    @Override
    public ObservableList<User> getList() throws SQLException {

        JDBC.openConnection();

        ObservableList<User> allUsers = FXCollections.observableArrayList();

        String sql = "SELECT * FROM users";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        System.out.println("connection working");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            User user = new User(userId,userName,password);
            allUsers.add(user);}
        JDBC.closeConnection();
        return allUsers; // Returns the observable list

    }

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
            return false; // Insert failed
        }
        JDBC.closeConnection();
        return true;
    }

    @Override
    public boolean update(User user) throws SQLException {
        JDBC.openConnection();
        String sql = "UPDATE users SET User_Name = ?, Password = ? WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false; // Update failed
        }
        JDBC.closeConnection();
        return true;

    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false; //  failed
        }
        return true;
    }

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
}
