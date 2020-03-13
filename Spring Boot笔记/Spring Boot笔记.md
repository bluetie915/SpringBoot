# 一、Spring Boot入门

## 1、Spring Boot简介

> 简化Spring应用开发的一个框架；
>
> 整个Spring技术栈的一个大整合；
>
> J2EE开发的一站式解决方案。

## 2、微服务

微服务：架构风格（服务微化）

一个应用应该是一组小型服务：可以通过HTTP的方式进行互通；

每一个功能元素最终都是一个可独立替换和独立升级的软件单元；

详细参照微服务文档<https://blog.csdn.net/weixin_41404773/article/details/82689866> 

## 3、环境准备



## 4、Spring Boot HelloWorld

一个功能

浏览器发送hello请求，服务器接受请求并处理，响应Hello World字符串。

### 4.1、创建一个maven工程；（jar）



### 4.2、导入依赖Spring Boot相关的依赖

~~~xml
<!-- Inherit defaults from Spring Boot -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.5.RELEASE</version>
    </parent>

    <!-- Add typical dependencies for a web application -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
~~~

### 4.3、编写一个主程序，启动Spring Boot应用

~~~java
/**
 * 标注一个主程序类，说明这是一个Spring Boot应用
 */
@SpringBootApplication
public class HelloWorldMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldMainApplication.class, args);
    }
}
~~~

### 4.4、编写相关的Controller、Service

