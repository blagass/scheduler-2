package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The ContactDAO uses the DAO interface to carry out Contact specific database manipulation
 */
public class ContactDAO implements DAOInterface<Contact>{
    /**
     * This method returns all available contacts in the database
     * @return allContacts ObservableList containing all contacts
     * @throws SQLException database errors
     */
    @Override
    public ObservableList<Contact> getList() throws SQLException {
        JDBC.openConnection();
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();

        String sql = "SELECT * FROM CONTACTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");


            Contact contact = new Contact(contactId,contactName,email);
            allContacts.add(contact);

        }
        return allContacts;

    }

    /**
     * This method retrieves a specific contact using the Contact ID
     * @param id id of the contact to retrieve
     * @return this returns the matching contact
     * @throws SQLException for database error catching
     */
    @Override
    public Contact getById(int id) throws SQLException {
        JDBC.openConnection();

        String sql = "SELECT * FROM CONTACTS where Contact_ID =?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        Contact contact = null;
        while (rs.next()) {
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");


            contact = new Contact(contactId, contactName, email);

        }
        return contact;
    }
}
