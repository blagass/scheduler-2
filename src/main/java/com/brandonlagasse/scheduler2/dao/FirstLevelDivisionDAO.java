package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.FirstLevelDivision;
import javafx.collections.ObservableList;

public class FirstLevelDivisionDAO implements DAOInterface<FirstLevelDivision> {
    @Override
    public ObservableList<FirstLevelDivision> getList() {
        return null;
    }

    @Override
    public boolean insert(FirstLevelDivision object) {
        return false;
    }

    @Override
    public boolean update(FirstLevelDivision object) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public FirstLevelDivision getById(int id) {
        return null;
    }
}
