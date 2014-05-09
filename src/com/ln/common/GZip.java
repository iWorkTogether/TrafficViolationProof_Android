package com.ln.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


public class GZip {
	public static byte[] getGZipCompressed(byte[] byteData) {
		byte[] compressed = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(
					byteData.length);
			Deflater compressor = new Deflater();
			compressor.setLevel(Deflater.BEST_COMPRESSION); // 将当前压缩级别设置为指定值�?
			compressor.setInput(byteData, 0, byteData.length);
			compressor.finish(); // 调用时，指示压缩应当以输入缓冲区的当前内容结尾�?

			// Compress the data
			final byte[] buf = new byte[1024];
			while (!compressor.finished()) {
				int count = compressor.deflate(buf);
				bos.write(buf, 0, count);
			}
			compressor.end(); // 关闭解压缩器并放弃所有未处理的输入�?
			compressed = bos.toByteArray();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return compressed;
	}

	public static byte[] getGZipUncompress(byte[] data) throws IOException {
		byte[] unCompressed = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		Inflater decompressor = new Inflater();
		try {
			decompressor.setInput(data);
			final byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			}

			unCompressed = bos.toByteArray();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			decompressor.end();
		}
		// String test = bos.toString();
		return unCompressed;
	}
}
