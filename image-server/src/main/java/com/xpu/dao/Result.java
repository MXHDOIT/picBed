package com.xpu.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Result {

    private boolean flag;
    private String message;
    private Object object;
}
