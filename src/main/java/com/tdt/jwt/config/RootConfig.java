package com.tdt.jwt.config;

import com.tdt.jwt.dao.UserAuthDAO;
import com.tdt.jwt.model.UserAuth;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author
 **/
@Order(0)
@Configuration
@ComponentScan(basePackages = "com.tdt.jwt")
@PropertySource(value = "classpath:application.properties")
public class RootConfig {

    @Bean
    InitializingBean initializingBean(UserAuthDAO userAuthDAO) {

        return () -> userAuthDAO.save(UserAuth
                .builder()
                .username("admin")
                .tenantId("1")
                .password(new BCryptPasswordEncoder().encode("admin"))
                .build());
    }
}
