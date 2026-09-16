package com.test.oabackend.controller;

import com.test.oabackend.common.CommonQuery;
import com.test.oabackend.common.Result;
import com.test.oabackend.domain.Emp;
import com.test.oabackend.domain.EmpQuery;
import com.test.oabackend.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emp")

public class EmpController {

    @Autowired
    private EmpService empService;

    @GetMapping("/page")
    public Result page(EmpQuery query){
        return Result.success(empService.page(query));
    }

    @GetMapping("/get")
    public Result get(@PathVariable Long id){
        return Result.success(empService.get(id));
    }

    @PostMapping
    public Result add(@RequestBody Emp emp){
        empService.add(emp);
        return Result.success(null, "新增成功，初始密码为 123456");
    }

    @PutMapping
    public Result update(@RequestBody Emp emp){
        empService.update(emp);
        return Result.success(null, "修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        empService.delete(id);
        return Result.success(null, "删除成功");
    }

    @DeleteMapping("/batch")
    public Result deleteBatch(@RequestBody List<Long> ids){
        empService.deleteBatch(ids);
        return Result.success(null, "批量删除成功");
    }

    @PutMapping("/status/{id}")
    public Result updateStatus(@PathVariable Long id, @RequestParam Integer status){
        empService.updateStatus(id, status);
        return Result.success(null, "状态修改成功");
    }

}
