package com.xpu.dao;

import com.xpu.model.User;
import org.junit.Test;

import java.util.Date;

public class UserDaoTest {

    /**
     * 测试用户注册
     */
    @Test
    public void fInsert(){
        UserDao userDao = new UserDao();
        User user = new User();
        user.setUserName("mxh"+new Date());
        user.setPassword("123");
        userDao.insert(user);
    }

    /**
     * 测试用户登录
     */
    @Test
    public void fQuery(){
        UserDao userDao = new UserDao();
        User user = new User();
        user.setUserName("mxh");
        user.setPassword("123");
        User query = userDao.query(user);
        System.out.println(query);
    }
}
