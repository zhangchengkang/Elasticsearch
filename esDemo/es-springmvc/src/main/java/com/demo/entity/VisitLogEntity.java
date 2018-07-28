package com.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName: visitLogEntity
 * @Description:
 * @author: ZhangChengKang
 * @date: 2018/7/27
 */
@Data
public class VisitLogEntity extends BaseEntity {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 訪問IP
     */
    private String ip;
    /**
     * 访问时间
     */
    private Date createTime;
}