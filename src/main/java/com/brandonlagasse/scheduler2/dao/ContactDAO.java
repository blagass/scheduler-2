package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Contact;
import javafx.collections.ObservableList;

public class ContactDAO implements DAOInterface<Contact>{
    @Override
    public ObservableList<Contact> getList() {
        return null;
    }

    @Override
    public boolean insert(Contact object) {
        return false;
    }

    @Override
    public boolean update(Contact object) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Contact getById(int id) {
        return null;
    }
}
