package org.zhiqiang.lu.easycode.spring.aop.annotation;

import java.lang.annotation.*;

/**
 * @author Q
 * @desc 请求数据解密
 * @date 2019/10/25 20:17
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Security {

  String key() default "****************";

  String encode() default "UTF-8";

  String algorithm_name() default "AES";

  String algorithm_name_ecb_padding() default "AES/ECB/PKCS5Padding";

  String output() default "HEX";

  /**
   * 入参是否解密，默认解密
   */
  boolean decrypt() default true;

  /**
   * 出参是否加密，默认加密
   */
  boolean encrypt() default true;
}
