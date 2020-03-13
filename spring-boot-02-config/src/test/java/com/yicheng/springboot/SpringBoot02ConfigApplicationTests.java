package com.yicheng.springboot;

import com.yicheng.springboot.bean.Person;
import com.yicheng.springboot.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * SpringBoot单元测试
 * 可以在测试期间很方便的类似编码一样进行自动注入等容器的功能
 */
@SpringBootTest
class SpringBoot02ConfigApplicationTests {

    @Autowired
    Person person;

    @Autowired
    ApplicationContext context;

    @Test
    public void testHelloService() {
        boolean flag = context.containsBean("helloService");
        System.out.println(flag);
    }

    @Test
    void contextLoads() {
        System.out.println(person);
    }
}
