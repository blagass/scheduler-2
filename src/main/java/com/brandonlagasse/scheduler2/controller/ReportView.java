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

/**
 * The ReportView is a robust collection of reports and helper methods to display numerous stats the users. There are three primary reports.
 * Looking back, I would do this whole section differently, and it ended up this way because of the step-by-step approach I used because of the complexity of the reports (originally in a table).
 */
public class ReportView implements Initializable {
    public TextArea contactSchedule;
    public TextArea mostAppointments;
    public TextArea appointmentsByType;
    public TextArea appointmentsByMonth;
    private AppointmentDAO appointmentDAO = new AppointmentDAO();


    /**
     * This initialization method sets up the report TextAreas. This is done by pulling each report in smaller methods, and combining within initialize.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Appointment> appointments;
        try {
            appointments = appointmentDAO.getList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String contactScheduleReport = buildContactSchedules(appointments);
        contactSchedule.setText(contactScheduleReport);

        //appointments by type and month
        String appointmentsByTypeMonthReport = appointmentsByTypeAndMonth(appointments);
        appointmentsByType.setText(appointmentsByTypeMonthReport);


        List<AppointmentCount> countsByMonth = countsByMonth(appointments);
        String reportByMonth = buildReport(countsByMonth, "Total Appointments by Month");
        appointmentsByMonth.setText(reportByMonth);


        String mostAppointmentsReport = mostAppointmentsReport(appointments);
        mostAppointments.setText(mostAppointmentsReport);
    }

    /**
     * This gets the count of appointments for each month within a given list
     * @param appointments this is an array of appointments
     * @return returns a list which contain the month and number of appointments for that month.
     */
    private List<AppointmentCount> countsByMonth(List<Appointment> appointments) {
        List<AppointmentCount> counts = new ArrayList<>();
        for (Appointment appointment : appointments) {
            Month month = appointment.getStart().getMonth();
            updateCount(counts, month);
        }
        return counts;
    }

    /**
     * This method updates a list based on the Object given. Using an Object here makes it funcitonal for multiple uses.
     * @param counts the list of appointmentcounts to update
     * @param object the object category
     */
    private void updateCount(List<AppointmentCount> counts, Object object) {
        for (AppointmentCount entry : counts) {
            if (entry.object.equals(object)) {
                entry.count++;
                return;
            }
        }
        counts.add(new AppointmentCount(object, 1));
    }

    /**
     * This builds a visual, formatted report based on the AppointmentCounts object provided
     * @param counts AppointmentCounts containing an object and its  count.
     * @param title title of the report
     * @return This returns the string after it's been converted
     */
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

    /**
     * This builds a report of contact schedules with appointments
     * @param appointments this is the parameter for appointment list
     * @return returns the contact schedules formatted
     */
    private String buildContactSchedules(List<Appointment> appointments) {
        List<ContactSchedule> contactSchedules = new ArrayList<>();
        for (Appointment appointment : appointments) {
            int contactId = appointment.getContactId();
            updateContactSchedule(contactSchedules, contactId, appointment);
        }
        return formatContactSchedules(contactSchedules);
    }

    /**
     * This method takes in a contact id and provides a list of schedules
     * @param schedules the parameter holding the schedule list
     * @param contactId the id input for matching schedules
     * @param appt the appointment parameter for the contacts schedule
     */
    private void updateContactSchedule(List<ContactSchedule> schedules, int contactId, Appointment appt) {
        for (ContactSchedule schedule : schedules) {
            if (schedule.contactId == contactId) {
                schedule.appointments.add(appt);
                return;
            }
        }
        schedules.add(new ContactSchedule(contactId, appt));
    }

