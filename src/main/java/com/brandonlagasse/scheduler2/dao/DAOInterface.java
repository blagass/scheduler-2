package com.brandonlagasse.scheduler2.dao;

import javafx.collections.ObservableList;

import java.sql.SQLException;

/**
 * This interface is used in associated with all DAO available in the dao package. It contains CRUD database calls, as well as an get by id fetch.
 * @param <T>
 */
public interface DAOInterface<T> {
    /**
     * Interface list for retriving all objects available
     * @return null
     * @throws SQLException connection errors
     */
    default ObservableList<T> getList() throws SQLException {
        return null;
    }

    /**
     * Interace for inserting an ojbect into the database
     * @param object object to insert
     * @return boolean to determine method success
     * @throws SQLException catch database errors
     */
     default boolean insert(T object) throws SQLException // More generic name
    {
        return false;
    }

    /**
     * Interface for updating objects in the database
     * @param object object to update
     * @return boolean for success determination
     * @throws SQLException catch for database connection errors, etc.
     */
     default  boolean update(T object) throws SQLException // More generic name
    {

        return false;
    }

    /**
     * Interface for removing objects out from the database
     * @param id the id of the object to delte
     * @return boolean to determine success
     * @throws SQLException database error catching
     */
     default boolean delete(int id) throws SQLException {
        return false;
    }

    /**
     * This is the interface for retrieving an object by an id
     * @param id id of the object to retrieve
     * @return boolean to tell if the retrieval was successfull
     * @throws SQLException database error catching
     */
     default T getById(int id) throws SQLException {
        return null;
    }
}