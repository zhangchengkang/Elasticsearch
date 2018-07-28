package com.demo.service;

import com.demo.entity.BaseEntity;
import com.demo.utils.TransportUtil;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.joda.time.DateTimeZone;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * @ClassName: VisitLogServiceImpl
 * @Description: impl
 * @author: ZhangChengKang
 * @date: 2018/7/27
 */
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
    //private static TransportClient client = TransportUtil.getConnection();
    private TransportClient getClient() {
        return TransportUtil.getConnection();
    }

    /**
     * 通过反射获得当前entity的class
     */
    private Class<T> getEntityClass() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) (parameterizedType.getActualTypeArguments()[0]);
    }

    private String getIndex() {
        return getEntityClass().getSimpleName().toLowerCase();
    }

    private String getType() {
        return getEntityClass().getSimpleName().toLowerCase();
    }

    public String addOne(T entity) {
        IndexRequestBuilder index = getClient().prepareIndex(entity.index(), entity.type());
        IndexResponse indexResponse = index.setSource(entity).get();
        return indexResponse.getId();
    }

    /**
     * MatchAllQuery 查询所有的数据
     */
    public SearchResponse matchAllQuery() {
        return search(QueryBuilders.matchAllQuery());
    }

    /**
     * MatchQuery 查询单一条件的数据
     */
    public SearchResponse matchQuery(String field, String value) {
        return search(QueryBuilders.matchQuery(field, value));
    }

    /**
     * MultiMatchQuery  在多个字段中查询一个关键字
     */
    public SearchResponse multiMatchQuery(String[] fields, String value) {
        return search(QueryBuilders.matchQuery(value, fields));
    }

    /**
     * 更多 查询 可 进QueryBuilders查看源码
     */
    private SearchResponse search(QueryBuilder queryBuilder) {
        return getClient().prepareSearch(getIndex()).setTypes(getType()).setQuery(queryBuilder).get();
    }

    /**
     * 复合查询
     * <p>
     * 所有的 must 子句必须匹配, 并且所有的 must_not 子句必须不匹配,
     * 但是多少 should 子句应该匹配呢? 默认的,可以不需要匹配任何 should 子句
     * 一种情况例外:如果没有must子句,就必须至少匹 配一个should子句。
     */
    public SearchResponse boolQuery(String field, Object from, Object to) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(field).from(from).to(to));
        return getClient().prepareSearch(getIndex()).setTypes(getType()).setQuery(boolQueryBuilder).get();
    }

    /**
     * 时间聚合
     */
    private SearchResponse timeAgg(String field, Long startTime, Long endTime) {
        AggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram("agg")//聚合出来的桶的名称  自定义
                .field(field)//字段
                .dateHistogramInterval(DateHistogramInterval.DAY)//分桶间距
                .minDocCount(0).extendedBounds(new ExtendedBounds(startTime, endTime))//在startTime和endTime间为0的也会被统计出来
                .format("yyyy-MM-dd")//将日期格式化
                .timeZone(DateTimeZone.forID("Asia/Shanghai"));//设置中国时区
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery(field).from(startTime).to(endTime);//查询startTime和endTime间的数据
        return aggSearch(queryBuilder, aggregationBuilder);
    }

    /**
     * Stats 聚合操作  会返回 count min max avg sum 的值
     */
    private SearchResponse statAgg(String field) {
        AggregationBuilder aggregationBuilder = AggregationBuilders.stats(field);
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return aggSearch(queryBuilder, aggregationBuilder);
    }

    /**
     * Filters  多过滤聚合，用多个过滤条件，来对当前文档进行过滤的聚合，每个过滤都包含所有满足它的文档个数，
     * <p>
     * result: "xiaomi": {"doc_count": 2 },"daier": { "doc_count": 1 }
     */
    private SearchResponse filterAgg(String field) {
        AggregationBuilder aggregationBuilder = AggregationBuilders.filters("agg", new FiltersAggregator.KeyedFilter("xiaomi", QueryBuilders.matchQuery("tag", "小米")), new FiltersAggregator.KeyedFilter("daier", QueryBuilders.matchQuery("tag", "戴尔")));
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        return aggSearch(queryBuilder, aggregationBuilder);
    }

    /**
     * 聚合查询
     * <p>
     * 更多  聚合操作 可进AggregationBuilders查看源码
     */
    private SearchResponse aggSearch(QueryBuilder queryBuilder, AggregationBuilder aggregationBuilder) {
        return getClient().prepareSearch(getIndex()).setTypes(getType()).setQuery(queryBuilder).addAggregation(aggregationBuilder).get();
    }
}