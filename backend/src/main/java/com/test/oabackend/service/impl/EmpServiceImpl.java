package com.test.oabackend.service.impl;

import com.test.oabackend.common.PageResult;
import com.test.oabackend.dao.EmpMapper;
import com.test.oabackend.common.CommonQuery;
import com.test.oabackend.domain.Emp;
import com.test.oabackend.domain.EmpQuery;
import com.test.oabackend.service.EmpService;
import com.test.oabackend.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Value("${file.base-url}")
    private String baseUrl;
    @Override
    public PageResult<Emp> page(EmpQuery query) {
        List<Emp> records = empMapper.selectPage(query);
        long total = empMapper.countByQuery(query);
        return new PageResult<>(records, total, query.getPage(), query.getPageSize());
    }

    @Override
    public Long count(EmpQuery query) {
        return empMapper.countByQuery(query);
    }

    @Override
    public Emp get(Long id) {
        Emp emp = empMapper.selectById(id);
        if(emp == null){
            throw new RuntimeException("员工不存在");
        }
        return emp;
    }

    @Override
    public void add(Emp emp) {
        int seq = empMapper.maxEmpNoSeq()+1;
        emp.setEmpNo(String.format("EMP%03d", seq));
        if(emp.getUsername() == null || emp.getUsername().isEmpty()){
            emp.setUsername(emp.getEmpNo());
        }
        emp.setPassword(Md5Util.md5("123456"));
        emp.setRole("employee");
        emp.setStatus(1);
        if(emp.getAvatar()==null){
            emp.setAvatar("");
        }
        empMapper.insert(emp);
    }

    @Override
    public void update(Emp emp) {
        try {empMapper.update(emp);
        } catch (Exception e) {
            throw new RuntimeException("修改失败");
        }

    }

    @Override
    public void delete(Long id) {
        try {empMapper.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("删除失败");
        }

    }

    @Override
    public void deleteBatch(List<Long> ids) {
        if(ids !=null && !ids.isEmpty()){
            empMapper.deleteBatch(ids);
        }else {
            throw new RuntimeException("删除失败");
        }


    }

    @Override
    public void updateStatus(Long id, Integer status) {
        empMapper.updateStatus(id, status);

    }


}
