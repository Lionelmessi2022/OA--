package com.test.oabackend.dao;

import com.test.oabackend.common.CommonQuery;
import com.test.oabackend.domain.Job;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobMapper {

    List<Job> selectPage(CommonQuery query);

    long countByQuery(CommonQuery query);

    List<Job> selectAll();

    Job selectById(long id);

    int insert(Job job);

    int update(Job job);

    int deleteById(long id);

    long countEmpByJobId(long id);



}
