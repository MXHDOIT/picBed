package com.xpu.servlet;

import com.xpu.dao.ImageDao;
import com.xpu.dao.Result;
import com.xpu.model.Image;
import com.xpu.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/imageShow")
public class ImageShowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取数据
        String imageId = request.getParameter("imageId");

        if (imageId == null || imageId.equals("")){
            Result result = new Result();
            result.setFlag(false);
            result.setMessage("没有选择图片");
            response.setContentType("application/json;charset=utf8");
            response.getWriter().write(JSONUtil.serialize(result));
        }

        //2.调用数据库
        ImageDao imageDao = new ImageDao();
        Image image = imageDao.queryByImageId(Integer.parseInt(imageId));
        if (image == null){
            Result result = new Result();
            result.setFlag(false);
            result.setMessage("没有该图片");
            response.setContentType("application/json;charset=utf8");
            response.getWriter().write(JSONUtil.serialize(result));
        }

        //设置返回数据类型并且返回响应数据
        response.setContentType(image.getContentType());
        File file = new File(image.getPath());
        //由于图片是二进制文件，应该使用字节流的形式读文件
        ServletOutputStream outputStream = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];

        while (true){
            int len = fileInputStream.read(buffer);
            if (len == -1){
                break;
            }
            outputStream.write(buffer);
        }
        fileInputStream.close();
        outputStream.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
