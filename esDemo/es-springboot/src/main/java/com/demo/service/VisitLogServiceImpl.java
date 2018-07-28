package com.demo.service;

import com.demo.dao.BaseDao;
import com.demo.dao.VisitLogDao;
import com.demo.entity.VisitLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VisitLogServiceImpl extends BaseServiceImpl<VisitLogEntity> implements VisitLogService {
    @Autowired
    private VisitLogDao visitLogDao;

    @Override
    public BaseDao<VisitLogEntity> getBaseDao() {
        return visitLogDao;
    }

    @Override
    public List<VisitLogEntity> findByIp(String ip) {
        return visitLogDao.findByIp(ip);
    }

    @Override
    public List<VisitLogEntity> findByIpNot(String ip) {
        return visitLogDao.findByIpNot(ip);
    }

    @Override
    public List<VisitLogEntity> findByIpLike(String ip) {
        return visitLogDao.findByIpLike(ip);
    }

    @Override
    public List<VisitLogEntity> findByIpAndCreateTime(String ip, Date createTime) {
        return visitLogDao.findByIpAndCreateTime(ip, createTime);
    }
}