package com.ry.sskt.core.annotation;

import com.couchbase.client.java.document.AbstractDocument;

import java.io.IOException;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: Redis缓存对象运算接口
 */
public interface IRedisOperationObject<T> {
     boolean operation(byte[] objByte) throws IOException;
   // <T extends AbstractDocument<byte[]>> boolean operation(T t) throws IOException;
}
