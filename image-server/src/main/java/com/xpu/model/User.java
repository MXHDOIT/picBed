package com.xpu.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/***
 * 用户实体类
 */
@Setter
@Getter
@ToString
public class User {

    private Integer userId;
    private String userName;
    private String password;
}
