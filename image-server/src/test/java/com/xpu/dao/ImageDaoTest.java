package com.xpu.dao;

import com.xpu.model.Image;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class ImageDaoTest {
    ImageDao imageDao = new ImageDao();

    //测试照片插入
    @Test
    public void fInsert(){

        Image image = new Image();
        image.setImageName("证件照");
        image.setSize(455);
        image.setUploadTime(String.valueOf(new Date()));
        image.setContentType("image/png");
        image.setPath("../image/"+image.getImageName());
        image.setMd5("dsa54564fadfd");
        image.setUserId(3);

        imageDao.insert(image);
    }

    //测试
    @Test
    public void fQueryById(){
        List<Image> images = imageDao.queryById(1);
        for (Image image :
                images) {
            System.out.println(image);
        }
    }

    //测试全部照片查询
    @Test
    public void query(){
        List<Image> images = imageDao.queryAll();
        for (Image image:images) {
            System.out.println(image);
        }
    }

    //测试照片信息删除
    @Test
    public void delete(){
        imageDao.delete(1);
    }

    //查询md5相同的图片数
    @Test
    public void queryMd5(){
        int queryMd5 = imageDao.queryMd5("dsa54564fadfd");
        System.out.println(queryMd5);
    }
}
