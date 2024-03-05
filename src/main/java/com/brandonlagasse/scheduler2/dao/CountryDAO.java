package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Country;
import javafx.collections.ObservableList;

public class CountryDAO implements DAOInterface<Country>{
    @Override
    public ObservableList<Country> getList() {
        return null;
    }

    @Override
    public boolean insert(Country object) {
        return false;
    }

    @Override
    public boolean update(Country object) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Country getById(int id) {
        return null;
    }
}
