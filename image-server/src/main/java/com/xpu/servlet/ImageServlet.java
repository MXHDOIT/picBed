package com.xpu.servlet;

import com.xpu.dao.ImageDao;
import com.xpu.dao.Result;
import com.xpu.model.Image;
import com.xpu.model.User;
import com.xpu.util.JSONUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {

    //新增图片
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取图片的属性信息，存入数据库
        //1.1 创建factory和upload对象，为获取到图片信息做准备工作
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fileItems = null;
        //获取图片属性存入数据库
        try {
            fileItems = upload.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
            response.setContentType("application/json;charset=utf8");
            Result result = new Result();
            result.setFlag(false);
            result.setMessage("文件解析错误");
            response.getWriter().write(JSONUtil.serialize(request));
        }
        //获取图片属性存入数据库
        FileItem fileItem = fileItems.get(0);
        //封装图片属性
        Image image = new Image();
        image.setImageName(fileItem.getName());
        image.setSize((int) fileItem.getSize());
        image.setUploadTime(new SimpleDateFormat().format(new Date()));
        image.setContentType(fileItem.getContentType());
        //md5：主要用于解决存储重复
        image.setMd5(DigestUtils.md5Hex(fileItem.get()));
        image.setPath("./image/"+image.getMd5());
        //从session种获取用户id
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        image.setUserId(user.getUserId());


        //插入数据库，同时利用MD5查询是否已存在
        ImageDao imageDao = new ImageDao();
        int md5Num = imageDao.queryMd5(image.getMd5());
        imageDao.insert(image);

        //2.上传图片
        if(md5Num == 0){
            File file = new File(image.getPath());
            if (!file.exists()){
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            try {
                fileItem.write(file);
            } catch (Exception e) {
                e.printStackTrace();
                response.setContentType("application/json;charset=utf8");
                Result result = new Result();
                result.setFlag(false);
                result.setMessage("图片内容上传错误");
                response.getWriter().write(JSONUtil.serialize(request));
            }
        }

        //3.返回结果
        response.setContentType("application/json;charset=utf8");
        Result result = new Result();
        result.setFlag(true);
        response.getWriter().write(JSONUtil.serialize(result));
    }

    //查看所有图片信息/具体图片信息
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //考虑所有图片信息或指定属性
        //获取请求参数:如果有这是指定属性，没有则是所有图片信息
        String imageId = request.getParameter("imageId");
        //从session中获取用户id
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        //从数据库获取数据
        ImageDao imageDao = new ImageDao();
        String res = null;
        if (imageId == null || imageId.equals("")){
            List<Image> images = imageDao.queryById(user.getUserId());
            res = JSONUtil.serialize(images);
        }else{
            Image image = imageDao.queryByImageId(Integer.parseInt(imageId));
            res = JSONUtil.serialize(image);
        }

        //返回数据
        response.setContentType("application/json;charset=utf8");
        response.getWriter().write(res);
    }

    //删除具体的图片
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取数据
        String imageId = request.getParameter("imageId");

        //删除数据库中的数据
        ImageDao imageDao = new ImageDao();
        Image image = imageDao.queryByImageId(Integer.parseInt(imageId));
        imageDao.delete(Integer.parseInt(imageId));  //删除数据库
        int md5Num = imageDao.queryMd5(image.getMd5());

        //如果数据库中没有该图片信息则从磁盘删除图片
        if(md5Num == 0){
            File file = new File(image.getPath());
            file.delete();
        }

        //返回值
        Result result = new Result();
        result.setFlag(true);
        response.setContentType("application/json;charset=utf8");
        response.getWriter().write(JSONUtil.serialize(result));
    }
}
