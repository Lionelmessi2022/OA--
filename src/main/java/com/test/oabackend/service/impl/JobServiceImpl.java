package com.test.oabackend.service.impl;

import com.test.oabackend.common.BusinessException;
import com.test.oabackend.common.PageResult;
import com.test.oabackend.dao.JobMapper;
import com.test.oabackend.common.CommonQuery;
import com.test.oabackend.domain.Job;
import com.test.oabackend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;

    @Override
    public PageResult<Job> page(CommonQuery query) {
        List<Job> records = jobMapper.selectPage(query);
        long total = jobMapper.countByQuery(query);
        return new PageResult<>(records,total,query.getPage(),query.getPageSize());
    }

    @Override
    public List<Job> list() {
        return jobMapper.selectAll();
    }

    @Override
    public void add(Job job) {
        jobMapper.insert(job);
    }

    @Override
    public void update(Job job) {
        jobMapper.update(job);
    }

    @Override
    public void delete(Long id) {
        if(jobMapper.countEmpByJobId(id)>0){
            throw new BusinessException("该职位下存在员工，禁止删除");
        }
        jobMapper.deleteById(id);
    }

}
