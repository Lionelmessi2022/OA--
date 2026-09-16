package com.test.oabackend.service.impl;

import com.test.oabackend.common.BusinessException;
import com.test.oabackend.common.PageResult;
import com.test.oabackend.dao.DeptMapper;
import com.test.oabackend.domain.Dept;
import com.test.oabackend.common.CommonQuery;
import com.test.oabackend.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;
    @Override
    public PageResult<Dept> page(CommonQuery q) {
        List<Dept> records = deptMapper.selectPage(q);
        long total = deptMapper.countByQuery(q);
        return new PageResult<>(records, total, q.getPage(), q.getPageSize());
    }

    @Override
    public List<Dept> list() {
        return deptMapper.selectAll();
    }

    @Override
    public void add(Dept d) {
        deptMapper.insert(d);
    }

    @Override
    public void update(Dept d) {
        deptMapper.update(d);
    }

    @Override
    public void delete(Long id) {
        if (deptMapper.countEmpByDeptId(id) > 0) {
            throw new BusinessException("该部门下存在员工，禁止删除");
        }
        deptMapper.deleteById(id);
    }
}
