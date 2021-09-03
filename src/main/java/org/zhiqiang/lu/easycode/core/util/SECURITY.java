package org.zhiqiang.lu.easycode.core.util;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.util.Base64;
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
    return encrypt(paramStr, key, "AES", "AES/ECB/PKCS5Padding", "UTF-8", "HEX");
  }

  public static String encrypt(String content, String key, String algorithm_name, String algorithm_name_ecb_padding,
      String encode, String output) throws Exception {
    String cipherText = "";
    if (null != content && !"".equals(content)) {
      Cipher cipher = generateEcbCipher(algorithm_name, algorithm_name_ecb_padding, Cipher.ENCRYPT_MODE,
          key.getBytes());
      byte[] cipherArray = cipher.doFinal(content.getBytes(encode));
      if ("HEX".equals(output)) {
        cipherText = ByteUtils.toHexString(cipherArray);
      } else {
        Base64 base64 = new Base64();
        cipherText = base64.encodeToString(cipherArray);
      }
    }
    return cipherText;
  }

  /**
   * @Description:解密
   */
  public static String decrypt(String cipherText, String key) throws Exception {
    return decrypt(cipherText, key, "AES", "AES/ECB/PKCS5Padding", "UTF-8", "HEX");
  }

  public static String decrypt(String cipherText, String key, String algorithm_name, String algorithm_name_ecb_padding,
      String encode, String output) throws Exception {
    byte[] cipherData = {};
    if ("HEX".equals(output)) {
      cipherData = ByteUtils.fromHexString(cipherText);
    } else {
      cipherData = Base64.decodeBase64(cipherText);
    }
    Cipher cipher = generateEcbCipher(algorithm_name, algorithm_name_ecb_padding, Cipher.DECRYPT_MODE, key.getBytes());
    byte[] srcData = cipher.doFinal(cipherData);
    return new String(srcData, encode);
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
      String json = "啊实打实的";
      String key = "1234567812345678";
      String cipher = security.encrypt(json, key, "SM4", "SM4/ECB/PKCS5Padding", "utf-8", "BASE64");
      System.out.println(cipher);
      json = security.decrypt(cipher, key, "SM4", "SM4/ECB/PKCS5Padding", "UTF-8", "BASE64");
      System.out.println(json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}