package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Customer;
import com.brandonlagasse.scheduler2.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO implements DAOInterface<Customer>{

    @Override
    public ObservableList<Customer> getList() throws SQLException {
        JDBC.openConnection();

        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

        String sql = "SELECT CUSTOMERS.Customer_ID, CUSTOMERS.Customer_Name, CUSTOMERS.Address, CUSTOMERS.Postal_Code, CUSTOMERS.Phone, CUSTOMERS.Division_ID, FIRST_LEVEL_DIVISIONS.Division \n" +
                "from CUSTOMERS \n" +
                "INNER JOIN  FIRST_LEVEL_DIVISIONS \n" +
                "ON CUSTOMERS.Division_ID = FIRST_LEVEL_DIVISIONS.Division_ID";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPostalCode = rs.getString("Postal_Code");
            String customerPhone = rs.getString("Phone");
            int customerDivision = rs.getInt("Division_ID");
            String customerDivisionName = rs.getString("Division");


            Customer customer = new Customer(customerId,customerName,customerAddress,customerPostalCode,customerPhone,customerDivision,customerDivisionName);
            allCustomers.add(customer);
        }



        return allCustomers; // Returns the observable list

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
    public boolean update(Customer customer) throws SQLException {
        JDBC.openConnection();
        String sql = "UPDATE customers SET Customer_ID = ?, Customer_name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,customer.getId());
        ps.setString(2,customer.getName());
        ps.setString(3,customer.getAddress());

        ps.setString(4, customer.getPostalCode());
        ps.setString(5, customer.getPhone());
        ps.setInt(6, customer.getDivisionId());
        ps.setInt(7,customer.getId());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false; // Update failed
        }
        JDBC.closeConnection();
        return true;

    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false; //  failed
        }
        return true;
    }


}
