package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Customer;
import javafx.collections.ObservableList;

public class CustomerDAO implements DAOInterface<Customer>{

    @Override
    public ObservableList<Customer> getList() {
        return null;
    }

    @Override
    public boolean insert(Customer object) {
        return false;
    }

    @Override
    public boolean update(Customer object) {
        return false;
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
