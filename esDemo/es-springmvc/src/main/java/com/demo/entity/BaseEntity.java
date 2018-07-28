package com.demo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseEntity implements Serializable {
    /**
     * ID
     */
    private String id;


    public String index() {
        return getClass().getSimpleName().toLowerCase();
    }

    public String type() {
        return getClass().getSimpleName().toLowerCase();
    }
}