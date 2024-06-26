package com.brandonlagasse.scheduler2.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This JDBC open/close connection singleton class is for reuse in all DAO open/close connection scenarios. It uses open to set credentials to the database whenever its needed.
 */
public abstract class JDBC {
    ///Class Variables///
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location ="//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbUrl = protocol + vendor + location + databaseName +"?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;
    ///


    /**
     * Opens the DB Connection
     */
    public static void openConnection(){ //OPEN DATABASE
        try{
            if(connection == null || connection.isClosed()){
                Class.forName(driver);
                connection = DriverManager.getConnection(jdbUrl,userName,password);
            }

        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error" + e.getMessage());
        }
    }


    /**
     * Closes the DB Connection
     */
    public static void closeConnection(){ //CLOSE DATABASE
        try{
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
        catch(Exception e){
            System.out.println("Error" + e.getMessage());

        }
    }

}