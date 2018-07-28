package com.demo.service;

import com.demo.entity.BaseEntity;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity> {
    T save(T entity);

    Iterable<T> saveAll(List<T> entitys);

    void deleteById(String id);

    void deleteAll(List<T> entitys);

    Optional<T> findById(String id);

    Iterable<T> findAll();

    Iterable<T> findPageList(int pageNumber, int pageSize);

    Iterable<T> findPageList(QueryBuilder query, Pageable pageable);
}