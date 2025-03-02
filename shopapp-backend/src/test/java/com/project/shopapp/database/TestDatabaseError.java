package com.project.shopapp.database;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.time.Instant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDatabaseError {

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

    //Test create table error bc table users alread exist
    @Test
    public void testCreateTable() throws SQLException {
        // statement string
        String createTableQuery = "CREATE TABLE users (id INT PRIMARY KEY)";

        // create stament for excute
        try (Statement statement = connection.createStatement()) {
            boolean result = statement.execute(createTableQuery);
            // check error
            assertTrue(!result);
            ResultSet resultSet = connection.getMetaData().getTables(null, null, "test_table", null);
            assertTrue(resultSet.next());
        }
    }
//  error bc table delete_table doesn't exist
    @Test
    public void testDropTable() throws SQLException {
        String dropTableQuery = "DROP TABLE  delete_table";
        try (Statement statement = connection.createStatement()) {
            boolean result = statement.execute(dropTableQuery);
            assertFalse(result);
            boolean checkExist = false;
            try (Statement statement1 = connection.createStatement();
                 var resultSet = statement1.executeQuery("SHOW TABLES LIKE '" + "delete_table" + "'")) {
                checkExist = resultSet.next();
            }
            assertFalse(checkExist);
        }
    }


//  error bc category name  Iphone already exist
    @Test
    public void testInsertCategory() throws SQLException {
        // create insert new category
        String insertQuery = "INSERT INTO categories (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, "Iphone");
            int rowsAffected = preparedStatement.executeUpdate();
            assertTrue(rowsAffected > 0);

            // Kiểm tra xem bản ghi đã được thêm vào có đúng không
            String selectQuery = "SELECT * FROM categories WHERE name = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, "Iphone");
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    assertTrue(resultSet.next());
                    assertEquals("Iphone", resultSet.getString("name"));
                }
            }
        }
    }

//  bc table name category and element category_delete doesn't exist
    @Test
    public void testDropCategory() throws SQLException {
        String dropTableQuery = "DELETE FROM category WHERE name = 'category_delete';";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(dropTableQuery);
            assertTrue(rowsAffected > 0);
            boolean checkExist = false;
            String checkQuery = "SELECT * FROM categories WHERE name = 'category_delete'";
            try (Statement statementResult = connection.createStatement();
                 ResultSet resultSet = statementResult.executeQuery(checkQuery)) {
                checkExist = resultSet.next();
            }
            assertFalse(checkExist);
        }
    }

//  bc user _id == 0
    @Test
    public void testInsertOrder() throws SQLException {
        int order_id = -1; // Khai báo và khởi tạo order_id

        String insertQuery = "INSERT INTO orders (user_id, fullname, email, phone_number, address, note, order_date, status, total_money, shipping_method, shipping_address, shipping_date, tracking_number, payment_method, active, coupon_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, "buithanhson test");
            preparedStatement.setString(3, "sonbui@gmail.com");
            preparedStatement.setString(4, "0363079576");
            preparedStatement.setString(5, "dadasda");
            preparedStatement.setString(6, "");
            preparedStatement.setTimestamp(7, Timestamp.valueOf("2024-05-13 00:00:00"));
            preparedStatement.setString(8, "pending");
            preparedStatement.setDouble(9, 1827.5);
            preparedStatement.setString(10, "express");
            preparedStatement.setNull(11, Types.VARCHAR); // shipping_address
            preparedStatement.setDate(12, Date.valueOf("2024-05-13")); // shipping_date
            preparedStatement.setNull(13, Types.VARCHAR); // tracking_number
            preparedStatement.setString(14, "cod");
            preparedStatement.setInt(15, 0);
            preparedStatement.setNull(16, Types.INTEGER); // coupon_id

            int rowsAffected = preparedStatement.executeUpdate();
            assertEquals(1, rowsAffected);
        }
        String selectQuery = "SELECT * FROM orders WHERE fullname = '" + "buithanhson test" + "'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            assertTrue(resultSet.next());
        }
    }
//   bc table name orderss and fullname buithanhson_delete doesn't exist
    @Test
    public void testDeleteOrder() throws SQLException {
        String deleteQuery = "DELETE FROM orderss WHERE fullname = " + "'buithanhson_delete'";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(deleteQuery);
            assertEquals(1, rowsAffected);
        }
        String selectQuery = "SELECT * FROM orderss WHERE fullname = " + "'buithanhson_delete'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            assertFalse(resultSet.next());
        }
    }
