# 四、Web开发

## 1、简介

使用SpringBoot：

1. 创建SpringBoot应用，选中我们需要的模块；
2. SpringBoot已经默认将这些场景配置好了，只需要在配置文件中指定少量配置就可以运行起来；
3. 自己编写业务代码；

**自动配置原理？**

- 这个场景SpringBoot帮我们配置了什么？
- 能不能修改？
- 能修改哪些配置？
- 能不能扩展？

~~~
×××AutoConfiguration：帮我们给容器中自动配置组件
×××Properties：配置类来封装配置文件的内容
~~~

## 2、SpringBoot对静态资源的映射规则

~~~java
@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			if (!this.resourceProperties.isAddMappings()) {
				logger.debug("Default resource handling disabled");
				return;
			}
			Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
			CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
			if (!registry.hasMappingForPattern("/webjars/**")) {
				customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
						.addResourceLocations("classpath:/META-INF/resources/webjars/")
						.setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
			}
			String staticPathPattern = this.mvcProperties.getStaticPathPattern();
			if (!registry.hasMappingForPattern(staticPathPattern)) {
				customizeResourceHandlerRegistration(registry.addResourceHandler(staticPathPattern)
						.addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
						.setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
			}
		}
~~~

1. 所有/webjars/**，都去classpath:/META-INF/resources/webjars/找资源；

   webjars：以jar包的方式引入静态资源

<https://www.webjars.org/> 

![1584198192423](C:\Users\张艺成\AppData\Local\Temp\1584198192423.png)

2. "/**"：访问当前项目的任何资源（静态资源的文件夹）

~~~
"classpath:/META-INF/resources/",
"classpath:/resources/", 
"classpath:/static/", 
"classpath:/public/"
"/"：当前项目的根路径
~~~

localhost:8080/abc === 去静态资源文件夹里面找abc

3. 欢迎页：静态资源文件夹下的所有index.html页面，被"/**"映射；

   localhost:8080/  找index页面

4. 所有的**/favicon.ico都是在静态资源文件下找

## 3、模板引擎

Jsp、Velocity、Freemarker、Thymeleaf；

![1584199274373](C:\Users\张艺成\AppData\Local\Temp\1584199274373.png)

SpringBoot推荐的Thymeleaf：

语法更简单，功能更强大

### 3.1、引入thymeleaf

~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<thymeleaf.version>3.0.9.RELEASE</thymeleaf.version>
<!-- 布局功能的支持程序，thymeleaf3主程序，layout2以上版本 -->
<thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>
~~~

### 3.2、thymeleaf使用&语法

~~~
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";
}
~~~

**只要我们把html页面放在classpath:/templates/，thymeleaf就会自动渲染java**

#### 3.2.1、导入thymeleaf的名称空间：

~~~xml
<html lang="en" xmlns:th="http://www.thymeleaf.org">
~~~

#### 3.2.2、使用thymeleaf语法：

~~~html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
	<meta charset="UTF-8">
	<title>Title</title>
</head>
<body>
	<h2>成功</h2>
	<!-- 将div里面的文本内容设置为文本 -->
	<div th:text="${hello}">内容</div>
</body>
</html>
~~~

#### 3.2.3、语法规则

1. th:text：改变当前元素里面的文本内容；
2. th：任意html属性，来替换原生属性的

![1584238533759](C:\Users\张艺成\AppData\Local\Temp\1584238533759.png)

