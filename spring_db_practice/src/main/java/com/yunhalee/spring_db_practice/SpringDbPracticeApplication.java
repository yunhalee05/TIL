package com.yunhalee.spring_db_practice;

import com.yunhalee.spring_db_practice.config.JdbcTemplateV1Config;
import com.yunhalee.spring_db_practice.config.JdbcTemplateV2Config;
import com.yunhalee.spring_db_practice.config.MemoryConfig;
import com.yunhalee.spring_db_practice.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Import(JdbcTemplateV2Config.class)
@SpringBootApplication(scanBasePackages = "com.yunhalee.spring_db_practice.web")
public class SpringDbPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDbPracticeApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(ItemRepository itemRepository) {
        return new TestDataInit(itemRepository);
    }
}
