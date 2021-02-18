package com.ry.sskt.core.utils;

import com.ry.sskt.model.common.constant.CommonConst;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * 3DES加密解密工具类
 *
 * @author 幸仁强
 * @createtime 2020-4-16
 */
public class CryptogramHelper {
    private static final String DESEDE = "desede";

    // 3DESECB加密,key必须是长度大于等于 3*8 = 24 位哈
    public static String Encrypt3DES(final String src, final String key)
            throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] reKey = promotion16To24(md5.digest(key.getBytes("UTF-8")));
        final DESedeKeySpec dks = new DESedeKeySpec(reKey);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DESEDE);
        final SecretKey securekey = keyFactory.generateSecret(dks);
        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        final byte[] b = cipher.doFinal(src.getBytes("UTF-8"));
        String str = new String(Base64.encodeBase64(b));
        return str.replaceAll("\r", "").replaceAll("\n", "");
    }

    // 3DESECB解密,key必须是长度大于等于 3*8 = 24 位哈
    public static String Decrypt3DES(final String src, final String key)
            throws Exception {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] reKey = promotion16To24(md5.digest(key.getBytes("UTF-8")));
            final byte[] bytesrc = Base64.decodeBase64(src);
            // --解密的key
            final DESedeKeySpec dks = new DESedeKeySpec(reKey);
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DESEDE);
            final SecretKey securekey = keyFactory.generateSecret(dks);
            // --Chipher对象解密
            final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            final byte[] retByte = cipher.doFinal(bytesrc);
            return new String(retByte, "UTF-8");
        } catch (Exception e) {
            return src;
        }
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMap(Object obj)
            throws IllegalAccessException {
        Map<String, String> map = new HashMap<String, String>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if (StringUtils.isBlank(String.valueOf(value))) {
                continue;
            }
            map.put(fieldName, String.valueOf(value));
        }
        return map;
    }

    /**
     * 实体类转换成map
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, String> beanToMap(T bean) {
        Map<String, String> map = new HashMap<String, String>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if (null == beanMap.get(key)) {
                    continue;
                }
                map.put(key + "", String.valueOf(beanMap.get(key)));
            }
        }
        return map;
    }

    /**
     * 方法描述:签名字符串
     *
     * @param params 需要签名的参数
     * @param secret 签名密钥
     * @return
     */
    public static String sign(Map<String, String> params, String secret) {
        StringBuilder valueSb = new StringBuilder();
        // 将参数以参数名的字典升序排序
        Map<String, String> sortParams = new TreeMap<String, String>(params);
        Set<Entry<String, String>> entrys = sortParams.entrySet();
        // 遍历排序的字典,并拼接key1=value1&key2=value2......格式
        for (Entry<String, String> entry : entrys) {
            valueSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        valueSb.append("key=").append(secret);
        return md5(valueSb.toString());
    }

    /**
     * 方法描述:将字符串MD5加码 生成32位md5码
     *
     * @param inStr
     * @return
     */
    public static String md5(String inStr) {
        try {
            return DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误", e);
        }
    }

    public static int[] ConvertCSharpByteToJavaByte(byte[] bytes) {
        int data[] = new int[bytes.length];

        for (int i = 0; i < bytes.length; i++) {

            data[i] = bytes[i] & 0xff;
        }
        return data;
    }


    /**
     * 将3DES的16位秘钥升级为24位秘钥
     *
     * @param inputKey 16位秘钥
     * @return 24位秘钥
     * @throws IllegalArgumentException
     */
    static byte[] promotion16To24(byte[] inputKey)
            throws IllegalArgumentException {
        if (inputKey == null || inputKey.length != 16) {
            throw new IllegalArgumentException("input error");
        }
        byte[] outputKey = new byte[24];
        System.arraycopy(inputKey, 0, outputKey, 0, 16);
        System.arraycopy(inputKey, 0, outputKey, 16, 8);
        return outputKey;
    }

    public static void main(String[] args)
            throws Exception {

        String url = "rtmp://59.55.140.152:1935/live/R69_zb0002_1";
        System.out.println(String.format("https://%s:%s%s%s", CommonConst.DOMAIN,CommonConst.AVCON_HLS_PROT_8299,url.substring(url.indexOf("/live/")).replace("/live/","/hls/"),CommonConst.PINGOS_HLS_STUFF));
    }
}
