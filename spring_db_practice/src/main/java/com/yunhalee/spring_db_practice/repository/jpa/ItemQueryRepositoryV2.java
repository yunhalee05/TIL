package com.yunhalee.spring_db_practice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunhalee.spring_db_practice.domain.Item;
import com.yunhalee.spring_db_practice.domain.QItem;
import com.yunhalee.spring_db_practice.repository.ItemRepository;
import com.yunhalee.spring_db_practice.repository.ItemSearchCond;
import com.yunhalee.spring_db_practice.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.yunhalee.spring_db_practice.domain.QItem.item;

/**
 * QueryDSL
 */
@Repository
public class ItemQueryRepositoryV2 {

    private Logger log = LoggerFactory.getLogger(ItemQueryRepositoryV2.class);
    private final JPAQueryFactory query;

    public ItemQueryRepositoryV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond) {
        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();
        return query.selectFrom(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();
    }


    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }

}
