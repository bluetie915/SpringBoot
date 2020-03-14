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

