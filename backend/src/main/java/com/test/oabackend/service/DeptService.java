package com.test.oabackend.service;

import com.test.oabackend.common.PageResult;
import com.test.oabackend.domain.Dept;
import com.test.oabackend.common.CommonQuery;

import java.util.List;


public interface DeptService {

    PageResult<Dept> page(CommonQuery q);

    List<Dept> list();

    void add(Dept d);

    void update(Dept d);

    void delete(Long id);
}
