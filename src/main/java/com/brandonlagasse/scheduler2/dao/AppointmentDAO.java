package com.brandonlagasse.scheduler2.dao;

import com.brandonlagasse.scheduler2.helper.TimeHelper;
import com.brandonlagasse.scheduler2.model.Appointment;
import com.brandonlagasse.scheduler2.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.*;
import java.util.TimeZone;

import static com.brandonlagasse.scheduler2.helper.TimeHelper.displayErrorMessage;

/**
 * This is the data access object specifically for the appointments database. It contains all CRUD as well as appointment Id retrieval.
 */
public class AppointmentDAO implements DAOInterface<Appointment> {
    /**
     * This method retrieves all available appointments n the database
     * @return returns an ObservableList of all appointments in the database
     * @throws SQLException returns an error
     */
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

            Appointment appointment = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentUserId, contactId);
            allAppointments.add(appointment);
        }
        return allAppointments;
    }

    /**
     * This method takes an appointment and inserts it in the database
     * @param appointment the appointment to insert
     * @return lets us know if the insert was successful
     * @throws SQLException returns an error with database access
     */
    @Override
    public boolean insert(Appointment appointment) throws SQLException {
        JDBC.openConnection();

        LocalDateTime start = appointment.getStart();
        LocalDateTime end = appointment.getEnd();

        int userId = appointment.getUserId();
        ObservableList<Appointment> overlappingAppointment = getAppointmentsForUser(userId);

        LocalDate startDate = start.toLocalDate();
        LocalTime startTime = start.toLocalTime();
        LocalDate endDate = end.toLocalDate();
        LocalTime endTime = end.toLocalTime();

        for (Appointment existingAppt : overlappingAppointment) {
            LocalDateTime existingStartLdt = existingAppt.getStart();
            LocalDateTime existingEndLdt = existingAppt.getEnd();

            LocalDate existingStartDate = existingStartLdt.toLocalDate();
            LocalTime existingStartTime = existingStartLdt.toLocalTime();
            LocalDate existingEndDate = existingEndLdt.toLocalDate();
            LocalTime existingEndTime = existingEndLdt.toLocalTime();

            if (!startDate.isEqual(existingStartDate)) {
                continue;
            }

            if (startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Uh oh!");
                alert.setContentText("Appointment overlaps with an existing appointment.");
                alert.showAndWait();
                return false;
            }
        }

        Timestamp startTimeStamp = Timestamp.valueOf(start);
        Timestamp endTimeStamp = Timestamp.valueOf(end);

        String sql = "INSERT INTO APPOINTMENTS(Title,Description,Location,Type,Start,End,Customer_ID,User_ID,Contact_ID) VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,appointment.getTitle());
        ps.setString(2,appointment.getDescription());
        ps.setString(3,appointment.getLocation());
        ps.setString(4,appointment.getType());
        ps.setTimestamp(5,startTimeStamp);
        ps.setTimestamp(6,endTimeStamp);
        ps.setInt(7, appointment.getCustomerId());
        ps.setInt(8,appointment.getUserId());
        ps.setInt(9,appointment.getContactId());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false;
        }
        return true;

    }

    /**
     * Update takes in an appointment and changes the current associated update in the database
     * @param appointment the appointment to update
     * @return boolean to tell if the update worked
     * @throws SQLException for database errors
     */
    @Override
    public boolean update(Appointment appointment) throws SQLException {

        JDBC.openConnection();

        LocalDateTime start = appointment.getStart();
        LocalDateTime end = appointment.getEnd();

        int userId = appointment.getUserId();
        ObservableList<Appointment> appointments = getAppointmentsForUser(userId);

        LocalDate startDate = start.toLocalDate();
        LocalTime startTime = start.toLocalTime();
        LocalDate endDate = end.toLocalDate();
        LocalTime endTime = end.toLocalTime();


        boolean overlapFound = false;
        for (Appointment existingAppt : appointments) {
            if (existingAppt.getId() == appointment.getId()) {
                continue;
            }

            LocalDateTime existingStartLdt = existingAppt.getStart();
            LocalDateTime existingEndLdt = existingAppt.getEnd();

            LocalDate existingStartDate = existingStartLdt.toLocalDate();
            LocalTime existingStartTime = existingStartLdt.toLocalTime();
            LocalDate existingEndDate = existingEndLdt.toLocalDate();
            LocalTime existingEndTime = existingEndLdt.toLocalTime();

            if (!startDate.isEqual(existingStartDate)) {
                continue;
            }

            if (startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) {
                overlapFound = true;
                break;
            }

        }
        if(overlapFound){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Uh oh!");
            alert.setContentText("Appointment overlaps with an existing appointment.");
            alert.showAndWait();
            return false;
        }

        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID =? WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);


        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
        ps.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
        ps.setInt(7, appointment.getCustomerId());
        ps.setInt(8, appointment.getUserId());
        ps.setInt(9, appointment.getContactId());
        ps.setInt(10, appointment.getId());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected == 0) {
            return false;
        }
        return true;
    }

    /**
     * This removes an appointment from the database based on an ID
     * @param id the id associated with the appointment to delete
     * @return boolean for delete success
     * @throws SQLException database errors tryuing to update existing customer
     */
    @Override
    public boolean delete(int id) throws SQLException {

        JDBC.openConnection();
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        JDBC.closeConnection();
        return rowsAffected > 0;
    }

    /**
     * This is a simple method that returns an appointment by ID
     * @param id appointment id associated with appointment to return
     * @return boolean to show delete success or failure
     * @throws SQLException for database access errors
     */
    @Override
    public Appointment getById(int id) throws SQLException {
        JDBC.openConnection();
        String sql = "SELECT * FROM appointments WHERE Appointment_ID =?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        Appointment appointment = null;

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


            ZoneId currentZone = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime zdt = appointmentStart.atZone(currentZone);
            ZonedDateTime currentToLocalZDT = zdt.withZoneSameInstant(currentZone);
            appointmentStart = currentToLocalZDT.toLocalDateTime();

            appointment = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentUserId, contactId);
            //allAppointments.add(appointment);
        }
        return appointment;
    }

    public ObservableList<Appointment> getAppointmentsForUser(int userId) throws SQLException {
        JDBC.openConnection();

        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM APPOINTMENTS WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerId = rs.getInt("Customer_ID");
            int contactId = rs.getInt("Contact_ID");


            ZoneId currentZone = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime zdtStart = start.atZone(currentZone);
            ZonedDateTime currentToLocalStart = zdtStart.withZoneSameInstant(currentZone);
            start = currentToLocalStart.toLocalDateTime();

            ZonedDateTime zdtEnd = end.atZone(currentZone);
            ZonedDateTime currentToLocalEnd = zdtEnd.withZoneSameInstant(currentZone);
            end = currentToLocalEnd.toLocalDateTime();

            Appointment appointment = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
            userAppointments.add(appointment);
        }
        return userAppointments;
    }

}
