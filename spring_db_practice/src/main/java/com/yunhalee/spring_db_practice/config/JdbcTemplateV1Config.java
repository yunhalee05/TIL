package com.yunhalee.spring_db_practice.config;

import com.yunhalee.spring_db_practice.repository.ItemRepository;
import com.yunhalee.spring_db_practice.repository.jdbctemplate.JdbcTemplateItemRepositoryV1;
import com.yunhalee.spring_db_practice.service.ItemService;
import com.yunhalee.spring_db_practice.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateV1Config {

    private final DataSource dataSource;

    public JdbcTemplateV1Config(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JdbcTemplateItemRepositoryV1(dataSource);
    }
}
