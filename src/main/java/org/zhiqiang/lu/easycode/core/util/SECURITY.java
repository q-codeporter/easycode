package org.zhiqiang.lu.easycode.core.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import org.zhiqiang.lu.easycode.spring.aop.model.LogicException;

public class security {

  /**
   * 加密
   *
   * @param content    加密的字符串
   * @param encryptKey key值
   * @return
   * @throws Exception
   */
  public static String encrypt(String content, String method, String encryptKey) throws Exception {
    if (data.no_null(content)) {
      if ("AES".equals(method)) {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        byte[] b = cipher.doFinal(content.getBytes("utf-8"));
        // 采用base64算法进行转码,避免出现中文乱码
        return Base64.getEncoder().encodeToString(b);
      } else if ("SM4".equals(method)) {
        return "";
      } else {
        throw new LogicException("沒有找到加密方法");
      }
    } else {
      return "";
    }
  }

  /**
   * 解密
   *
   * @param content    解密的字符串
   * @param decryptKey 解密的key值
   * @return
   * @throws Exception
   */
  public static String decrypt(String content, String method, String decryptKey) throws Exception {
    if (data.no_null(content)) {
      if ("AES".equals(method)) {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.getDecoder().decode(content);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
      } else if ("SM4".equals(method)) {
        return "";
      } else {
        throw new LogicException("沒有找到加密方法");
      }
    } else {
      return "";
    }
  }
}
