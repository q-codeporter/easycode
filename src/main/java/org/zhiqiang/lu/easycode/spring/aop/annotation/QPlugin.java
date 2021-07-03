package org.zhiqiang.lu.easycode.spring.aop.annotation;

import org.zhiqiang.lu.easycode.spring.aop.configuration.QAdvice;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Auther: Q
 * @Description: 全局AOP处理，开启全局异常捕获，开启封装注解加密解密，开启返回参数封装
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented

@Import({QAdvice.class,})
public @interface QPlugin {
}
