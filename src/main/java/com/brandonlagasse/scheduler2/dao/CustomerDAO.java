package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Customer;
import com.brandonlagasse.scheduler2.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is the DAO for retriving all Customer related data from the database
 */
public class CustomerDAO implements DAOInterface<Customer>{
    /**
     * Retrieves all available Customers
     * @return list of all Customers
     * @throws SQLException database errors
     */
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



        return allCustomers;

    }

    /**
     *Method for inserting customers into the databse
     * @param customer customer to insert
     * @return boolean to identify success/failure
     * @throws SQLException database error handling
     */
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
            return false; //insert failed
        }
        JDBC.closeConnection();
        return true;
    }

    /**
     * This method updates existing customers in the databse
     * @param customer customer to update
     * @return boolean to measure sucess/failure
     * @throws SQLException databse error handling
     */
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
            return false;
        }
        JDBC.closeConnection();
        return true;

    }

//    @Override
//    public boolean delete(int id) throws SQLException {
//        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setInt(1, id);
//        int rowsAffected = ps.executeUpdate();
//
//        if (rowsAffected == 0) {
//            return false; //  failed
//        }
//        return true;
//    }

    /**
     * This method removes an existing customer from the database
     * @param customer the id of the customer to delete
     * @return boolean to determine success
     * @throws SQLException database error catch
     */
    @Override
    public boolean delete(int customer) throws SQLException {

        String appointmentSql = "SELECT Appointment_ID FROM appointments WHERE Customer_ID = ?";
        PreparedStatement appointmentPs = JDBC.connection.prepareStatement(appointmentSql);
        appointmentPs.setInt(1, customer);
        ResultSet rs = appointmentPs.executeQuery();

        ObservableList<Integer> appointmentIds = FXCollections.observableArrayList();
        while (rs.next()) {
            appointmentIds.add(rs.getInt("Appointment_ID"));
        }


        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement deletePs = JDBC.connection.prepareStatement(sql);

        for (int appointmentId : appointmentIds) {
            deletePs.setInt(1, appointmentId);
            deletePs.executeUpdate();
        }

        // deleting the customer
        String deleteCustomerSql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement deleteCustomerPs = JDBC.connection.prepareStatement(deleteCustomerSql);
        deleteCustomerPs.setInt(1, customer);

        int rowsAffected = deleteCustomerPs.executeUpdate();
        return rowsAffected > 0;
    }

    /**
     * This is a helper method that determine if the customer exists in the database
     * @param customerId id of the customer to check
     * @return boolean for success/failure
     * @throws SQLException database errors
     */
    public boolean customerExists(int customerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    /**
     * This function uses an SQL query to search the DB, surround each name in %% make it a query for the setstring, making it a throughough option for this requirement.
     * @param name the name input derived from the text field
     * @return the search results to change the display of the customers table
     * @throws SQLException db errors
     */
    public ObservableList<Customer> searchByName(String name) throws SQLException {


        JDBC.openConnection();

        ObservableList<Customer> searchResults = FXCollections.observableArrayList();
        //This is something I wish I had known earlier. The use of == vs Like. It makes search easier, without being so strict.
        String sql = "SELECT CUSTOMERS.Customer_ID, CUSTOMERS.Customer_Name, CUSTOMERS.Address, CUSTOMERS.Postal_Code, CUSTOMERS.Phone, CUSTOMERS.Division_ID, FIRST_LEVEL_DIVISIONS.Division " +
                "FROM CUSTOMERS " +
                "INNER JOIN FIRST_LEVEL_DIVISIONS ON CUSTOMERS.Division_ID = FIRST_LEVEL_DIVISIONS.Division_ID " +
                "WHERE CUSTOMERS.Customer_Name LIKE ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, "%" + name + "%");
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPostalCode = rs.getString("Postal_Code");
            String customerPhone = rs.getString("Phone");
            int customerDivision = rs.getInt("Division_ID");
            String customerDivisionName = rs.getString("Division");

            Customer customer = new Customer(customerId, customerName, customerAddress, customerPostalCode, customerPhone, customerDivision, customerDivisionName);
            searchResults.add(customer);
        }

        JDBC.closeConnection();
        return searchResults;
    }
}
