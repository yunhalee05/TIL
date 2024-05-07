package com.yunhalee.spring_db_practice.repository;


public class ItemSearchCond {

    private String itemName;
    private Integer maxPrice;

    public ItemSearchCond() {
    }

    public ItemSearchCond(String itemName, Integer maxPrice) {
        this.itemName = itemName;
        this.maxPrice = maxPrice;
    }


    public String getItemName() {
        return itemName;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }
}
