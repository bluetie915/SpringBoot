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

#### 5.2.1、父项目

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

#### 5.2.2、导入的依赖

~~~xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
~~~

spring-boot-starter-web：

​	spring-boot-starter：spring-boot场景启动器，帮我们导入了web模块正常运行所依赖的组件。

Spring Boot将所有的功能场景都抽取出来，做成一个个starters（启动器），只需要在项目里面引入这些starter，相关场景的所有依赖都会导入进来，要用什么功能就导入什么场景的启动器。

