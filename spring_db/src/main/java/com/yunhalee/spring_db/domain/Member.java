package com.yunhalee.spring_db.domain;

import lombok.Data;

public class Member {

    private String memberId;
    private int money;

    public Member() {
    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }

    public String getMemberId() {
        return memberId;
    }

    public int getMoney() {
        return money;
    }
}
