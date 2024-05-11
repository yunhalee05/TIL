package com.yunhalee.spring_db_practice.config;

import com.yunhalee.spring_db_practice.repository.ItemRepository;
import com.yunhalee.spring_db_practice.repository.memory.MemoryItemRepository;
import com.yunhalee.spring_db_practice.repository.mybatis.ItemMapper;
import com.yunhalee.spring_db_practice.repository.mybatis.MyBatisItemRepository;
import com.yunhalee.spring_db_practice.service.ItemService;
import com.yunhalee.spring_db_practice.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    private final ItemMapper itemMapper;

    public MyBatisConfig(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MyBatisItemRepository(itemMapper);
    }

}
