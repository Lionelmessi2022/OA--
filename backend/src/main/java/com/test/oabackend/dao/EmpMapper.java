package com.test.oabackend.dao;

import com.test.oabackend.common.CommonQuery;
import com.test.oabackend.domain.Emp;
import com.test.oabackend.domain.EmpQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmpMapper{
    Emp selectByUsername(String username);
    Emp selectById(Long id);
    List<Emp> selectPage(EmpQuery empQuery);

    Long countByQuery(EmpQuery empQuery);


    int insert (Emp emp);

    int update(Emp emp);

    int deleteById(Long id);

    int deleteBatch(@Param("ids") List<Long> ids);

    int updateStatus(@Param("id") Long id,@Param("status") Integer status);

    int maxEmpNoSeq();

    int updateAvatar(@Param("id")Long id,@Param("avatar")String avatar);

    int updatePassword(@Param("id")Long id,@Param("password")String password);
}

