package com.brandonlagasse.scheduler2.dao;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JDBCTest {
    @Test
    void testOpenConnection() {
        JDBC.openConnection();
        try {
            assertNotNull(JDBC.connection);
            assertFalse(JDBC.connection.isClosed());
        } catch (SQLException e) {
            fail();
        } finally {
            JDBC.closeConnection();
        }
    }

    @Test
    void testCloseConnection() {
        JDBC.openConnection();

        try {
            assertNotNull(JDBC.connection);
           assertFalse(JDBC.connection.isClosed());
        } catch (SQLException e) {
            fail("SQLException: " + e.getMessage());
        }

        JDBC.closeConnection();

        try {
            assertTrue(JDBC.connection.isClosed());
        } catch (SQLException e) {
            fail("SQLException: " + e.getMessage());
        }
    }
}
