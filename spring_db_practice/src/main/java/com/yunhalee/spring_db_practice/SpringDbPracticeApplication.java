package com.yunhalee.spring_db_practice;

import com.yunhalee.spring_db_practice.config.*;
import com.yunhalee.spring_db_practice.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Import(V2Config.class)
@SpringBootApplication(scanBasePackages = {"com.yunhalee.spring_db_practice.web", "com.yunhalee.spring_db_practice.service.order", "com.yunhalee.spring_db_practice.repository.order", "com.yunhalee.spring_db_practice.service.member", "com.yunhalee.spring_db_practice.repository.member"})
public class SpringDbPracticeApplication {

    private final Logger log = LoggerFactory.getLogger(SpringDbPracticeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringDbPracticeApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(ItemRepository itemRepository) {
        return new TestDataInit(itemRepository);
    }

//    @Bean
//    @Profile("test")
//    public DataSource dataSource() {
//        log.info("메모리 데이터베이스 추가");
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//        return dataSource;
//    }
}
