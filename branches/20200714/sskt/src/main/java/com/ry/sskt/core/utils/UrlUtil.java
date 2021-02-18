package com.ry.sskt.core.utils;

import com.ry.sskt.model.common.constant.CommonConst;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * URL转码工具类
 *
 * @author 幸仁强
 * @ClassName: UrlUtil
 * @date 2020年4月16日
 */
public class UrlUtil {
    private final static String ENCODE = "UTF-8";

    private static final Logger log = LoggerFactory.getLogger(UrlUtil.class);

    /**
     * URL 解码
     *
     * @param str
     * @return
     * @Title: getURLDecoderString
     * @author 幸仁强
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE).replace(" ", "+");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL 转码
     *
     * @param str
     * @return
     * @Title: getURLEncoderString
     * @author 幸仁强
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * ID加密
     *
     * @return
     * @throws Exception
     * @Title: encrypt
     * @author 幸仁强
     */
    public static String encrypt(Object source) {
        try {
            return getURLEncoderString(CryptogramHelper.Encrypt3DES(source.toString(), CommonConst.IDDESKEY));
        } catch (Exception e) {
            log.error(String.format("ID为%s加密失败", source), e);
            return "";

        }
    }

    /// <summary>
    /// url加密
    /// </summary>
    /// <param name="source"></param>
    /// <param name="key"></param>
    /// <returns></returns>
    public static String encrypt(Object source, String key) {
        try {
            return getURLEncoderString(CryptogramHelper.Encrypt3DES(source.toString(), key));
        } catch (Exception e) {
            log.error(String.format("ID为%s加密失败", source), e);
            return "";

        }
    }

    /**
     * ID解密
     *
     * @param <T>
     * @param idEncoderStr
     * @return
     * @throws Exception
     * @Title: decrypt
     * @author 幸仁强
     */
    public static <T> T decrypt(final String idEncoderStr, final Class<T> clazz) {
        try {
            String idStr = CryptogramHelper.Decrypt3DES(getURLDecoderString(idEncoderStr), CommonConst.IDDESKEY);

            return (T) ConvertUtils.convert(idStr, clazz);
        } catch (Exception e) {
            log.error(String.format("密文为%s的ID解密失败", idEncoderStr), e);
            return (T) ConvertUtils.convert("0", clazz);
        }
    }

    /**
     * ID解密
     *
     * @param <T>
     * @param idEncoderStr
     * @return
     * @throws Exception
     * @Title: decrypt
     * @author 幸仁强
     */
    public static <T> T decrypt(final String idEncoderStr, String key, final Class<T> clazz) {
        try {
            String idStr = CryptogramHelper.Decrypt3DES(getURLDecoderString(idEncoderStr), key);
            return (T) ConvertUtils.convert(idStr, clazz);
        } catch (Exception e) {
            log.error(String.format("密文为%s的ID解密失败", idEncoderStr), e);
            return (T) ConvertUtils.convert("0", clazz);
        }
    }

    public static void main(String[] args)
            throws Exception {
        // System.out.println(encrypt(102));
        // System.out.println(encrypt(6));
         System.out.println(decrypt("q%2BA2KX51Fq0%3D",Integer.class));
        // System.out.println(decrypt("pOXHW79ZABE%3D",Integer.class));
        //  System.out.println(decrypt("PVeJVC6q7Yo%3D",Integer.class));
        //  System.out.println(decrypt("c9azy6ZAGrQ%3D",Integer.class));
    }

}
