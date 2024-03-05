package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Customer;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDAO implements DAOInterface<Customer>{

    @Override
    public ObservableList<Customer> getList() {
        return null;
    }

    @Override
    public boolean insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS(Customer_Name,Address,Postal_Code, Phone, Division_ID) VALUES(?,?,?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,customer.getName());
        ps.setString(2,customer.getAddress());
        ps.setString(3,customer.getPostalCode());
        ps.setString(4,customer.getPhone());
        ps.setInt(5,customer.getDivisionId());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false; // Insert failed
        }
        JDBC.closeConnection();
        return true;
    }

    @Override
    public boolean update(Customer object) {
        JDBC.openConnection();
        String sql = "UPDATE customers SET User_Name = ?, Password = ? WHERE User_ID = ?";
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
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Customer getById(int id) {
        return null;
    }
}
