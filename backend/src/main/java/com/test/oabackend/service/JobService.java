package com.test.oabackend.service;

import com.test.oabackend.common.PageResult;
import com.test.oabackend.common.CommonQuery;
import com.test.oabackend.domain.Job;

import java.util.List;

public interface JobService {

    //分页查询
    PageResult<Job> page(CommonQuery query);
    //单条查询
    List<Job> list();

    void add(Job job);

    void update(Job job);

    void delete(Long id);



}
