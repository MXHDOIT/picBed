package com.xpu.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Image {

    private Integer imageId;
    private String imageName;
    private Integer size;
    private String uploadTime;
    private String contentType;
    private String path;
    private String md5;
    private Integer userId;
}
