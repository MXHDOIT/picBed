package com.xpu.servlet;

import com.xpu.dao.Result;
import com.xpu.dao.UserDao;
import com.xpu.model.User;
import com.xpu.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        //获取数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //封装user
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);

        //查询数据库
        UserDao userDao = new UserDao();
        User user1 = userDao.query(user);
        //封装返回数据
        Result result = new Result();
        if (user1 != null){
            result.setFlag(true);
            result.setMessage("用户登录成功");
            //建立session
            HttpSession session = request.getSession();
            session.setAttribute("user",user1);
        }else{
            result.setFlag(false);
            result.setMessage("账号或密码错误");
        }

        //返回数据
        response.getWriter().write(JSONUtil.serialize(result));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
