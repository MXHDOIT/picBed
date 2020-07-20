package com.xpu.dao;

import com.xpu.model.Image;
import com.xpu.util.DBUtil;
import com.xpu.util.ImageServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDao {

    //1.新增图片信息(上传图片)
    public void insert(Image image){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "insert into image values(null ,?,?,?,?,?,?,?)";
        PreparedStatement p = null;

        try {
            p = c.prepareStatement(sql);
            p.setString(1,image.getImageName());
            p.setInt(2,image.getSize());
            p.setString(3,image.getUploadTime());
            p.setString(4,image.getContentType());
            p.setString(5,image.getPath());
            p.setString(6,image.getMd5());
            p.setInt(7,image.getUserId());
            //3.执行SQL
            int res = p.executeUpdate();
            //4.处理结果集
            if (res != 1){
                throw new ImageServerException("图片新增数据库层出现异常");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ImageServerException("图片新增数据库层出现异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p);
        }
    }

    //2.删除图片信息(删除图片: 利用图片id)
    public void delete(int imageId){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "delete from image where imageId = ? ";
        PreparedStatement p = null;

        try {
            p = c.prepareStatement(sql);
            p.setInt(1,imageId);
            //3.执行SQL
            int res = p.executeUpdate();
            //4.处理结果集
            if (res != 1){
                throw new ImageServerException("图片删除数据库层出现异常");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ImageServerException("图片删除数据库层出现异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p);
        }
    }

    //3.查询图片信息(用户查询：利用用户id)
    public List<Image> queryById(int userId){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "select * from image where userId = ?";
        PreparedStatement p = null;
        ResultSet r = null;
        List<Image> res = new ArrayList<>();
        try {
            p = c.prepareStatement(sql);
            p.setInt(1,userId);
            //3.执行SQL
            r = p.executeQuery();
            //4.处理结果集
            while (r.next()){
                Image image = new Image();
                image.setImageId(r.getInt("imageId"));
                image.setImageName(r.getString("imageName"));
                image.setSize(r.getInt("size"));
                image.setUploadTime(r.getString("uploadTime"));
                image.setContentType(r.getString("contentType"));
                image.setPath(r.getString("path"));
                image.setMd5(r.getString("md5"));
                image.setUserId(r.getInt("userId"));

                res.add(image);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ImageServerException("图片利用用户ID查询数据库层出现异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p,r);
        }
        return res;
    }

    //4.查询图片信息(用户查询：利用图片id)
    public Image queryByImageId(int imageId){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "select * from image where imageId = ?";
        PreparedStatement p = null;
        ResultSet r = null;
        try {
            p = c.prepareStatement(sql);
            p.setInt(1,imageId);
            //3.执行SQL
            r = p.executeQuery();
            //4.处理结果集
            if (r.next()){
                Image image = new Image();
                image.setImageId(r.getInt("imageId"));
                image.setImageName(r.getString("imageName"));
                image.setSize(r.getInt("size"));
                image.setUploadTime(r.getString("uploadTime"));
                image.setContentType(r.getString("contentType"));
                image.setPath(r.getString("path"));
                image.setMd5(r.getString("md5"));
                image.setUserId(r.getInt("userId"));

                return image;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ImageServerException("图片利用图片ID查询数据库层出现异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p,r);
        }
        return null;
    }

    //5.查询图片信息(所有图片)
    public List<Image> queryAll(){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "select * from image";
        PreparedStatement p = null;
        ResultSet r = null;
        List<Image> res = new ArrayList<>();
        try {
            p = c.prepareStatement(sql);
            //3.执行SQL
            r = p.executeQuery();
            //4.处理结果集
            while (r.next()){
                Image image = new Image();
                image.setImageId(r.getInt("imageId"));
                image.setImageName(r.getString("imageName"));
                image.setSize(r.getInt("size"));
                image.setUploadTime(r.getString("uploadTime"));
                image.setContentType(r.getString("contentType"));
                image.setPath(r.getString("path"));
                image.setMd5(r.getString("md5"));
                image.setUserId(r.getInt("userId"));

                res.add(image);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ImageServerException("图片查询数据库层出现异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p,r);
        }
        return res;
    }

    //6.查询md5相同的图片数(图片删除，添加IO)
    public int queryMd5(String md5){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "select count(userId) count from image where md5 = ?";
        PreparedStatement p = null;
        ResultSet r = null;
        List<Image> res = new ArrayList<>();
        try {
            p = c.prepareStatement(sql);
            p.setString(1,md5);
            //3.执行SQL
            r = p.executeQuery();
            //4.处理结果集
            if (r.next()){
                return r.getInt("count");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ImageServerException("图片利用用户ID查询数据库层出现异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p,r);
        }
        return 0;
    }
}
