package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO For accessing all country related infromation in the database
 */
public class CountryDAO implements DAOInterface<Country>{
    /**
     * Retrieves all countries from the database
     * @return returns allCountries, which holds all available countries
     * @throws SQLException catches database errors
     */
    @Override
    public ObservableList<Country> getList() throws SQLException {

        JDBC.openConnection();
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        String sql = "SELECT * FROM COUNTRIES";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int countryId = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");

            Country country = new Country(countryId,countryName);
            allCountries.add(country);

        }
        return allCountries; // Returns the observable list

    }
//
//    @Override
//    public boolean insert(Country object) {
//        return false;
//    }
//
//    @Override
//    public boolean update(Country object) {
//        return false;
//    }
//
//    @Override
//    public boolean delete(int id) {
//        return false;
//    }

    /**
     * Method for retrieving a specific country by ID
     * @param id id of the Country to retrieve
     * @return individual matching Country
     * @throws SQLException for database errors
     */
    @Override
    public Country getById(int id) throws SQLException {
        String sql = "SELECT * FROM countries WHERE Country_ID =?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Country country = null;
        while (rs.next()) {
            int countryId = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");

            country = new Country(countryId, countryName);

        }
        return country;
    }
}