~~~properties
Simple expressions:（表达式语法）
  Variable Expressions: ${...} 获取变量：OGNL
    1.获取对象的属性，调用方法
      2.使用内置的基本对象
        #ctx : the context object.
        #vars: the context variables.
        #locale : the context locale.
        #request : (only in Web Contexts) the HttpServletRequest object.
        #response : (only in Web Contexts) the HttpServletResponse object.
        #session : (only in Web Contexts) the HttpSession object.
        #servletContext : (only in Web Contexts) the ServletContext object.
        ${session.foo}
      3.内置的一些工具对象
        #execInfo : information about the template being processed.
        #messages : methods for obtaining externalized messages inside variables expressions, in the same way as they            would be obtained using #{…} syntax.
        #uris : methods for escaping parts of URLs/URIs
        #conversions : methods for executing the configured conversion service (if any).
        #dates : methods for java.util.Date objects: formatting, component extraction, etc.
        #calendars : analogous to #dates , but for java.util.Calendar objects.
        #numbers : methods for formatting numeric objects.
        #strings : methods for String objects: contains, startsWith, prepending/appending, etc.
        #objects : methods for objects in general.
        #bools : methods for boolean evaluation.
        #arrays : methods for arrays.
        #lists : methods for lists.
        #sets : methods for sets.
        #maps : methods for maps.
        #aggregates : methods for creating aggregates on arrays or collections.
        #ids : methods for dealing with id attributes that might be repeated (for example, as a result of an iteration).
            
  Selection Variable Expressions: *{...}  选择表达式：和${}在功能上一样：
    补充：配合th:object="${session.user}"
      <div th:object="${session.user}">
          <p>Name: <span th:text="*{firstName}">Sebastian</span>.</p>
          <p>Surname: <span th:text="*{lastName}">Pepper</span>.</p>
          <p>Nationality: <span th:text="*{nationality}">Saturn</span>.</p>
      </div>
    
  Message Expressions: #{...}  获取国际化内容
  Link URL Expressions: @{...}  定义URL
    @{/order/process(execId=${execId},execType='FAST')}
    
    Fragment Expressions: ~{...}  片段引用表达式
      <div th:insert="~{commons :: main}">...</div>
Literals（字面量）
    Text literals: 'one text' , 'Another one!' ,…
    Number literals: 0 , 34 , 3.0 , 12.3 ,…
    Boolean literals: true , false
    Null literal: null
    Literal tokens: one , sometext , main ,…
Text operations:（文本操作）
    String concatenation: +
    Literal substitutions: |The name is ${name}|
Arithmetic operations:（数学运算）
    Binary operators: + , - , * , / , %
    Minus sign (unary operator): -
Boolean operations:（布尔运算）
    Binary operators: and , or
    Boolean negation (unary operator): ! , not
    Comparisons and equality:
    Comparators: > , < , >= , <= ( gt , lt , ge , le )
    Equality operators: == , != ( eq , ne )
Conditional operators:（条件运算）（三元运算符）
    If-then: (if) ? (then)
    If-then-else: (if) ? (then) : (else)
    Default: (value) ?: (defaultvalue)
Special tokens:
	Page 17 of 106No-Operation: _
~~~

## 4、SpringMVC自动配置

<https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html#boot-features-developing-web-applications> 

- #### 7.1.1. Spring MVC Auto-configuration

  SpringBoot自动配置好了SpringMVC，以下是对SpringMVC的默认配置：

  Spring Boot provides auto-configuration for Spring MVC that works well with most applications.

  The auto-configuration adds the following features on top of Spring’s defaults:

  - Inclusion of `ContentNegotiatingViewResolver` and `BeanNameViewResolver` beans.
    - 自动配置了ViewResolver（视图解析器：根据方法的返回值得到视图对象(View)，视图对象决定如何渲染（转发&重定向））
    - `ContentNegotiatingViewResolver`：组件视图解析器
    - 如何定制：我们可以自己给容器中添加一个视图解析器，自动的将其组合进来
  - Support for serving static resources, including support for WebJars (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-static-content))).
  - Automatic registration of `Converter`, `GenericConverter`, and `Formatter` beans.
  - Support for `HttpMessageConverters` (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-message-converters)).
  - Automatic registration of `MessageCodesResolver` (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-message-codes)).
  - Static `index.html` support.（静态首页访问）
  - Custom `Favicon` support (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-favicon)).（favicon.ico）
  - Automatic use of a `ConfigurableWebBindingInitializer` bean (covered [later in this document](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-web-binding-initializer)).