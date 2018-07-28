package com.demo.service;

import com.demo.entity.VisitLogEntity;

import java.util.Date;
import java.util.List;

public interface VisitLogService extends BaseService<VisitLogEntity> {
    List<VisitLogEntity> findByIp(String ip);

    List<VisitLogEntity> findByIpNot(String ip);

    List<VisitLogEntity> findByIpLike(String ip);

    List<VisitLogEntity> findByIpAndCreateTime(String ip, Date createTime);
}