package com.cj.cn.util;

import com.cj.cn.pojo.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);   //忽略空对象转json的错误
    }

    public static <T> String objToStr(T object) {
        if (object.getClass() == String.class) {
            return (String) object;
        }
        String objJson = "";
        try {
            objJson = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.info("{}进行JSON序列化时发生异常, {}", object.toString(), e);
        }
        return objJson;
    }

    public static <T> T strToObj(String objJson, Class<T> clazz) {
        if (clazz == String.class) {
            return (T) objJson;
        }
        T obj = null;
        try {
            obj = mapper.readValue(objJson, clazz);
        } catch (JsonProcessingException e) {
            log.info("{}进行JSON反序列化时发生异常, {}", objJson, e);
        }
        return obj;
    }

    public static <T> T strToObj(String objJson, Class<?> collectionClass, Class<?>... elementClass) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        T obj = null;
        try {
            obj = mapper.readValue(objJson, javaType);
        } catch (JsonProcessingException e) {
            log.info("{}进行JSON反序列化时发生异常, {}", objJson, e);
        }
        return obj;
    }

    public static void main(String[] args) {
        List<User> list1 = new ArrayList<>();
        list1.add(new User().setId(1l).setNickname("大明"));
        list1.add(new User().setId(2l).setNickname("小明"));
        String str = JsonUtil.objToStr(list1);
        System.out.println(str);
        List<User> o = strToObj(str, List.class, User.class);
        System.out.println(o);
    }
}
