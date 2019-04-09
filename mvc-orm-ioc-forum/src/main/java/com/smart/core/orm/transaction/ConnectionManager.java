package com.smart.core.orm.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static ThreadLocal<Connection> threadLocalConn=new ThreadLocal<>();

    public static String driver;
    public static String url;
    public static String user;
    public static String password;

    public static Connection getConnection() throws SQLException {
        Connection conn= threadLocalConn.get();
        if(conn==null) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            conn = DriverManager.getConnection(url, user, password);
            threadLocalConn.set(conn);
            return conn;
        }else {
            return conn;
        }

    }

    public static void beginTransaction(){//这四个方法连同上面的getConnection() 异常的处理
        try {
            getConnection().setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void commit() {
        try {
            getConnection().commit();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void rollback(){
        try {
            getConnection().rollback();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void close(){
        try {
            Connection conn = getConnection();
            conn.close();
            conn = null;
            threadLocalConn.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
