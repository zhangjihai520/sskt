package com.ry.sskt.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP工具类
 * @author 幸仁强
 * @createtime 2018-2-12
 */
@Component
@Slf4j
public final class GZipUtil {
	/**
	 * gzip压缩
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	/**public static byte[] compress(byte[] bytes) throws IOException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		// 压缩后的输出流
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 压缩
		GZIPOutputStream gzip = new GZIPOutputStream(os);
		gzip.write(bytes);
		gzip.close();
		return os.toByteArray();
	}**/

	/**
	 * gzip解压
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	/**public static byte[] uncompress(byte[] bytes) throws IOException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		GZIPInputStream ungzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = ungzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}**/
}
