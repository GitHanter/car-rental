package com.plc.carrental.entity;

import com.plc.carrental.mybatis.PersistableEnumValue;

public enum OrderStatus implements PersistableEnumValue {
    /**
     * When order generated, it's on this status
     */
    CONFIRMED(1),
    /**
     * When customer cancle order
     */
    CANCELLED(2),
    /**
     * When order completed
     */
    COMPLETED(3);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