~~~java
@Controller
public class HelloController {

    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
~~~

### 4.5、运行主程序测试



### 4.6、简化部署

~~~xml
<!-- 该插件可以将应用打包成一个可执行的jar包 -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
~~~

将这个应用打成jar包，直接使用java -jar命令进行执行。

## 5、Hello World探究

### 5.1、POM文件

#### 5.1.1、父项目

~~~xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.5.RELEASE</version>
</parent>

<!-- 它的父项目 -->
<!-- 它来真正管理Spring Boot应用里面的所有依赖版本 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.2.5.RELEASE</version>
    <relativePath>../../spring-boot-dependencies</relativePath>
</parent>
~~~

Spring Boot的版本仲裁中心

以后我们导入依赖认为是不需要写版本（没有在dependencies里面管理的依赖自然需要声明版本号）。

#### 5.1.2、导入的依赖

~~~xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
~~~

spring-boot-starter-web：

​	spring-boot-starter：spring-boot场景启动器，帮我们导入了web模块正常运行所依赖的组件。

Spring Boot将所有的功能场景都抽取出来，做成一个个starters（启动器），只需要在项目里面引入这些starter，相关场景的所有依赖都会导入进来，要用什么功能就导入什么场景的启动器。

### 5.2、主程序类、主入口类

```java
@SpringBootApplication
public class HelloWorldMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldMainApplication.class, args);
    }
}
```

**@SpringBootApplication**：标注在某个类上说明这个类是Spring Boot的主配置类，Spring Boot就应该运行这个类的main方法来启动Spring Boot应用。

~~~java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
~~~

**@SpringBootConfiguration**：Spring Boot的配置类，标注在某个类上说明这是一个Spring Boot的配置类

​	**@Configuration**：配置类上来标注这个注解：

​		配置类----配置文件：配置类也是容器中的一个组件：**@Component**

~~~java
@Configuration
public @interface SpringBootConfiguration {
~~~

**@EnableAutoConfiguration**：开启自动配置功能：

​	以前我们需要配置的东西，Spring Boot帮我们自动配置，@EnableAutoConfiguration告诉Spring Boot开启			  	  自动配置功能，这样自动配置才能生效。

~~~java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
~~~

​	**@AutoConfigurationPackage**：自动配置包

​		**@Import**(AutoConfigurationPackages.Registrar.class)

​		Spring的底层注解**@import**，给容器中导入一个组件，导入的组件由AutoConfigurationPackages.Registrar.class决定。

​		**将主配置类（@SpringBootApplication标注）所在包及所有子包里面所有组件扫描到Spring容器**

​	**@Import(AutoConfigurationImportSelector.class)**

​		给容器导入组件：

​		AutoConfigurationImportSelector.class：导入哪些组件的选择器？

​		将所有需要导入的组件以全类名的方式返回，这些组件就会被添加到容器中；

​		会给容器中导入非常多的自动配置类（×××AutoConfiguration），就是给容器中导入这个场景需要的所有组件，并配置好这些组件。

​		有了自动配置类，免去了我们手动配置注入功能组件等的工作：Spring Boot在启动的时候从类路径下的META-INF/spring.factories中获取EnableAutoConfiguration指定的值，将这些值作为自动配置类导入到容器中，自动配置类就生效，帮我们进行自动配置工作。

**J2EE的整体整合解决方案和自动配置都在spring-boot-autoconfigure-2.2.5.RELEASE.jar**

## 6、使用Spring Initializer快速创建Spring Boot项目

默认生成的Spring Boot项目：

- 主程序已经生成好了，我们只需要编写自己需要的逻辑
- resources文件夹中目录结构
  - static：保存所有的静态资源：javascript、css、images；
  - template：保存所有的模板页面（Spring Boot使用内嵌Tomcat，默认不支持Jsp页面），可以使用模板引擎（freemarker、themeleaf）；
  - application.properties：Spring Boot应用的配置文件，可以修改一些默认配置。

# 二、配置文件

## 1、配置文件

Spring Boot使用一个全局文件，配置文件名是固定的。

- application.properties
- application.yml

配置文件的作用：修改Spring Boot自动配置的默认值，Spring Boot在底层都给我们自动配置好

标记语言：

​	以前的配置文件大多都使用的是*.xml文件；

​	YAML：以数据为中心，比json、xml等更适合做配置文件；

~~~yaml
server:
	port:8081
~~~

~~~xml
<server>
	<port>8081</port>
</server>
~~~

## 2、YAML语法：

### 2.1、基本语法

k：（空格）v：表示一对键值对（空格必须有）

以空格的缩进来表示层级关系，只要是左边对齐的一列数据，都是同一个层级的

~~~yaml
server:
	port:8081
	path:/hello
~~~

属性和值也是大小写敏感。

### 2.2、值的写法

#### 字面量：普通的值（数字，字符串，布尔）

​	k：v：字面直接来写：

​		字符串默认不用加上单引号或者双引号

​		"  "：双引号不会转义字符串里面的特殊字符，特殊字符会作为本身想表示的意思

​		name："zhangsan\nlisi"	输出：zhangsan \n(换行) lisi

​		'  '：单引号会转义字符串里面的特殊字符，特殊字符就只是一个普通的字符串

​		name："zhangsan\nlisi"	输出：zhangsan\nlisi

#### 对象、Map（属性和值）（键值对）

​	k：v：

​		对象还是k：v的方式

~~~yaml
friends:
	lastName:zhangsan
	age:22
~~~

​	行内写法：

~~~yaml
friends: {lastName: zhangsan,age: 22}
~~~

#### 数组（List、Set）

~~~yaml
pets:
 - cat
 - dog
 - pig
~~~

行内写法

~~~yaml
pets: {cat,dog,pig}
~~~

## 3、配置文件值注入

xml：

~~~yaml
person:
  lastName: zhangsan
  age: 18
  boss: false
  birth: 2017/12/12
  maps: {k1: v1, k2: v2}
  lists:
    - lisi
    - zhangsan
  dog:
    name: 小狗
    age: 2
~~~

javaBean：

~~~java
/**
 * 将配置文件中的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties，告诉SpringBoot将本类所有属性和配置文件中相关的配置绑定
 *  prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 *
 *  只有这个组件是容器中的组件，才能提供@ConfigurationProperties功能
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {

    private String  lastName;
    private Integer age;
    private Boolean boss;
    private Date birth;

    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}
~~~

我们可以导入配置文件处理器，以后编写配置就有提示了

~~~xml
<!-- 导入配置文件处理器，配置文件进行绑定就会有提示 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
~~~

### 3.1、properties配置文件在idea中默认UTF-8可能会乱码

![1584090987174](C:\Users\张艺成\AppData\Local\Temp\1584090987174.png)

选中运行改为ascii即可

### 3.2、@Value获取值和@ConfigurationProperties获取值比较

|                    | @ConfigurationProperties | @Value     |
| ------------------ | ------------------------ | ---------- |
| 功能               | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定(松散语法) | 支持                     | 不支持     |
| SpEL               | 不支持                   | 支持       |
| JSR303数据校验     | 支持                     | 不支持     |
| 复杂类型封装       | 支持                     | 不支持     |

配置文件yml还是properties他们都能获取到值，但是：

如果我们只是在某个业务逻辑中需要获取一下配置文件中的某项值，使用**@Value**；

如果我们专门编写了一个javaBean和配置文件进行映射，我们就直接使用**@ConfigurationProperties**即可。

### 3.3、配置文件注入值数据校验

~~~java
@Component
@ConfigurationProperties(prefix = "person")
//@Validated
public class Person {

    /**
     * <bean class="Person">
     *     <property name="lastName" value="字面量/${key}(从环境变量、配置文件中获取值)/#{SpEL}"></property>
     * </bean>
     */
//    @Email
    @Value("${person.last-name}")
    private String  lastName;
    @Value("#{11*3}")
    private Integer age;
    @Value("true")
    private Boolean boss;
    private Date birth;

    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}
~~~

### 3.4、@PropertySource&@ImportResource

**@PropertySource**：加载指定的配置文件，前提是主配置文件中没有

~~~java
@PropertySource(value = {"classpath:person.properties"})
@Component
@ConfigurationProperties(prefix = "person")
//@Validated
public class Person {

