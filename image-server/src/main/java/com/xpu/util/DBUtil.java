package com.xpu.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库工具类
 */
public class DBUtil {
    //数据库连接信息
    private final static String URL = "jdbc:mysql://localhost:3306/image_server";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";

    public volatile static DataSource dataSource;

    /**
     * 获取连接池
     * @return
     */
    private static DataSource getDataSource(){
        if (dataSource == null){
            synchronized (DBUtil.class){
                if (dataSource == null){
                    dataSource = new MysqlDataSource();
                    ((MysqlDataSource) dataSource).setURL(URL);
                    ((MysqlDataSource) dataSource).setUser(USERNAME);
                    ((MysqlDataSource) dataSource).setPassword(PASSWORD);
                }
            }
        }
        return dataSource;
    }

    //获取连接
    public static Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (SQLException throwables) {
            throw new ImageServerException("获取连接出现异常");
        }
    }

    //关闭连接：注意顺序
    public static void close(Connection c, PreparedStatement p, ResultSet r){
        try {
            if (r != null){
                r.close();
            }

            if (p != null){
                p.close();
            }

            if (c != null){
                c.close();
            }
        } catch (SQLException throwables) {
            throw new ImageServerException("关闭连接出现异常");
        }
    }

    public static void close(Connection c, PreparedStatement p){
        close(c, p,null);
    }
}
