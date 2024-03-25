package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.AppointmentDAO;
import com.brandonlagasse.scheduler2.model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportView implements Initializable {
    public TextArea appointmentSchedule;
    public TextArea contactSchedule;
    public TextArea mostAppointments;
    public TextArea appointmentsByType;
    public TextArea appointmentsByMonth;
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Appointment> appointments;
        try {
            appointments = appointmentDAO.getList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Contact Schedules
        String contactScheduleReport = buildContactSchedules(appointments);
        contactSchedule.setText(contactScheduleReport);

        // Appointments by type and month
        String appointmentsByTypeMonthReport = buildAppointmentsByTypeMonthReport(appointments);
        appointmentsByType.setText(appointmentsByTypeMonthReport);

        // Total Appointments by Month
        List<AppointmentCount> countsByMonth = countsByMonth(appointments);
        String reportByMonth = buildReport(countsByMonth, "Total Appointments by Month");
        appointmentsByMonth.setText(reportByMonth);

        // Users with the Most Appointments
        String mostAppointmentsReport = buildMostAppointmentsReport(appointments);
        mostAppointments.setText(mostAppointmentsReport);
    }

    private List<AppointmentCount> countsByMonth(List<Appointment> appointments) {
        List<AppointmentCount> counts = new ArrayList<>();
        for (Appointment appointment : appointments) {
            Month month = appointment.getStart().getMonth();
            updateCount(counts, month);
        }
        return counts;
    }

    private List<AppointmentCount> countsByTYpe(List<Appointment> appointments) {
        List<AppointmentCount> counts = new ArrayList<>();
        for (Appointment appointment : appointments) {
            String type = appointment.getType();
            updateCount(counts, type);
        }
        return counts;
    }

    private void updateCount(List<AppointmentCount> counts, Object object) {
        for (AppointmentCount entry : counts) {
            if (entry.object.equals(object)) {
                entry.count++;
                return;
            }
        }
        counts.add(new AppointmentCount(object, 1));
    }

    private String buildReport(List<AppointmentCount> counts, String title) {
        StringBuilder builder = new StringBuilder(title + ":\n");
        for (AppointmentCount entry : counts) {
            builder.append(entry.object.toString())
                    .append(": ")
                    .append(entry.count)
                    .append("\n");
        }
        return builder.toString();
    }


    private String buildContactSchedules(List<Appointment> appointments) {
        List<ContactSchedule> contactSchedules = new ArrayList<>();
        for (Appointment appointment : appointments) {
            int contactId = appointment.getContactId();
            updateContactSchedule(contactSchedules, contactId, appointment);
        }
        return formatContactSchedules(contactSchedules);
    }

    private void updateContactSchedule(List<ContactSchedule> schedules, int contactId, Appointment appt) {
        for (ContactSchedule schedule : schedules) {
            if (schedule.contactId == contactId) {
                schedule.appointments.add(appt);
                return;
            }
        }
        schedules.add(new ContactSchedule(contactId, appt));
    }

    private String formatContactSchedules(List<ContactSchedule> schedules) {
        StringBuilder builder = new StringBuilder();
        for (ContactSchedule schedule : schedules) {
            builder.append("Contact ID: ").append(schedule.contactId).append("\n");
            for (Appointment appt : schedule.appointments) {
                builder.append("  Appointment ID: ").append(appt.getId()).append("\n");
                builder.append("  Title: ").append(appt.getTitle()).append("\n");
                builder.append("  Type: ").append(appt.getType()).append("\n");
                builder.append("  Description: ").append(appt.getDescription()).append("\n");
                builder.append("  Start Date/Time: ").append(appt.getStart()).append("\n");
                builder.append("  End Date/Time: ").append(appt.getEnd()).append("\n");
                builder.append("  Customer ID: ").append(appt.getCustomerId()).append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private String buildAppointmentsByTypeMonthReport(List<Appointment> appointments) {
        List<AppointmentTypeMonthCount> counts = new ArrayList<>();
        for (Appointment appointment : appointments) {
            String type = appointment.getType();
            Month month = appointment.getStart().getMonth();
            updateTypeMonthCount(counts, type, month);
        }
        return formatAppointmentsByTypeMonth(counts);
    }

    private void updateTypeMonthCount(List<AppointmentTypeMonthCount> counts, String type, Month month) {
        for (AppointmentTypeMonthCount entry : counts) {
            if (entry.type.equals(type) && entry.month.equals(month)) {
                entry.count++;
                return;
            }
        }
        counts.add(new AppointmentTypeMonthCount(type, month, 1));
    }

    private String formatAppointmentsByTypeMonth(List<AppointmentTypeMonthCount> counts) {
        StringBuilder builder = new StringBuilder("Appointments by Type and Month:\n");
        for (AppointmentTypeMonthCount entry : counts) {
            builder.append(entry.type).append(" (").append(entry.month).append("): ")
                    .append(entry.count)
                    .append("\n");
        }
        return builder.toString();
    }

    private String buildMostAppointmentsReport(List<Appointment> appointments) {
        List<UserAppointmentCount> counts = new ArrayList<>();
        for (Appointment appointment : appointments) {
            int userId = appointment.getUserId();
            updateUserCount(counts, userId);
        }
        return formatMostAppointments(counts);
    }

    private void updateUserCount(List<UserAppointmentCount> counts, int userId) {
        for (UserAppointmentCount entry : counts) {
            if (entry.userId == userId) {
                entry.count++;
                return;
            }
        }
        counts.add(new UserAppointmentCount(userId, 1));
    }

    private String formatMostAppointments(List<UserAppointmentCount> counts) {
        StringBuilder builder = new StringBuilder("Users with Most Appointments:\n");
        // Consider sorting 'counts' to show users in descending appointment order
        for (UserAppointmentCount entry : counts) {
            builder.append("User ID: ").append(entry.userId)
                    .append(": ")
                    .append(entry.count)
                    .append("\n");
        }
        return builder.toString();
    }

    public void onExit(ActionEvent actionEvent) {
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/main-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading main-view.fxml: " + e.getMessage());
        }
    }


    // Helper classes
    class AppointmentCount {
        Object object;
        int count;

        public AppointmentCount(Object key, int count) {
            this.object = key;
            this.count = count;
        }
    }

    class ContactSchedule {
        int contactId;
        List<Appointment> appointments = new ArrayList<>();

        public ContactSchedule(int contactId, Appointment appointment) {
            this.contactId = contactId;
            this.appointments.add(appointment);
        }
    }

    class AppointmentTypeMonthCount {
        String type;
        Month month;
        int count;

        public AppointmentTypeMonthCount(String type, Month month, int count) {
            this.type = type;
            this.month = month;
            this.count = count;
        }
    }

    class UserAppointmentCount {
        int userId;
        int count;

        public UserAppointmentCount(int userId, int count) {
            this.userId = userId;
            this.count = count;
        }
    }
}
