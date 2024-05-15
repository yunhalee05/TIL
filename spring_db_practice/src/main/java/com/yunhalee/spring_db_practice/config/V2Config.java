package com.yunhalee.spring_db_practice.config;

import com.yunhalee.spring_db_practice.repository.ItemRepositoryV2;
import com.yunhalee.spring_db_practice.repository.jpa.ItemQueryRepositoryV2;
import com.yunhalee.spring_db_practice.repository.jpa.JpaItemRepositoryV1;
import com.yunhalee.spring_db_practice.repository.jpa.JpaItemRepositoryV2;
import com.yunhalee.spring_db_practice.repository.jpa.SpringDataJpaItemRepository;
import com.yunhalee.spring_db_practice.service.ItemService;
import com.yunhalee.spring_db_practice.service.ItemServiceV2;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class V2Config {

    private final EntityManager em;
    private final ItemRepositoryV2 itemRepository; // SpringDataJpa


    public V2Config(ItemRepositoryV2 itemRepository, EntityManager em) {
        this.itemRepository = itemRepository;
        this.em = em;
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV2(itemRepository, itemQueryRepository());
    }

    @Bean
    public JpaItemRepositoryV1 itemRepository() {
        return new JpaItemRepositoryV1(em);
    }

    @Bean
    public ItemQueryRepositoryV2 itemQueryRepository() {
        return new ItemQueryRepositoryV2(em);
    }


}