//  bc order_id == 0
    @Test
    public void testInsertOrderDetail() throws SQLException {
        String insertQuery = "INSERT INTO order_details (order_id, product_id, price, number_of_products, total_money, color, coupon_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, 0);
            preparedStatement.setInt(2, 5812);
            preparedStatement.setDouble(3, 1234.56);
            preparedStatement.setInt(4, 1);
            preparedStatement.setDouble(5, 1234.56); // total_money
            preparedStatement.setNull(6, Types.VARCHAR); // color
            preparedStatement.setNull(7, Types.INTEGER); // coupon_id
            int rowsAffected = preparedStatement.executeUpdate();
            assertEquals(1, rowsAffected);
        }
        String selectQuery = "SELECT * FROM order_details WHERE ABS(price - 1234.56) <= 0.001";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            assertTrue(resultSet.next());
        }
    }
//  bc table name: order_detailss doesn't exist
    @Test
    public void testDeleteOrderDetail() throws SQLException {
        String sqlDelete = "DELETE FROM order_detailss WHERE ABS(price - 1234.56) <= 0.001 ";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(sqlDelete);
            assertEquals(1, rowsAffected);
        }
        String selectQuery = "SELECT * FROM order_detailss WHERE ABS(price - 1234.56) <= 0.001";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            assertFalse(resultSet.next()); // Kiểm tra rằng không còn bản ghi nào còn tồn tại sau khi xóa
        }
    }
//    bc product_id == 1 doesn't exist
    @Test
    public void testInsertProductImage() throws SQLException {
        // Thêm bản ghi vào bảng product_images
        String insertQuery = "INSERT INTO product_images (product_id, image_url) VALUES (?, ?)";
        try (PreparedStatement preparedStatementInsert = connection.prepareStatement(insertQuery)) {
            preparedStatementInsert.setInt(1, 1);
            preparedStatementInsert.setString(2, "url_test_data");
            int rowsAffected = preparedStatementInsert.executeUpdate();
            assertEquals(1, rowsAffected);
        }
    }
//   bc table name: product_imagess doesn't exist
    @Test
    public void testDeleteProductImage() throws SQLException {
        String deleteQuery = "DELETE FROM product_imagess WHERE image_url = 'url_test_data'";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(deleteQuery);
            assertEquals(1, rowsAffected);
        }
    }
//  bc id = 1 already exist
    @Test
    public void testInsertRole() throws SQLException {
        String insertQuery = "INSERT INTO roles (id, name) VALUES (?,?)";
        try (PreparedStatement preparedStatementInsert = connection.prepareStatement(insertQuery)) {
            preparedStatementInsert.setInt(1, 1);
            preparedStatementInsert.setString(2, "test_role");
            int rowsAffected = preparedStatementInsert.executeUpdate();
            assertEquals(1, rowsAffected);
        }
    }
//    bc table name roless doesn't exist
    @Test
    public void testDeleteRole() throws SQLException {
        String deleteQuery = "DELETE FROM roless WHERE id = 5";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(deleteQuery);
            assertEquals(1, rowsAffected);
        }
    }
//    bc role_id = 99 doesn't exist
    @Test
    public void testInsertUser() throws SQLException {
        // Thêm bản ghi vào bảng users
        String insertQuery = "INSERT INTO users (fullname, phone_number, address, password, created_at, updated_at, is_active, date_of_birth, facebook_account_id, google_account_id, role_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatementInsert = connection.prepareStatement(insertQuery)) {
            preparedStatementInsert.setString(1, "test_user");
            preparedStatementInsert.setString(2, "99999");
            preparedStatementInsert.setString(3, "asa");
            preparedStatementInsert.setString(4, "password"); // Password
            preparedStatementInsert.setTimestamp(5, Timestamp.from(Instant.now()));
            preparedStatementInsert.setTimestamp(6, Timestamp.from(Instant.now()));
            preparedStatementInsert.setInt(7, 1);
            preparedStatementInsert.setDate(8, Date.valueOf("2000-01-02"));
            preparedStatementInsert.setInt(9, 0);
            preparedStatementInsert.setInt(10, 0);
            preparedStatementInsert.setInt(11, 99); // Role_id

            int rowsAffected = preparedStatementInsert.executeUpdate();
            assertEquals(1, rowsAffected); // Kiểm tra rằng chỉ có một bản ghi đã được thêm vào
        }
    }
// bc name tables == userss doesn't exist
    @Test
    public void testDeleteUser() throws SQLException {
        // Xóa bản ghi từ bảng users
        String deleteQuery = "DELETE FROM userss WHERE fullname = 'test_user'";
        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(deleteQuery);
            assertEquals(1, rowsAffected); // Kiểm tra rằng chỉ có một bản ghi đã bị xóa
        }
    }



}

