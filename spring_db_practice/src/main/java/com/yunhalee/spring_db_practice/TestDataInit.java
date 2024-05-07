package com.yunhalee.spring_db_practice;

import com.yunhalee.spring_db_practice.domain.Item;
import com.yunhalee.spring_db_practice.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public class TestDataInit {

    private static Logger log = LoggerFactory.getLogger(TestDataInit.class);

    private final ItemRepository itemRepository;

    public TestDataInit(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * 확인용 초기 데이터 추가
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
