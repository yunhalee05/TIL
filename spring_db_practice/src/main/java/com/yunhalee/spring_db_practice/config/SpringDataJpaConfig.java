package com.yunhalee.spring_db_practice.config;

import com.yunhalee.spring_db_practice.repository.ItemRepository;
import com.yunhalee.spring_db_practice.repository.jpa.JpaItemRepositoryV1;
import com.yunhalee.spring_db_practice.repository.jpa.JpaItemRepositoryV2;
import com.yunhalee.spring_db_practice.repository.jpa.SpringDataJpaItemRepository;
import com.yunhalee.spring_db_practice.service.ItemService;
import com.yunhalee.spring_db_practice.service.ItemServiceV1;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDataJpaConfig {

    private final SpringDataJpaItemRepository springDataJpaItemRepository;

    public SpringDataJpaConfig(SpringDataJpaItemRepository springDataJpaItemRepository) {
        this.springDataJpaItemRepository = springDataJpaItemRepository;
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV2(springDataJpaItemRepository);
    }

}
