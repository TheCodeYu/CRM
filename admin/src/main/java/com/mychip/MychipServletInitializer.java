package com.mychip;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 继承[SpringBootServletInitializer]可以使用外部tomcat，自己可以设置端口号，项目名。不需要用外部tomcat的话继承不继承都可以。
 * @author zhouyu
 */
public class MychipServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(MychipServletInitializer.class);
    }
}
