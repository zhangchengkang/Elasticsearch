package com.demo.service;

import com.demo.dao.BaseDao;
import com.demo.entity.BaseEntity;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
    public abstract BaseDao<T> getBaseDao();

    public T save(T entity) {
        return getBaseDao().save(entity);
    }

    public Iterable<T> saveAll(List<T> entitys) {
        return getBaseDao().saveAll(entitys);
    }

    public void deleteById(String id) {
        Optional<T> entity = getBaseDao().findById(id);
        if (entity.isPresent()) {
            getBaseDao().deleteById(id);
        }
    }

    public void deleteAll(List<T> entitys) {
        if (!entitys.isEmpty()) {
            getBaseDao().deleteAll(entitys);
        }
    }

    public Optional<T> findById(String id) {
        return getBaseDao().findById(id);
    }

    public Iterable<T> findAll() {
        return getBaseDao().findAll();
    }

    public Iterable<T> findPageList(int pageNumber, int pageSize) {
        return getBaseDao().findAll(PageRequest.of(pageNumber, pageSize));
    }

    public Iterable<T> findPageList(QueryBuilder queryBuilder, Pageable pageable) {
        return getBaseDao().search(queryBuilder, pageable);
    }
}