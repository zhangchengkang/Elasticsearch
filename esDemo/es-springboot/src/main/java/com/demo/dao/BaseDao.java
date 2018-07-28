package com.demo.dao;

import com.demo.entity.BaseEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BaseDao<T extends BaseEntity> extends ElasticsearchRepository<T, String> {
}