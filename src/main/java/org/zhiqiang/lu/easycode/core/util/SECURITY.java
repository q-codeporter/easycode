package org.zhiqiang.lu.easycode.core.util;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

public class security {
  public static String encode = "UTF-8";
  public static String algorithm_name = "AES";
  public static String algorithm_name_ecb_padding = "AES/ECB/PKCS5Padding";
  public static int key_size = 128;

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  /**
   * @Description:生成ecb暗号
   */
  private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
    Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
    Key sm4Key = new SecretKeySpec(key, algorithm_name);
    cipher.init(mode, sm4Key);
    return cipher;
  }

  /**
   * @Description:自动生成密钥
   */
  public static byte[] generateKey() throws Exception {
    return generateKey(key_size);
  }

  public static byte[] generateKey(int keySize) throws Exception {
    KeyGenerator kg = KeyGenerator.getInstance(algorithm_name, BouncyCastleProvider.PROVIDER_NAME);
    kg.init(keySize, new SecureRandom());
    return kg.generateKey().getEncoded();
  }

  public static byte[] generateKey(String key) throws Exception {
    return key.getBytes();
  }

  /**
   * @Description:加密
   */
  public static String encrypt(String paramStr, String key) throws Exception {
    String cipherText = "";
    if (null != paramStr && !"".equals(paramStr)) {
      byte[] keyData = ByteUtils.fromHexString(ByteUtils.toHexString(key.getBytes()));
      byte[] srcData = paramStr.getBytes(encode);
      byte[] cipherArray = encrypt_padding(keyData, srcData);
      cipherText = ByteUtils.toHexString(cipherArray);
    }
    return cipherText;
  }

  private static byte[] encrypt_padding(byte[] key, byte[] data) throws Exception {
    Cipher cipher = generateEcbCipher(algorithm_name_ecb_padding, Cipher.ENCRYPT_MODE, key);
    byte[] bs = cipher.doFinal(data);
    return bs;
  }

  /**
   * @Description:sm4解密
   */
  public static String decrypt(String cipherText, String key) throws Exception {
    String decryptStr = "";
    byte[] keyData = ByteUtils.fromHexString(ByteUtils.toHexString(key.getBytes()));
    byte[] cipherData = ByteUtils.fromHexString(cipherText);
    byte[] srcData = decrypt_padding(keyData, cipherData);
    decryptStr = new String(srcData, encode);
    return decryptStr;
  }

  private static byte[] decrypt_padding(byte[] key, byte[] cipherText) throws Exception {
    Cipher cipher = generateEcbCipher(algorithm_name_ecb_padding, Cipher.DECRYPT_MODE, key);
    return cipher.doFinal(cipherText);
  }

  /**
   * @Description:密码校验
   */
  public static boolean verify(String hexKey, String cipherText, String paramStr) throws Exception {
    boolean flag = false;
    byte[] keyData = ByteUtils.fromHexString(hexKey);
    byte[] cipherData = ByteUtils.fromHexString(cipherText);
    byte[] decryptData = decrypt_padding(keyData, cipherData);
    byte[] srcData = paramStr.getBytes(encode);
    flag = Arrays.equals(decryptData, srcData);
    return flag;
  }

  /**
   * @Description:测试类
   */
  public static void main(String[] args) {
    try {
      String json = "asd";
      // 自定义的32位16进制密钥
      // String key = "210d1fd6382f2ceb2f31a39fd2d2c350";
      String key = "1234567812345678";
      security.algorithm_name = "SM4";
      security.algorithm_name_ecb_padding = "SM4/ECB/PKCS5Padding";
      String cipher = security.encrypt(json, key);
      System.out.println(cipher);
      json = security.decrypt(cipher, key);
      System.out.println(json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
