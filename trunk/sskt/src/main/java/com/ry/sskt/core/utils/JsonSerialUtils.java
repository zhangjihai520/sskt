package com.ry.sskt.core.utils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;

public class JsonSerialUtils<T> {
    public T deserialize(byte[] data) {
        if (data == null) {
            return null;
        }
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return JSON.parseObject(data, entityClass);
    }

    public byte[] serialize(T data) {
        if (data == null) {
            return null;
        }
        return JSON.toJSONString(data).getBytes();
    }
}
