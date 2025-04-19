package com.example.hotels.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private final static String URL= "jdbc:mysql://127.0.0.1:3306/mydatabase";
    private final static String USER="root";
    private final static String PSW="";
    private Connection connection;

    private static MyDataBase instance;

    private MyDataBase() {
        try {
            connection = DriverManager.getConnection(URL,USER,PSW);
            System.out.println("Connected!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
