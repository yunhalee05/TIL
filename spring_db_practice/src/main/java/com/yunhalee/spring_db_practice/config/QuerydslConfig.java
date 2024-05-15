package com.yunhalee.spring_db_practice.config;

import com.yunhalee.spring_db_practice.repository.ItemRepository;
import com.yunhalee.spring_db_practice.repository.jpa.ItemQueryRepositoryV1;
import com.yunhalee.spring_db_practice.service.ItemService;
import com.yunhalee.spring_db_practice.service.ItemServiceV1;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    private final EntityManager em;

    public QuerydslConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new ItemQueryRepositoryV1(em);
    }

}
