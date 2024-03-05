package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Appointment;
import javafx.collections.ObservableList;

public class AppointmentDAO implements DAOInterface<Appointment> {
    @Override
    public ObservableList<Appointment> getList() {
        return null;
    }

    @Override
    public boolean insert(Appointment object) {
        return false;
    }

    @Override
    public boolean update(Appointment object) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Appointment getById(int id) {
        return null;
    }
}
