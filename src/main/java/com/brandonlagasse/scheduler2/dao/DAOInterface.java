package com.brandonlagasse.scheduler2.dao;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface DAOInterface<T> {
    default ObservableList<T> getList() throws SQLException {
        return null;
    }

     default boolean insert(T object) throws SQLException // More generic name
    {
        return false;
    }

     default  boolean update(T object) throws SQLException // More generic name
    {

        return false;
    }

     default boolean delete(int id) throws SQLException {
        return false;
    }

     default T getById(int id) throws SQLException {
        return null;
    }
}