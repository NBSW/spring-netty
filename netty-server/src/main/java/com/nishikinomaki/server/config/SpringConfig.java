package com.nishikinomaki.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
@Import({NettyConfig.class})
@ComponentScan(
        basePackages = "com.nishikinomaki.server",
        useDefaultFilters = false,
        includeFilters = {
            @Filter(type = FilterType.ANNOTATION, value = Component.class),
            @Filter(type = FilterType.ANNOTATION, value = Service.class)
        }
)


//因为spring4使用了jdk1.8的@Repeatable注解,jdk<1.8在编译的时候会产生一个警告,但是不会影响运行
@PropertySources(value = {@PropertySource("classpath:netty-server.properties")})
public class SpringConfig {

    @Autowired
    private AbstractApplicationContext context;

    /**
     * Necessary to make the Value annotations work.
     *
     * @return PropertySourcesPlaceholderConfigurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
