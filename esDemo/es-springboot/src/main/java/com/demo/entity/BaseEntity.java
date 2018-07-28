package com.demo.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = "baseEntity", type = "baseEntity")
@Data
public abstract class BaseEntity implements Serializable {
    /**
     * ID
     */
    private String id;


}