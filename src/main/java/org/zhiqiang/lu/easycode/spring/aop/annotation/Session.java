package org.zhiqiang.lu.easycode.spring.aop.annotation;

import com.google.gson.Gson;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AliasFor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.zhiqiang.lu.easycode.spring.aop.model.MissingParameterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.*;

/**
 * @author Q
 * @desc Session注解，方便直接获取session对象
 * @date 2019/10/25 20:17
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Session {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default true;

    /**
     * Resolves method arguments annotated with an @{@link SessionAttribute}.
     *
     * @author Rossen Stoyanchev
     * @since 4.3
     */
    class SessionMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(Session.class);
        }

        @Override
        protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
            Session ann = parameter.getParameterAnnotation(Session.class);
            Assert.state(ann != null, "No SessionAttribute annotation");
            return new NamedValueInfo(ann.name(), ann.required(), ValueConstants.DEFAULT_NONE);
        }

        @Override
        @Nullable
        protected Object resolveName(String name, MethodParameter methodParameter, NativeWebRequest nativeWebRequest) {
            System.out.println("Session生效");
            Gson gson = new Gson();
            HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
            HttpSession session = request.getSession();
            if (session.getAttribute(methodParameter.getParameterName()) != null) {
                Object session_value = session.getAttribute(methodParameter.getParameterName());
                if (session_value instanceof String) {
                    if (methodParameter.getParameterType().getName().equals("java.lang.String")) {
                        return (String) session_value;
                    } else {
                        return gson.fromJson((String) session_value, methodParameter.getParameterType());
                    }
                } else {
                    if (methodParameter.getParameterType().getName().equals("java.lang.String")) {
                        return session_value;
                    } else {
                        return gson.toJson(session.getAttribute(methodParameter.getParameterName()),
                                methodParameter.getParameterType());
                    }
                }
            } else {
                throw new MissingParameterException("Session " + methodParameter.getParameterType().getName()
                        + " parameter '" + methodParameter.getParameterName() + "' is not present");
            }
        }

        @Override
        protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {
            throw new ServletRequestBindingException("Missing session attribute '" + name + "' of type "
                    + parameter.getNestedParameterType().getSimpleName());
        }
    }
}
