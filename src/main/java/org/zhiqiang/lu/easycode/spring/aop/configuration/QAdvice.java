package org.zhiqiang.lu.easycode.spring.aop.configuration;

import org.zhiqiang.lu.easycode.core.util.security;
import org.zhiqiang.lu.easycode.spring.aop.annotation.Security;
import org.zhiqiang.lu.easycode.spring.aop.annotation.Session;
import org.zhiqiang.lu.easycode.spring.aop.model.DecryptException;
import org.zhiqiang.lu.easycode.spring.aop.model.LogicException;
import org.zhiqiang.lu.easycode.spring.aop.model.MissingParameterException;
import org.zhiqiang.lu.easycode.spring.aop.model.ReturnMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.security.auth.login.LoginException;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: Q
 * @Description: 全局AOP处理，全局异常捕获，封装注解加密解密，封装返回参数
 */
@Configuration
public class QAdvice implements WebMvcConfigurer {

  @Override
  public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
    // 第一种方式是将 json 处理的转换器放到第一位，使得先让 json 转换器处理返回值，这样 String转换器就处理不了了。
    // converters.add(0, new MappingJackson2HttpMessageConverter());
    // 第二种就是把String类型的转换器去掉，不使用String类型的转换器
    converters.removeIf(httpMessageConverter -> httpMessageConverter.getClass() == StringHttpMessageConverter.class);
  }

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new Session.SessionMethodArgumentResolver());
  }

  /**
   * @Auther: Q
   * @Description: 全局捕获异常和自定义全局捕获异常
   */
  @ControllerAdvice
  public class ExceptionAdvice {
    /**
     * 全局异常处理，反正异常返回统一格式的map
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ReturnMessage exceptionHandler(final Exception e) {
      final ReturnMessage message = new ReturnMessage();
      if (e instanceof ServletRequestBindingException) {
        message.setStatus(400);
        message.setMessage(e.getMessage());
        message.setError("Bad Request");
      } else if (e instanceof MissingServletRequestParameterException || e instanceof MissingParameterException) {
        message.setStatus(400);
        message.setMessage(e.getMessage());
        message.setError("Bad Request");
      } else if (e instanceof LoginException) {
        message.setStatus(401);
        message.setMessage(e.getMessage());
        message.setError("Unauthorized");
      } else if (e instanceof HttpMessageNotReadableException) {
        message.setStatus(402);
        message.setMessage(e.getMessage());
        message.setError("Payment Required");
      } else if (e instanceof LogicException) {
        message.setStatus(418);
        message.setMessage(e.getMessage());
        message.setError("Logic Error");
      } else if (e instanceof SQLException) {
        message.setStatus(500);
        message.setMessage(e.getMessage());
        message.setError("Data Base Error");
      } else {
        e.printStackTrace();
        message.setStatus(500);
        message.setMessage(e.getMessage());
        message.setError("Internal Server Error");
      }
      return message;
    }
  }

  /**
   * @author Q
   * @desc 请求数据解密
   * @date 2019/10/25 20:17
   */
  @ControllerAdvice(annotations = RestController.class)
  class RestRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(final MethodParameter methodParameter, final Type type,
        final Class<? extends HttpMessageConverter<?>> aClass) {
      return true;
    }

    @Override
    public Object handleEmptyBody(final Object body, final HttpInputMessage httpInputMessage,
        final MethodParameter methodParameter, final Type type, final Class<? extends HttpMessageConverter<?>> aClass) {
      return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, final MethodParameter methodParameter,
        final Type type, final Class<? extends HttpMessageConverter<?>> c) throws DecryptException {
      Security security = null;
      String method = "", key = "";
      if (methodParameter.getDeclaringClass().isAnnotationPresent(Security.class)) {
        security = methodParameter.getDeclaringClass().getAnnotation(Security.class);
        method = security.method();
        key = security.key();
      }

      if (methodParameter.getMethod().isAnnotationPresent(Security.class)) {
        security = methodParameter.getMethodAnnotation(Security.class);
        method = security.method();
        key = security.key();
      }

      // 入参是否需要解密
      if (security != null && security.decrypt()) {
        inputMessage = new DecryptHttpInputMessage(inputMessage, method, key);
      }
      return inputMessage;
    }

    @Override
    public Object afterBodyRead(final Object body, final HttpInputMessage httpInputMessage,
        final MethodParameter methodParameter, final Type type, final Class<? extends HttpMessageConverter<?>> aClass) {
      return body;
    }

    class DecryptHttpInputMessage implements HttpInputMessage {
      private final HttpHeaders headers;

      private InputStream body;

      public DecryptHttpInputMessage(final HttpInputMessage inputMessage, final String method, final String key)
          throws DecryptException {
        this.headers = inputMessage.getHeaders();
        try {
          final ByteArrayOutputStream baos = new ByteArrayOutputStream();
          int i = -1;
          while ((i = inputMessage.getBody().read()) != -1) {
            baos.write(i);
          }
          this.body = new ByteArrayInputStream(security.decrypt(baos.toString(), method, key).getBytes());
        } catch (final Exception e) {
          throw new DecryptException("密文参数解析失败");
        }
      }

      @Override
      public InputStream getBody() {
        return body;
      }

      @Override
      public HttpHeaders getHeaders() {
        return headers;
      }
    }
  }

  /**
   * @author Q
   * @desc 返回数据加密
   * @date 2019/10/25 20:17
   */

  @ControllerAdvice(annotations = RestController.class)
  class RestResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(final MethodParameter methodParameter, final Class<? extends HttpMessageConverter<?>> c) {
      return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, final MethodParameter methodParameter, final MediaType mediaType,
        final Class<? extends HttpMessageConverter<?>> c, final ServerHttpRequest serverHttpRequest,
        final ServerHttpResponse serverHttpResponse) {

      final Gson gson = new Gson();
      final ReturnMessage message = new ReturnMessage();
      message.setPath(serverHttpRequest.getURI().getPath());

      Security security = null;
      String method = "", key = "";
      if (methodParameter.getDeclaringClass().isAnnotationPresent(Security.class)) {
        security = methodParameter.getDeclaringClass().getAnnotation(Security.class);
        method = security.method();
        key = security.key();
      }

      if (methodParameter.getMethod().isAnnotationPresent(Security.class)) {
        security = methodParameter.getMethodAnnotation(Security.class);
        method = security.method();
        key = security.key();
      }

      // 出参是否需要加密
      if (security != null && security.encrypt()) {
        try {
          if ("java.lang.String".equals(body.getClass().getName())) {
            body = org.zhiqiang.lu.easycode.core.util.security.encrypt((String) body, method, key);
          } else {

            body = org.zhiqiang.lu.easycode.core.util.security.encrypt(gson.toJson(body), method, key);
          }
          message.setEncryption(true);
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
      message.setData(body);
      return message;
    }
  }
}
