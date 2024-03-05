package com.brandonlagasse.scheduler2.helper;

import com.brandonlagasse.scheduler2.dao.UserDAO;
import com.brandonlagasse.scheduler2.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Tester {
User steve = new User(6,"steve","guy");

ObservableList<User> allUsers = FXCollections.observableArrayList();

    public ObservableList<User> getAllUsers() {
        return allUsers;
    }
}
