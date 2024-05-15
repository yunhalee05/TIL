package com.yunhalee.spring_db_practice.service;

import com.yunhalee.spring_db_practice.domain.Item;
import com.yunhalee.spring_db_practice.repository.ItemRepositoryV2;
import com.yunhalee.spring_db_practice.repository.ItemSearchCond;
import com.yunhalee.spring_db_practice.repository.ItemUpdateDto;
import com.yunhalee.spring_db_practice.repository.jpa.ItemQueryRepositoryV2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemServiceV2 implements ItemService {

    private final ItemRepositoryV2 itemRepository;
    private final ItemQueryRepositoryV2 itemQueryRepository;


    public ItemServiceV2(ItemRepositoryV2 itemRepository, ItemQueryRepositoryV2 itemQueryRepository) {
        this.itemRepository = itemRepository;
        this.itemQueryRepository = itemQueryRepository;
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = findById(itemId).orElseThrow();
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCond cond) {
        return itemQueryRepository.findAll(cond);
    }
}
