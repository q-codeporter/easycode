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
      String json = "啊实打实的";
      String key = "1234567812345678";
      String cipher = security.encrypt(json, key, "SM4", "SM4/ECB/PKCS5Padding", "utf-8");
      System.out.println(cipher);
      json = security.decrypt(
          "66e4bfc4845dabe01a457f9a4cb1d8a6cd8be5c8610774f261b4eec251214f2dc6c4718d909a1663d2f002fed63c754377cc8cdcad8af4a07b92af22c8457617b8f4ab7c6c1f4ec34cb68e51dd2110b76fa9f4b2ee6c361323097dd8ec10972fede8f42a45b597ddeeaadc5d7831c0102b276b7cd2ebe6bda3dc9f3db67cfa1a6efb7e24ed22b7c6053258bed95a5743ac01d1c31a7df1f33d77fb3c2bf09c208900e894d1340fee8a25f3f1850aa7eebc1a11ecd44b78726a2f18829a33b546eaa624f968ba526c0de0564c7239c04f74b3aa039699851ce4dd7f832ab511f33bfdfb2dfa0c04b5f6f7d6315afa5c8ab6ebd8ff4bd4c16a35ceebdac43b9afbbd018e4e6f8f5bdd036afbf3a07ee48c80be0786d7ca4cac1aa5793714bd50eaa6686a292c5228024cf1f3aaa1fdfb426e213c814f31f99aca0ebba0003f26d3672b8613eabddb41bc9c72270e7fe3d429e05ecac1eb92c8f2a6ca00f5b4618db0ccb1a51456a9c8253734b4834987f362d9e4a247d0463c0461c67af7742ec216c905a8f75ad73b27fa102519a0d3772e061cbc580858f0a3a7f4374e989b5c54dadc959be9b5d2b52b28565cb5dc6bfe23be7c9221217328118a524426ef40dd2c8b2871e34e1c87359ae9016f3d4fc607afa7c672288a8887b40e8ec48858c5e8f68a2e6e8a7a9faca5022080f348323a793dd7067a81e759d4e072cae876e082442fa0dc97c7aff2da3945fdc3f01f38c73a3b78790a14b423c6c143bcd9535ec20030949fb25976724d78e2e3eb031fe130eb8b4af8395778f52fb3574d17884c3559c4fd7d06361ccbe21324b08f53d490d72f27065bb393ffb5490fa6f357d368b99781ad0470c743ed189dd7c452fd583ec18d9f372ab6d4a0d27de6822a7d5681e3e7c8d9817221bc3f7304231a4984464ef9d3a239818d01f949113bb5689927d2dd6423d878ca641318da",
          key, "SM4", "SM4/ECB/PKCS5Padding", "UTF-8");
      System.out.println(json);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}