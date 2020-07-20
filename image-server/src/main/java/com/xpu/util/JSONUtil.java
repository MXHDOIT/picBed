package com.xpu.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JSONUtil {
    //单例模式
    private static volatile ObjectMapper MAPPER;

    public static ObjectMapper get(){ //双重校验锁
        if(MAPPER == null){
            synchronized (JSONUtil.class){
                if(MAPPER == null){
                    MAPPER = new ObjectMapper();
                }
            }
        }
        return MAPPER;
    }

    //Java对象转化为JSON格式
    public static String serialize(Object o){
        try {
            return get().writeValueAsString(o);
        } catch (JsonProcessingException e) { //编译时异常转换为运行时，编译不处理
            throw new RuntimeException("JSON序列化失败，对象为"+o,e);
        }
    }

    //JSON格式转化为JAVA对象
    public static <T> T deserialize(String json,Class<T> clazz){
        try {
            return get().readValue(json,clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON反序列化失败，对象为"+json,e);
        }
    }

    public static <T> T deserialize(InputStream is, Class<T> clazz){
        try {
            return get().readValue(is,clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON反序列化失败，对象为"+is,e);
        }
    }
}
