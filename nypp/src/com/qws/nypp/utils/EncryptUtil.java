package com.qws.nypp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

/**
 * 加密工具，对称加密算法，采用随机Key
 * 
 * 
 * @Description TODO
 * @author CodeApe
 * @version 1.0
 * @date 2013-04-02
 * @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 * 
 */
public class EncryptUtil {
	/** 解密是否成功的校验码 */
	private static String CHECK_CODE = "http:\\wwww.tentinet.com";

	/**
	 * 
	 * @updateTime 2015-6-22,下午3:40:26
	 * @updateAuthor qw
	 * @param key 私钥
	 * @param plaintext 需要加密的明文
	 * @return
	 */
	public static String Encrypt(String key, String plaintext) {
		if (TextUtils.isEmpty(plaintext)) {// 如果明文为空
			return "";
		}
		StringBuffer buf = new StringBuffer();
		key = CHECK_CODE + key;// 私钥加上校验位
		plaintext = CHECK_CODE + plaintext; // 明文加上效验位
		byte[] keys = md5Encrypt(key).getBytes(); // 32位MD5私钥
		for (int i = 0; i < plaintext.length(); i++) {
			buf.append((char) (plaintext.charAt(i) ^ keys[i % 32]));
		}
		String ciphertext = buf.toString();
		return ciphertext;
	}

	/**
	 * 解密密文
	 * 
	 * @version 1.0
	 * @createTime 2013-10-14,上午10:39:41
	 * @updateTime 2013-10-14,上午10:39:41
	 * @createAuthor 罗文忠
	 * @updateAuthor 罗文忠
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param key 私钥
	 * @param ciphertext 需要解密的密文
	 * @return plaintext 解密后的明文
	 */
	public static String DEcrypt(String key, String ciphertext) {

		if (TextUtils.isEmpty(ciphertext)) {// 如果密文为空
			return "";
		}

		StringBuffer buf = new StringBuffer();

		key = CHECK_CODE + key;// 私钥加上校验位
		byte[] keys = md5Encrypt(key).getBytes(); // 32位MD5私钥
		for (int i = 0; i < ciphertext.length(); i++) {
			buf.append((char) (ciphertext.charAt(i) ^ keys[i % 32]));
		}
		String plaintext = buf.toString(); // 解密后的明文
		if (!plaintext.startsWith(CHECK_CODE)) {// 解密失败
			plaintext = "";
		} else {
			plaintext = plaintext.substring(CHECK_CODE.length()); // 解密成功，去除校验位
		}
		return plaintext;

	}

	/**
	 * 解密密文(使用MD5私钥解密)
	 * 
	 * @version 1.0
	 * @createTime 2013-10-14,上午10:39:41
	 * @updateTime 2013-10-14,上午10:39:41
	 * @createAuthor 罗文忠
	 * @updateAuthor 罗文忠
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param md5Key md5私钥
	 * @param ciphertext 需要解密的密文
	 * @return plaintext 解密后的明文
	 */
	public static String DEcryptForMd5Key(String md5Key, String ciphertext) {

		StringBuffer buf = new StringBuffer();

		byte[] keys = md5Key.getBytes(); // 32位MD5私钥
		for (int i = 0; i < ciphertext.length(); i++) {
			buf.append((char) (ciphertext.charAt(i) ^ keys[i % 32]));
		}
		String plaintext = buf.toString(); // 解密后的明文
		if (!plaintext.startsWith(CHECK_CODE)) {// 解密失败
			plaintext = "";
		} else {
			plaintext = plaintext.substring(CHECK_CODE.length()); // 解密成功，去除校验位
		}

		return plaintext;

	}

	/**
	 * 获取MD5 串
	 * 
	 * @version 1.0
	 * @createTime 2013-10-14,上午11:17:00
	 * @updateTime 2013-10-14,上午11:17:00
	 * @createAuthor 罗文忠
	 * @updateAuthor 罗文忠
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param plaintext
	 * @return
	 */
	public static String getMD5Key(String plaintext) {
		return md5Encrypt(CHECK_CODE + plaintext);
	}

	/**
	 * MD5加密
	 * 
	 * @version 1.0
	 * @createTime 2013-10-14,上午9:56:00
	 * @updateTime 2013-10-14,上午9:56:00
	 * @createAuthor 罗文忠
	 * @updateAuthor 罗文忠
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param plaintext 需要进行MD5加密的明文
	 * @return ciphertext MD5加密后的密文
	 */
	public static String md5Encrypt(String plaintext) {// 保持编码为UTF-8
		if (TextUtils.isEmpty(plaintext)) {
			plaintext = CHECK_CODE;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plaintext.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

}
