package com.cj.cn.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String objToStr(Object object) {
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

    public static Object strToObj(String objJson, Class<?> clazz) {
        if (clazz == String.class) {
            return objJson;
        }
        Object obj = null;
        try {
            obj = mapper.readValue(objJson, clazz);
        } catch (JsonProcessingException e) {
            log.info("{}进行JSON反序列化时发生异常, {}", objJson, e);
        }
        return obj;
    }
}
