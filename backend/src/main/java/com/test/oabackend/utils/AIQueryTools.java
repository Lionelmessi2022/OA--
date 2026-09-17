package com.test.oabackend.utils;

import com.test.oabackend.common.PageResult;
import com.test.oabackend.domain.Dept;
import com.test.oabackend.domain.Emp;
import com.test.oabackend.domain.EmpQuery;
import com.test.oabackend.domain.Job;
import com.test.oabackend.service.DeptService;
import com.test.oabackend.service.EmpService;
import com.test.oabackend.service.JobService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

/**
 * OA 数据库查询工具（Spring AI Tool Calling）
 * 只提供只读查询，模型在对话中自主决定何时调用
 * 约定：统一返回可读字符串；异常时返回"查询失败:原因"，交由模型自行应对
 */
@Component
public class AIQueryTools {

    @Autowired
    private EmpService empService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private JobService jobService;

    @Tool(description = "查询公司所有部门列表，返回格式为 部门ID:部门名称，多条用分号分隔。仅用于了解公司有哪些部门；按部门过滤的查询工具已支持直接传部门名称，一般无需先调用本工具。")
    public String listDepartments() {
        try {
            StringJoiner joiner = new StringJoiner("; ");
            for (Dept d : deptService.list()) {
                joiner.add(d.getId() + ":" + d.getName());
            }
            return joiner.toString();
        } catch (Exception e) {
            return "查询失败：" + e.getMessage();
        }
    }

    @Tool(description = "查询公司所有职位列表，返回格式为 职位ID:职位名称，多条用分号分隔。")
    public String listJobs() {
        try {
            StringJoiner joiner = new StringJoiner("; ");
            for (Job j : jobService.list()) {
                joiner.add(j.getId() + ":" + j.getName());
            }
            return joiner.toString();
        } catch (Exception e) {
            return "查询失败：" + e.getMessage();
        }
    }

    @Tool(description = "一次性获取各部门人数分布，返回格式为 部门名称:人数，多条用分号分隔。比较各部门人数或了解公司结构时优先使用本工具，避免逐部门多次调用。")
    public String deptHeadcount() {
        try {
            StringJoiner joiner = new StringJoiner("; ");
            for (Dept d : deptService.list()) {
                EmpQuery q = new EmpQuery();
                q.setDeptId(d.getId());
                joiner.add(d.getName() + ":" + empService.count(q) + "人");
            }
            return joiner.toString();
        } catch (Exception e) {
            return "查询失败：" + e.getMessage();
        }
    }

    @Tool(description = "统计员工人数。可按部门名称过滤（直接传部门名称，如 技术部），不传则统计全公司。返回形如 共23人。")
    public String countEmployees(
            @ToolParam(description = "部门名称，如 技术部；不按部门过滤时不传", required = false) String deptName) {
        try {
            EmpQuery query = new EmpQuery();
            String err = applyDept(query, deptName);
            if (err != null) {
                return err;
            }
            return "共" + empService.count(query) + "人";
        } catch (Exception e) {
            return "查询失败：" + e.getMessage();
        }
    }

    @Tool(description = "统计指定入职日期区间内的新入职员工人数，日期格式 yyyy-MM-dd。返回形如 共3人。")
    public String countNewHires(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") String startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") String endDate) {
        try {
            EmpQuery query = new EmpQuery();
            query.setStartDate(startDate);
            query.setEndDate(endDate);
            return "共" + empService.count(query) + "人";
        } catch (Exception e) {
            return "查询失败：" + e.getMessage();
        }
    }

    @Tool(description = "查询指定入职日期区间内新入职员工的名单，返回 姓名(部门/职位/入职日期)，并注明总人数。日期格式 yyyy-MM-dd。")
    public String searchNewHires(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") String startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") String endDate,
            @ToolParam(description = "最多返回条数，默认10，最大20", required = false) Integer limit) {
        EmpQuery query = new EmpQuery();
        query.setStartDate(startDate);
        query.setEndDate(endDate);
        return pageEmployees(query, limit);
    }

    @Tool(description = "按姓名模糊查询和/或按部门名称查询员工名单，返回 姓名(部门/职位/入职日期/状态)，并注明总人数。状态1=在职,0=离职。")
    public String searchEmployees(
            @ToolParam(description = "员工姓名关键字，不按姓名过滤时不传", required = false) String name,
            @ToolParam(description = "部门名称，如 技术部；不按部门过滤时不传", required = false) String deptName,
            @ToolParam(description = "最多返回条数，默认10，最大20", required = false) Integer limit) {
        EmpQuery query = new EmpQuery();
        query.setName(name == null || name.isBlank() ? null : name);
        String err = applyDept(query, deptName);
        if (err != null) {
            return err;
        }
        return pageEmployees(query, limit);
    }

    @Tool(description = "按姓名模糊查询某个员工的详细档案（取第一个匹配），包含工号、性别、部门、职位、电话、邮箱、入职日期、在职状态。用户询问某人的具体信息时使用本工具。")
    public String getEmployeeDetail(
            @ToolParam(description = "员工姓名关键字，如 张三") String keyword) {
        try {
            EmpQuery query = new EmpQuery();
            query.setName(keyword == null || keyword.isBlank() ? null : keyword);
            query.setPage(1);
            query.setPageSize(1);
            List<Emp> records = empService.page(query).getRecords();
            if (records.isEmpty()) {
                return "未找到与'" + keyword + "'匹配的员工";
            }
            Emp e = records.get(0);
            return "工号:" + nvl(e.getEmpNo())
                    + "; 姓名:" + nvl(e.getName())
                    + "; 性别:" + (e.getGender() == null ? "未知" : (e.getGender() == 1 ? "男" : "女"))
                    + "; 部门:" + nvl(e.getDeptName())
                    + "; 职位:" + nvl(e.getJobName())
                    + "; 电话:" + nvl(e.getPhone())
                    + "; 邮箱:" + nvl(e.getEmail())
                    + "; 入职日期:" + e.getHireDate()
                    + "; 状态:" + (Integer.valueOf(1).equals(e.getStatus()) ? "在职" : "离职");
        } catch (Exception e) {
            return "查询失败：" + e.getMessage();
        }
    }

    private String applyDept(EmpQuery query, String deptName) {
        if (deptName == null || deptName.isBlank()) {
            return null;
        }
        for (Dept d : deptService.list()) {
            if (deptName.equals(d.getName())) {
                query.setDeptId(d.getId());
                return null;
            }
        }
        return "未找到部门'" + deptName + "'，请核对部门名称，或调用 listDepartments 查看全部部门";
    }

    private String pageEmployees(EmpQuery query, Integer limit) {
        try {
            int size = (limit == null || limit < 1) ? 10 : Math.min(limit, 20);
            query.setPage(1);
            query.setPageSize(size);
            PageResult<Emp> pr = empService.page(query);
            List<Emp> records = pr.getRecords();
            if (records.isEmpty()) {
                return "未查询到符合条件的员工";
            }
            StringJoiner joiner = new StringJoiner("; ");
            for (Emp e : records) {
                joiner.add(e.getName() + "(" + nvl(e.getDeptName()) + "/" + nvl(e.getJobName())
                        + "/入职" + e.getHireDate() + "/状态" + e.getStatus() + ")");
            }
            return "共" + pr.getTotal() + "人，展示前" + records.size() + "条：" + joiner;
        } catch (Exception e) {
            return "查询失败：" + e.getMessage();
        }
    }

    private String nvl(String s) {
        return s == null ? "未知" : s;
    }
}
