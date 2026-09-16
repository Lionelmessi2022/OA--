package com.test.oabackend.service;

import com.test.oabackend.common.PageResult;
import com.test.oabackend.domain.Emp;
import com.test.oabackend.domain.EmpQuery;

import java.util.List;

public interface EmpService {

    PageResult<Emp> page(EmpQuery query);

    Emp get(Long id);

    void add(Emp emp);

    void update(Emp emp);

    void delete(Long id);

    void deleteBatch(List<Long> id);

    void updateStatus(Long id ,Integer status);


}
