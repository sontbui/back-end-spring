package com.project.shopapp.database;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.time.Instant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDatabaseFailure {

    private Connection connection;

    @Before
    public void setUp() throws Exception {
        // create config to database
        String url = "jdbc:mysql://localhost:3307/ShopApp?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String username = "root"; // username
        String password = "Abc123456789@"; // password
        connection = DriverManager.getConnection(url, username, password);
    }

    @After
    public void tearDown() throws Exception {
        // Đóng kết nối sau khi hoàn thành mỗi test case
        if (connection != null) {
            connection.close();
        }
    }

//    bc name = Category Tests doesn't exist
    @Test
    public void testDropCategory() throws SQLException {
        String dropTableQuery = "DELETE FROM categories WHERE name = 'Category Testsss';";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(dropTableQuery);
            assertTrue(rowsAffected > 0);
            boolean checkExist = false;
            String checkQuery = "SELECT * FROM categories WHERE name = 'Category Testss'";
            try (Statement statementResult = connection.createStatement();
                 ResultSet resultSet = statementResult.executeQuery(checkQuery)) {
                checkExist = resultSet.next();
            }
            assertFalse(checkExist);
        }
    }
//    bc name = abcd doesn't exist
    @Test
    public void testDeleteOrderSuccessfully() throws SQLException {
        String deleteQuery = "DELETE FROM orders WHERE fullname = " + "'abcd'";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(deleteQuery);
            assertEquals(1, rowsAffected);
        }
        String selectQuery = "SELECT * FROM orders WHERE fullname = " + "'abcd'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            assertFalse(resultSet.next());
        }
    }

//  bc price = Category Testsss doesn't exist
    @Test
    public void testDeleteOrderDetail() throws SQLException {
        String sqlDelete = "DELETE FROM order_details WHERE price = 1 ";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(sqlDelete);
            assertEquals(1, rowsAffected);
        }
        String selectQuery = "SELECT * FROM order_details WHERE  price = 1 ";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            assertFalse(resultSet.next());
        }
    }

//  bc image_url = url_url doesn't exist
    @Test
    public void testDeleteProductImage() throws SQLException {
        String deleteQuery = "DELETE FROM product_images WHERE image_url = 'url_url'";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(deleteQuery);
            assertEquals(1, rowsAffected);
        }
    }

//  bc id = 5 doesn't exist
    @Test
    public void testDeleteRole() throws SQLException {
        String deleteQuery = "DELETE FROM roles WHERE id = 9";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(deleteQuery);
            assertEquals(1, rowsAffected);
        }
    }
//  bc fullname = 'user_failure'" doesn't exist
    @Test
    public void testDeleteUser() throws SQLException {
        // Xóa bản ghi từ bảng users
        String deleteQuery = "DELETE FROM users WHERE fullname = 'user_failure'";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(deleteQuery);
            assertEquals(1, rowsAffected); // Kiểm tra rằng chỉ có một bản ghi đã bị xóa
        }
    }



}

