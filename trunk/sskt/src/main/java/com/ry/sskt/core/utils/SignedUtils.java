package com.ry.sskt.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;
import java.util.Map.Entry;

/**
 * 签名工具类
 *
 * @author 幸仁强
 * @ClassName: SignedHelper
 * @date 2018年12月17日
 */
public class SignedUtils {

    public static String StandardMd5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5=new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return fillMD5(md5);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误:"+e.getMessage(),e);
        }
    }
    private static String fillMD5(String md5){
        return md5.length()==32?md5:fillMD5("0"+md5);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    // 十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public static <T> String GetRequestSign(String token, T request)
            throws Exception {
        Map<String, String> propertiesSet = new HashMap<String, String>();

        GetPropertiesSet(propertiesSet, request);
        StringBuilder valueSb = new StringBuilder();
        // 将参数以参数名的字典升序排序
        Map<String, String> sortParams = new TreeMap<String, String>(propertiesSet);
        Set<Entry<String, String>> entrys = sortParams.entrySet();
        // 遍历排序的字典,并拼接key1=value1&key2=value2......格式
        for (Entry<String, String> entry : entrys) {
            valueSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        valueSb.append("key=").append(token);
        return md5(valueSb.toString());
    }

    public static <T> void GetPropertiesSet(Map<String, String> propertiesSet, T request)
            throws Exception,
            IllegalAccessException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(request.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors.length == 0) {
                return;
            }
            for (PropertyDescriptor property : propertyDescriptors) {
                Class<?> propertyType = property.getPropertyType();
                String typeStr = propertyType.toGenericString();
                String key = property.getName();
                Method getter = property.getReadMethod();
                Object value = getter.invoke(request);
                if (ObjectUtils.isEmpty(value) || StringUtils.isBlank(value.toString()) || "sign".equalsIgnoreCase(key) || "class".equalsIgnoreCase(key) || propertyType.isArray()
                        || typeStr.contains("List")) {
                    continue;
                }
                if (typeStr.contains("public class")) {
                    GetPropertiesSet(propertiesSet, value);
                } else {
                    if ("double".equalsIgnoreCase(typeStr)) {
                        value = doubleTrans(Double.parseDouble(value.toString()));
                    }
                    propertiesSet.put(property.getName().toLowerCase(), value.toString());
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Double类型需要特殊处理，double类型格式可能会不一致，如java是100.0，C#为100
     *
     * @param d
     * @return
     * @Title: doubleTrans
     * @author 幸仁强
     */
    public static String doubleTrans(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }

    /**
     * MD5 加密，返回小写字符串
     *
     * @param decript 需要加密字符串
     * @return 返回40位小写字符串
     * @throws Exception
     * @Title: MD5
     * @author 幸仁强
     */
    public static String md5(String decript)
            throws Exception {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
            byte[] results = md5.digest(decript.getBytes("UTF-8"));
            // 将得到的字节数组变成字符串返回
            String result = byteArrayToHexString(results);
            return result;
        } catch (Exception e) {
            throw new Exception("MD5加密出错：" + e.getMessage());
        }
    }

    /**
     * 轮换字节数组为十六进制字符串
     *
     * @param b 字节数组
     * @return 十六进制字符串
     * @author 幸仁强
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     *
     * @param b
     * @return
     * @Title: byteToHexString
     * @author 幸仁强
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
