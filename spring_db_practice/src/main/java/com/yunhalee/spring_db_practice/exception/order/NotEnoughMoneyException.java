package com.yunhalee.spring_db_practice.exception.order;

public class NotEnoughMoneyException  extends Exception{

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
