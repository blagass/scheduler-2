package com.brandonlagasse.scheduler2.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginHelper {

    private static final String LOG_FILE = "login_activity.txt";

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
}
