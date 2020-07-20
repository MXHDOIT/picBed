package com.xpu.dao;

import com.xpu.model.User;
import com.xpu.util.DBUtil;
import com.xpu.util.ImageServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    //1.插入数据(新增用户)
    public void insert(User user){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "insert into user values(null,?,?)";
        PreparedStatement p = null;

        try {
            p = c.prepareStatement(sql);
            p.setString(1,user.getUserName());
            p.setString(2,user.getPassword());
            //3.执行SQL
            int res = p.executeUpdate();
            //4.处理结果集
            if (res != 1){
                throw new ImageServerException("用户新增数据库层出现异常");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ImageServerException("用户新增数据库层出现异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p);
        }
    }

    //2.查询数据(登录)
    public User query(User user1){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "select * from user where username = ? and password = ?";
        PreparedStatement p = null;
        ResultSet r = null;
        try {
            p = c.prepareStatement(sql);
            p.setString(1,user1.getUserName());
            p.setString(2,user1.getPassword());
            //3.执行SQL
            r = p.executeQuery();
            //4.处理结果集
            if (r.next()){
                User user = new User();
                user.setUserId(r.getInt("userId"));
                user.setUserName(r.getString("userName"));
                user.setPassword(r.getString("password"));

                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ImageServerException("用户新增数据库层出现异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p);
        }

        return null;
    }
}
