package org.zhiqiang.lu.easycode.spring.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解
 *
 * @author Q
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull {
}

