package com.test.oabackend.controller;


import com.test.oabackend.common.Result;
import com.test.oabackend.domain.Dept;
import com.test.oabackend.common.CommonQuery;
import com.test.oabackend.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dept")

public class DeptController {
    @Autowired
    private DeptService deptService;
    @GetMapping("/page")
    public Result page(CommonQuery query) {
        return Result.success(deptService.page(query));
    }

    @GetMapping("/list")
    public Result list() {
        return Result.success(deptService.list());
    }

    @PostMapping
    public Result add(@RequestBody Dept dept) {
        deptService.add(dept);
        return Result.success(null, "新增成功");
    }

    @PutMapping
    public Result update(@RequestBody Dept dept) {
        deptService.update(dept);
        return Result.success(null, "修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        deptService.delete(id);
        return Result.success(null, "删除成功");
    }
}
