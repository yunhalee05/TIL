package com.yunhalee.spring_db_practice.service;


import com.yunhalee.spring_db_practice.domain.Item;
import com.yunhalee.spring_db_practice.repository.ItemSearchCond;
import com.yunhalee.spring_db_practice.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);
}