    /**
     * Output formatting to display the contact scheudles
     * LAMBDA - this lambda formats the stringbuilder using a foreach loop.
     * @param schedules the schedules to display
     * @return formatted string results
     */
    private String formatContactSchedules(List<ContactSchedule> schedules) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ContactSchedule schedule : schedules) {
            stringBuilder.append("Contact ID: ").append(schedule.contactId).append("\n");

            //LAMBDA
            schedule.appointments.forEach(appt -> {
                stringBuilder.append(" Appointment ID: ").append(appt.getId()).append("\n")
                        .append(" Title: ").append(appt.getTitle()).append("\n")
                        .append(" Type: ").append(appt.getType()).append("\n")
                        .append(" Description: ").append(appt.getDescription()).append("\n")
                        .append(" Start Date/Time: ").append(appt.getStart()).append("\n")
                        .append(" End Date/Time: ").append(appt.getEnd()).append("\n")
                        .append(" Customer ID: ").append(appt.getCustomerId()).append("\n");
            });

//
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * This method creates a report for the number of appointments by appointment type and month
     * @param appointments appointments object list
     * @return returns a string of appointments by month and type
     */
    private String appointmentsByTypeAndMonth(List<Appointment> appointments) {
        List<AppointmentsByTypeAndMonth> counts = new ArrayList<>();
        for (Appointment appointment : appointments) {
            String type = appointment.getType();
            Month month = appointment.getStart().getMonth();
            updateTypeMonthCount(counts, type, month);
        }
        return formatAppointmentsByTypeMonth(counts);
    }

    /**
     * This method updates the count total for type and month
     * @param counts  the totals list
     * @param type uses the type variable to return a total
     * @param month placeholder for identifying month counts
     */
    private void updateTypeMonthCount(List<AppointmentsByTypeAndMonth> counts, String type, Month month) {
        for (AppointmentsByTypeAndMonth entry : counts) {
            if (entry.type.equals(type) && entry.month.equals(month)) {
                entry.count++;
                return;
            }
        }
        counts.add(new AppointmentsByTypeAndMonth(type, month, 1));
    }

    /**
     * This method returns the type and month in a readable way in the TextArea
     * @param counts this is used to total the month and type
     * @return shows the formated copy used to display in the UI
     */
    private String formatAppointmentsByTypeMonth(List<AppointmentsByTypeAndMonth> counts) {
        StringBuilder stringBuilder = new StringBuilder("Appointments by Type and Month:\n");
        for (AppointmentsByTypeAndMonth entry : counts) {
            stringBuilder.append(entry.type).append(" (").append(entry.month).append("): ")
                    .append(entry.count)
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * This method is used to return a formatted result of the users with the most appointments
     * @param appointments list of appointments used for counting
     * @return returns the formatted appointment count
     */
    private String mostAppointmentsReport(List<Appointment> appointments) {
        List<UserAppointmentCount> counts = new ArrayList<>();
        for (Appointment appointment : appointments) {
            int userId = appointment.getUserId();
            updateUserCount(counts, userId);
        }
        return formatMostAppointments(counts);
    }

    /**
     * This method is used to update the amount of appointments counted for a specific user
     * @param counts the number of appointments
     * @param userId the associated user
     */
    private void updateUserCount(List<UserAppointmentCount> counts, int userId) {
        for (UserAppointmentCount entry : counts) {
            if (entry.userId == userId) {
                entry.count++;
                return;
            }
        }
        counts.add(new UserAppointmentCount(userId, 1));
    }

    /**
     * This method shows the users with the most appointments
     * @param counts the count parameter is used to store the number of appointments
     * @return returns the string reults showing the users with the most appointments
     */
    private String formatMostAppointments(List<UserAppointmentCount> counts) {
        StringBuilder builder = new StringBuilder("Users with Most Appointments:\n");

        for (UserAppointmentCount entry : counts) {
            builder.append("User ID: ").append(entry.userId)
                    .append(": ")
                    .append(entry.count)
                    .append("\n");
        }
        return builder.toString();
    }

    /**
     * This navigates users back to the Main View screen.
     * @param actionEvent Triggered by click on the Go Back button
     */
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

    /**
     * This helper method uses Object to store various objects like months and contact ids throughout several reports
     */
    class AppointmentCount {
        Object object;
        int count;

        public AppointmentCount(Object key, int count) {
            this.object = key;
            this.count = count;
        }
    }

    /**
     * This method is used for storing a contacts schedule and appointments
     */
    class ContactSchedule {
        int contactId;
        List<Appointment> appointments = new ArrayList<>();

        /**
         * @param contactId contact id identifier
         * @param appointment appointment to add to the contacts schedule
         */
        public ContactSchedule(int contactId, Appointment appointment) {
            this.contactId = contactId;
            this.appointments.add(appointment);
        }
    }

    /**
     * This method is a count of appointments by type and month
     */
    class AppointmentsByTypeAndMonth {
        String type;
        Month month;
        int count;

        public AppointmentsByTypeAndMonth(String type, Month month, int count) {
            this.type = type;
            this.month = month;
            this.count = count;
        }
    }

    /**
     * This method is the number of appointments a user has
     */
    class UserAppointmentCount {
        int userId;
        int count;

        public UserAppointmentCount(int userId, int count) {
            this.userId = userId;
            this.count = count;
        }
    }
}
