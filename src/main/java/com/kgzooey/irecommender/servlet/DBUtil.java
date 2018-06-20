package com.kgzooey.irecommender.servlet;

import java.sql.*;

public class DBUtil {
    /*
     * 声明数据库信息
     */
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String DB_NAME = "irecommender";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    /*
     * 声明JDBC对象
     */
    protected static Connection connection = null;
    protected static Statement statement = null;
    protected static PreparedStatement preparedStatement = null;
    protected static ResultSet resultSet = null;

    /*
     * 创建数据库连接
     */
    public static synchronized Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String con = URL + "/" + DB_NAME+"?"+"user="+USER+"&password="+PASSWORD+"&characterEncoding="+"utf-8";
            connection = DriverManager.getConnection(con + "&serverTimezone=CTT&useUnicode=true&allowMultiQueries=true");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /*
     * 执行 INSERT,UPDATE,DELETE语句
     *
     * @param sql SQL语句,字符串类型
     *
     * @return 执行结果 int类型
     */
    public static int executeUpdata(String sql) {
        int result = 0;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            result = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * 执行 SELECT语句
     *
     * @param sql SQL语句，字符串类型
     *
     * @return ResultSet 结果集
     */
    public static ResultSet executeQuery(String sql) {

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /*
     * 执行动态SQL语句
     *
     * @param sql 含有参数的动态SQL语句
     *
     * @return 返回PreparedStatement对象
     */
    public static PreparedStatement PrepareStatement(String sql) {
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    /*
     * 关闭数据库连接对象
     */
    public static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
