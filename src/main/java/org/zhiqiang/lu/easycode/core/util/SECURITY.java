package org.zhiqiang.lu.easycode.core.util;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

public class security {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  /**
   * @Description:生成ecb暗号
   */
  private static Cipher generateEcbCipher(String algorithm_name, String algorithm_name_ecb_padding, int mode,
      byte[] key) throws Exception {
    Cipher cipher = Cipher.getInstance(algorithm_name_ecb_padding, BouncyCastleProvider.PROVIDER_NAME);
    Key sm4Key = new SecretKeySpec(key, algorithm_name);
    cipher.init(mode, sm4Key);
    return cipher;
  }

  /**
   * @Description:自动生成密钥
   */
  // public static byte[] generateKey(int keySize) throws Exception {
  // KeyGenerator kg = KeyGenerator.getInstance(algorithm_name,
  // BouncyCastleProvider.PROVIDER_NAME);
  // kg.init(keySize, new SecureRandom());
  // return kg.generateKey().getEncoded();
  // }

  /**
   * @Description:加密
   */
  public static String encrypt(String paramStr, String key) throws Exception {
    return encrypt(paramStr, key, "AES", "AES/ECB/PKCS5Padding", "UTF-8");
  }

  public static String encrypt(String paramStr, String key, String algorithm_name, String algorithm_name_ecb_padding,
      String encode) throws Exception {
    String cipherText = "";
    if (null != paramStr && !"".equals(paramStr)) {
      byte[] keyData = ByteUtils.fromHexString(ByteUtils.toHexString(key.getBytes()));
      byte[] srcData = paramStr.getBytes(encode);
      Cipher cipher = generateEcbCipher(algorithm_name, algorithm_name_ecb_padding, Cipher.ENCRYPT_MODE, keyData);
      byte[] cipherArray = cipher.doFinal(srcData);
      cipherText = ByteUtils.toHexString(cipherArray);
    }
    return cipherText;
  }

  /**
   * @Description:解密
   */
  public static String decrypt(String cipherText, String key) throws Exception {
    return decrypt(cipherText, key, "AES", "AES/ECB/PKCS5Padding", "UTF-8");
  }

  public static String decrypt(String cipherText, String key, String algorithm_name, String algorithm_name_ecb_padding,
      String encode) throws Exception {
    String decryptStr = "";
    byte[] keyData = ByteUtils.fromHexString(ByteUtils.toHexString(key.getBytes()));
    byte[] cipherData = ByteUtils.fromHexString(cipherText);
    Cipher cipher = generateEcbCipher(algorithm_name, algorithm_name_ecb_padding, Cipher.DECRYPT_MODE, keyData);
    byte[] srcData = cipher.doFinal(cipherData);
    decryptStr = new String(srcData, encode);
    return decryptStr;
  }

  // /**
  // * @Description:密码校验
  // */
  // public static boolean verify(String hexKey, String cipherText, String
  // paramStr) throws Exception {
  // boolean flag = false;
  // byte[] keyData = ByteUtils.fromHexString(hexKey);
  // byte[] cipherData = ByteUtils.fromHexString(cipherText);
  // byte[] decryptData = decrypt_padding(keyData, cipherData);
  // byte[] srcData = paramStr.getBytes(encode);
  // flag = Arrays.equals(decryptData, srcData);
  // return flag;
  // }

  /**
   * @Description:测试类
   */
  public static void main(String[] args) {
    try {
      String json = "asd";
      String key = "12345678123456781234567812345678";
      String cipher = security.encrypt(json, key, "AES", "AES/ECB/PKCS5Padding", "UTF-8");
      System.out.println(cipher);
      json = security.decrypt(cipher, key, "AES", "AES/ECB/PKCS5Padding", "UTF-8");
      System.out.println(json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
