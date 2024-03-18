package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class AppointmentDAO implements DAOInterface<Appointment> {
    @Override
    public ObservableList<Appointment> getList() throws SQLException {
        JDBC.openConnection();
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, APPOINTMENTS.User_ID, Contact_ID " +
                "FROM APPOINTMENTS " ;
//                "INNER JOIN CUSTOMERS ON APPOINTMENTS.Customer_ID = CUSTOMERS.Customer_ID " ;
//                "INNER JOIN USERS ON APPOINTMENTS.User_ID = USERS.User_ID " +
//                "INNER JOIN CONTACTS ON APPOINTMENTS.Contact_ID = CONTACTS.Contact_ID";


        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
//
        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            String appointmentType = rs.getString("Type");

            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();

            int appointmentCustomerId = rs.getInt("Customer_ID");
            int appointmentUserId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");

            System.out.println(appointmentId);
            System.out.println(appointmentTitle);
            System.out.println(appointmentDescription);
            System.out.println(appointmentLocation);
            System.out.println(appointmentType);
            System.out.println(appointmentStart);
            System.out.println(appointmentEnd);
            System.out.println(appointmentCustomerId);
            System.out.println(appointmentUserId);
            System.out.println(contactId);

            // Timezone Handling
            ZoneId currentZone = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime zdt = appointmentStart.atZone(currentZone);
            ZonedDateTime currentToLocalZDT = zdt.withZoneSameInstant(currentZone);
            appointmentStart = currentToLocalZDT.toLocalDateTime();

            Appointment appointment = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentUserId, contactId);
            allAppointments.add(appointment);
        }

        return allAppointments;
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