    /**
     * <bean class="Person">
     *     <property name="lastName" value="字面量/${key}(从环境变量、配置文件中获取值)/#{SpEL}"></property>
     * </bean>
     */
//    @Email
//    @Value("${person.last-name}")
    private String  lastName;
//    @Value("#{11*3}")
    private Integer age;
//    @Value("true")
    private Boolean boss;
    private Date birth;

    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}
~~~

**@ImportResource**：导入Spring的配置文件，让配置文件里面的内容生效

​	Spring Boot里面没有Spring的配置文件，我们自己编写的配置文件也不能识别，想让Spring的配置文件生效，使用@ImportResource标注在一个配置类上

~~~java
@ImportResource(locations = {"classpath:beans.xml"})
// 导入Spring的配置文件让其生效
~~~
不来编写Spring的配置文件

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="helloService" class="com.yicheng.springboot.service.HelloService">

    </bean>
</beans>
~~~

SpringBoot推荐给容器中添加组件的方式：（全注解）

- 配置类====Spring配置文件
- 使用@Bean给容器中添加组件

~~~java
/**
 * @Configuration：指明当前类是一个配置类，就是来替代之前的Spring配置文件
 * 在配置文件中有<bean></bean>标签添加组件
 */
@Configuration
public class MyAppConfig {

    // 将方法的返回值添加到容器中，容器中这个组件默认的id就是方法名
    @Bean
    public HelloService helloService() {
        System.out.println("配置类@Bean给容器中添加组件成功！");
        return new HelloService();
    }
}
~~~

## 4、配置文件占位符

### 4.1、随机数

~~~java
${random.value}、${random.value}、${random.long}
${random.int(10)、${random.int[1021, 65536]}}
~~~

### 4.2、占位符获取之前配置的值，如果没有可以使用：指定默认值

~~~properties
person.last-name=张三
person.age=22
person.birth=2017/2/17
person.boss=${person.flag:true}
person.maps.k1=v1
person.maps.k2=v2
person.lists=a,b,c
person.dog.name=${person.last-name}的dog
person.dog.age=${random.int}
~~~

## 5、Profile

### 5.1、多Profile文件

我们在配置文件编写的时候，文件名可以是application-(profile).properties/yml

默认使用application.properties的配置

### 5.2、yml支持多文档块方式

~~~yaml
server:
  port: 8081
spring:
  profiles:
    active: prod # 指定属于哪个环境
---
server:
  port: 8082
spring:
  profiles: dev
---
server:
  port: 8083
spring:
  profiles: prod
~~~

### 5.3、激活指定profile

- 在配置文件中指定spring.profiles.active=prod

- 命令行方式激活：

  ​	在启动时设置启动项Program arguments：--spring.profiles.active=dev即可使用该配置，优先级高于配置文件中的配置，也可以在运行jar包时，使用java -jar *.jar --spring.profiles.active=dev/prod

- 虚拟机参数

  ​	-Dspring.profiles.active=dev

## 6、配置文件加载位置

SpringBoot启动会扫描以下位置的application.properties或者application.yml文件作为SpringBoot的默认配置文件

-file:./config/

-file:./

-classpath:/config/

-classpath:/

优先级由高到低，高优先级的配置会覆盖低优先级的配置；

SpringBoot会从这四个位置全部加载主配置文件：互补配置

我们还可以通过spring.config.location来改变默认的配置文件位置

项目打包好后，我们可以使用命令行参数--spring.config.location的形式，启动项目时指定配置文件的新位置：指定的配置文件和默认加载的配置文件会共同起作用，形成互补配置。

java -jar *.jar --spring.config.location=G:/a/b/application.properties

## 7、外部配置加载顺序

SpringBoot也可以从以下位置加载配置：优先级从高到低，高优先级的配置覆盖低优先级的配置，所有的命令会形成互补配置

1. **命令行参数**

   java -jar *.jar --server.port=8888

   多个配置用空格分开：--配置项 --配置项

2. 来自java:comp/env的JNDI属性

3. Java系统属性（System.getProperties()）

4. 操作系统环境变量

5. RandomValuePropertySource配置的random.*属性值

   **由jar包外到jar包内进行寻找**

   **优先加载带profile**

6. **jar包外部的application-(profile).properties或application.yml(带spring.profile)配置文件**

7. **jar包内部的application-(profile).properties或application.yml(带spring.profile)配置文件**

   **再来加载不带profile**

8. **jar包外部的application.properties或application.yml(不带spring.profile)配置文件**

9. **jar包内部的application.properties或application.yml(不带spring.profile)配置文件**

10. @Configuration注解类上的@PropertySource

11. 通过SpringApplication.setDefaultProperties指定的默认属性

所以支持的加载来源可以参考[官方文档](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html#boot-features-external-config)

## 8、自动配置原理

配置文件到底能写什么？怎么写？自动配置原理？

[配置文件能配置的属性参照](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/appendix-application-properties.html#common-application-properties )

自动配置原理：

