package com.yunhalee.spring_db_practice.repository.mybatis;

import com.yunhalee.spring_db_practice.domain.Item;
import com.yunhalee.spring_db_practice.repository.ItemRepository;
import com.yunhalee.spring_db_practice.repository.ItemSearchCond;
import com.yunhalee.spring_db_practice.repository.ItemUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class MyBatisItemRepository implements ItemRepository {

    private final Logger log = LoggerFactory.getLogger(MyBatisItemRepository.class);
    private final ItemMapper itemMapper;

    public MyBatisItemRepository(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public Item save(Item item) {
        log.info("ItemMapper class={}", itemMapper.getClass());
        itemMapper.save(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        itemMapper.update(itemId, updateParam);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemMapper.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        return itemMapper.findAll(cond);
    }
}
