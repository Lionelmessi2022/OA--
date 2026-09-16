package com.test.oabackend.dao;

import com.test.oabackend.domain.Dept;
import com.test.oabackend.common.CommonQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptMapper {
    List<Dept> selectPage(CommonQuery query);

    long countByQuery(CommonQuery query);

    List<Dept> selectAll();

    Dept selectById(Long id);

    int insert(Dept dept);

    int update(Dept dept);

    int deleteById(Long id);

    // 根据部门ID统计员工数量
    long countEmpByDeptId(Long deptId);

}
