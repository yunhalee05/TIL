package com.yunhalee.spring_db_practice.config;

import com.yunhalee.spring_db_practice.repository.ItemRepository;
import com.yunhalee.spring_db_practice.repository.jdbctemplate.JdbcTemplateItemRepositoryV2;
import com.yunhalee.spring_db_practice.repository.jdbctemplate.JdbcTemplateItemRepositoryV3;
import com.yunhalee.spring_db_practice.service.ItemService;
import com.yunhalee.spring_db_practice.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateV3Config {

    private final DataSource dataSource;

    public JdbcTemplateV3Config(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JdbcTemplateItemRepositoryV3(dataSource);
    }
}
