package com.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.entity.VisitLogEntity;
import com.demo.service.VisitLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/visitlog/")
public class VisitLogController {
    @Resource
    private VisitLogService visitLogService;

    @RequestMapping(value = "findall", method = RequestMethod.GET)
    public JSONObject findAll() {
        JSONObject jsObject = new JSONObject();
        jsObject.put("data", visitLogService.findAll());
        return jsObject;
    }

    @RequestMapping(value = "findbyid/{id}", method = RequestMethod.GET)
    public JSONObject findById(@PathVariable String id) {
        JSONObject jsObject = new JSONObject();
        jsObject.put("data", visitLogService.findById(id));
        return jsObject;
    }

    @RequestMapping(value = "findbyipandcreatetime", method = RequestMethod.POST)
    public JSONObject findById(@RequestBody VisitLogEntity entity) {
        JSONObject jsObject = new JSONObject();
        jsObject.put("data", visitLogService.findByIpAndCreateTime(entity.getIp(), entity.getCreateTime()));
        return jsObject;
    }
}