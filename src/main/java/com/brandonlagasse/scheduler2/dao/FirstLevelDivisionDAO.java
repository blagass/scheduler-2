package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This DAO is for accessing CRUD operations for pulling lists of FirstLevelDivisions
 */
public class FirstLevelDivisionDAO implements DAOInterface<FirstLevelDivision> {
    /**
     * This method pulls all first level divisions available and adds them to a list
     * @return ObservableList with all divisions
     * @throws SQLException database error handling
     */
    @Override
    public ObservableList<FirstLevelDivision> getList() throws SQLException {
        JDBC.openConnection();
        ObservableList<FirstLevelDivision> allStates = FXCollections.observableArrayList();
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int stateId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            FirstLevelDivision firstLevelDivision = new FirstLevelDivision(stateId,division,countryId);
            allStates.add(firstLevelDivision);
        }
        return allStates; // Returns the observable list
    }

//    @Override
//    public boolean insert(FirstLevelDivision object) {
//        return false;
//    }
//
//    @Override
//    public boolean update(FirstLevelDivision object) {
//        return false;
//    }
//
//    @Override
//    public boolean delete(int id) {
//        return false;
//    }

    /**
     * This method retrieves a FirstLevelDivison by its ID
     * @param id id of the FLD to retrieve
     * @return matching FLD to ID
     * @throws SQLException database errors
     */
    @Override
    public FirstLevelDivision getById(int id) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID =?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        FirstLevelDivision fld = null;
        while (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            fld = new FirstLevelDivision(divisionId, division, countryId);

        }
        return fld;
    }

    /**
     * This method returns a List of all US states
     * @return list of all available US states in the database
     * @throws SQLException for errors in database connections and CRUD
     */
    public static ObservableList<FirstLevelDivision> usStates() throws SQLException {
        JDBC.openConnection();
        ObservableList<FirstLevelDivision> allUsStates = FXCollections.observableArrayList();
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = 1";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int stateId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            FirstLevelDivision firstLevelDivision = new FirstLevelDivision(stateId,division,countryId);
            allUsStates.add(firstLevelDivision);
        }
        return allUsStates; // Returns the observable list
    }

    /**
     * This method returns a list of all UK states
     * @return List of all available UK states in the database
     * @throws SQLException for database error
     */
    public static ObservableList<FirstLevelDivision> ukStates() throws SQLException {
        JDBC.openConnection();
        ObservableList<FirstLevelDivision> allUkStates = FXCollections.observableArrayList();
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = 2";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int stateId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            FirstLevelDivision firstLevelDivision = new FirstLevelDivision(stateId,division,countryId);
            allUkStates.add(firstLevelDivision);
        }
        return allUkStates; // Returns the observable list
    }

    /**
     * This method returns a list of Canadian provinces
     * @return list of all Canadian provinces in the database
     * @throws SQLException database errors
     */
    public static ObservableList<FirstLevelDivision> canadaStates() throws SQLException {
        JDBC.openConnection();
        ObservableList<FirstLevelDivision> allCanadaStates = FXCollections.observableArrayList();
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = 3";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int stateId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            FirstLevelDivision firstLevelDivision = new FirstLevelDivision(stateId,division,countryId);
            allCanadaStates.add(firstLevelDivision);
        }
        return allCanadaStates; // Returns the observable list
    }

    /**
     * This method retrieves specific divisions by country ID
     * @param id the Country ID to match against
     * @return list of matching divisions
     * @throws SQLException database errors
     */
    public ObservableList<FirstLevelDivision> getDivisionsByCountryId(int id) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID =?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        ObservableList<FirstLevelDivision> firstLevelDivisions= FXCollections.observableArrayList();
        FirstLevelDivision fld = null;
        while (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            fld = new FirstLevelDivision(divisionId, division, countryId);
            firstLevelDivisions.add(fld);
        }
        return firstLevelDivisions;
    }
}
