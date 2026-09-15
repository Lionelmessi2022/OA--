package com.test.oabackend.controller;

import com.test.oabackend.common.Result;
import com.test.oabackend.common.CommonQuery;
import com.test.oabackend.domain.Job;
import com.test.oabackend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/page")
    public Result page(CommonQuery query){
        return Result.success(jobService.page(query));
    }

    @GetMapping("/list")
    public Result list(){

        return Result.success(jobService.list());
    }

    @PostMapping
    public Result add(@RequestBody Job job){
        jobService.add(job);
        return Result.success(null,"新增成功");
    }

    @PutMapping
    public Result update(@RequestBody Job job){
        jobService.update(job);
        return Result.success(null,"修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        jobService.delete(id);
        return Result.success(null,"删除成功");
    }
}
