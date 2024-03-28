package com.brandonlagasse.scheduler2.helper;

import com.brandonlagasse.scheduler2.dao.UserDAO;
import com.brandonlagasse.scheduler2.model.Appointment;
import com.brandonlagasse.scheduler2.model.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is a helper for the LoginView, as well as Login_Activity reporting
 *
 */
public class LoginHelper {

    private static final String LOG_FILE = "login_activity.txt";

    /**
     * This method logs users attempts to login, including user, timestamp, and login success boolean
     * @param username this parameter is checked against the Name column in the User db
     * @param success this is a check for the success of the login atttempt
     */
    public static void logLoginAttempt(String username, boolean success) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) { // 'true' to append
            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String logEntry = String.format("User: %s, Timestamp: %s, Login Success: %s\n",
                    username, ldt.format(formatter), success);

            bufferedWriter.write(logEntry);

        } catch (IOException e) {
            System.err.println("Error logging login attempt: " + e.getMessage());
            // Consider more robust error handling here
        }
    }

//    /**
//     * checks
//     * @param id
//     * @return
//     * @throws SQLException
//     */
//    public static boolean appointmentChecker(int id) throws SQLException {
//
//        Appointment appointment;
//
//        UserDAO userDAO = new UserDAO();
//        User user = userDAO.getById(id);
//        return false;
//    }
}
