package com.plc.carrental.mybatis;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface PersistableEnumValue {

    @JsonProperty
    Integer getValue();
}
