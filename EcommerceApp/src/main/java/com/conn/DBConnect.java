package com.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnect 
{
    private static Connection conn = null;

    public static Connection getConn()
    {
        try {
            if (conn == null || conn.isClosed())
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Yash/Videos/EcommerceApp/EcommerceApp/mydatabase.db");
                conn.setAutoCommit(true);
                // Enable WAL mode to prevent database locking
                Statement st = conn.createStatement();
                st.execute("PRAGMA journal_mode=WAL");
                st.execute("PRAGMA busy_timeout=5000");
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}