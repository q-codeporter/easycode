## Mybatis
##### mybatis-generator pagehelper  page 对象二次封装
org.zhiqiang.lu.easycode.spring.aop.model.mybatis.generator.PageInfo<T>
##### mybatis-plus BaseController 定义封装
描述：通用父类<br/>
org.zhiqiang.lu.easycode.spring.aop.model.mybatis.plus.BaseController<S extends IService<T>, T> <br/>
描述：使用swagger api 通用父类<br/>
org.zhiqiang.lu.easycode.spring.aop.model.mybatis.plus.swagger.BaseController<S extends IService<T>, T> <br/>


## SpringBoot全局AOP处理插件
### 使用方法：
springboot项目引入jar后，通过主类名添加@QPlugin注解开启插件功能
### 功能描述：
全局异常捕获与处理，全局返回数据的封装，开启@Security加密解密注解，开启@NotNull非null校验注解，开始@Session获取session注解<br/>
<br/>
##### 全局异常捕获
描述：异常无需try catch,插件自动捕获全局异常并统一处理，添加自定义事务逻辑异常 LogicException<br/>
<br/>
##### 返回数据封装
描述：统一封装返回数据<br/>
```
{
  "status": 200,                               // 状态代码，参考http状态代码
  "timestamp": "2020-04-26T05:25:01.966+0000", // 时间戳
  "error": "",                                 // 异常类型，出现异常时返回
  "message": "OK",                             // 异常信息
  "data": data,                                // 返回数据
  "encryption": false                          // 返回数据是否为加密数据
}
```
##### @Security加密解密注解
描述：支持post请求中的body参数解密、全局返回的数据的加密功能<br/>
使用方法：<br/>
@Security(method="AES", key = "\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*",encrypt = true,decrypt = true)<br/>
<br/>
默认值<br/>
key = "\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*" 密钥<br/>
encode = "UTF-8" 编码方式<br/>
algorithm_name = "AES" 算法名称<br/>
algorithm_name_ecb_padding = "AES/ECB/PKCS5Padding" 算法ecb padding<br/>
decrypt = true 开启解密，参数密文<br/>
encrypt = true 开启加密，返回密文<br/>
<br/>
使用位置：<br/>
全局使用：主类名加入注解<br/>
某个Controller使用：Controller类名加入注解<br/>
某个Controller中的方法使用：方法名加入注解<br/>
<br/>
##### @NotNull非null校验注解
描述：当接收参数为model类型时，添加判断model类型中的某些属性的非null校验功能<br/>
使用方法：<br/>
在model类中在需要判断非null的属性名上添加注解即可
<br/>
##### @Session获取session注解
描述：在Controller方法中可以直接使用session参数<br/>
使用方法：<br/>
方法参数直接使用@Session注解即可，参数名为存放session中的key
<br/>


