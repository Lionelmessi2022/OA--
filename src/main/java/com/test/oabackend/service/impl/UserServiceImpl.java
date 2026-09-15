package com.test.oabackend.service.impl;

import com.test.oabackend.common.BusinessException;
import com.test.oabackend.dao.DeptMapper;
import com.test.oabackend.dao.EmpMapper;
import com.test.oabackend.dao.JobMapper;
import com.test.oabackend.domain.Emp;
import com.test.oabackend.domain.LoginUser;
import com.test.oabackend.service.UserService;
import com.test.oabackend.utils.Md5Util;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private JobMapper jobMapper;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;
    @Override
    public LoginUser login(String userName, String password, HttpSession session) {
        // 实现登录逻辑

        //调用dao做登录的查询
        Emp emp = empMapper.selectByUsername(userName);
        if(emp == null || !emp.getPassword().equals(Md5Util.md5(password))){
            throw new BusinessException("用户名或密码错误");
        }
        if(emp.getStatus() != null && emp.getStatus()==0){
            throw new BusinessException("账号已禁用，请联系管理员");
        }
        //把emp对象转换成LoginUser对象
        LoginUser user = toLoginUser(emp);
        user.setToken(UUID.randomUUID().toString().replace("-", ""));
        session.setAttribute("LoginUser", user);
        return user;
    }

    @Override
    public LoginUser info(HttpSession session){
        LoginUser user = (LoginUser) session.getAttribute("LoginUser");
        if(user == null){
            throw new BusinessException("用户未登录");
        }
        return user;

    }

    @Override
    public void logout(HttpSession session){
        if(session != null){
            session.invalidate();
        }
    }

    @Override
    public void changePassword(String oldPwd,String newPwd,HttpSession session){
        LoginUser user = info(session);
        Emp emp = empMapper.selectById(user.getId());
        if (emp == null){
            throw new BusinessException("员工不存在");
        }
        if(!emp.getPassword().equals(Md5Util.md5(oldPwd))){
            throw new BusinessException("原密码错误");
        }
        empMapper.updatePassword(user.getId(),Md5Util.md5(newPwd));

    }
    @Override
    public String uploadAvatar(MultipartFile file, HttpSession session) {
        // 上传头像的完整实现见第四天"文件上传"专题，此处不展开
        LoginUser u = info(session);
        String originalFilename = file.getOriginalFilename();
        String ext = ".png";
        if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
        try {
            Files.createDirectories(Paths.get(uploadDir));
            File dest = Paths.get(uploadDir, fileName).toFile();
            file.transferTo(dest);
        } catch (IOException e) {
            throw new BusinessException("头像上传失败");
        }
        String url = baseUrl + "/uploads/" + fileName;
        empMapper.updateAvatar(u.getId(), url);
        u.setAvatar(url);
        session.setAttribute("loginUser", u);
        return url;
    }



    private LoginUser toLoginUser(Emp emp){
        LoginUser u = new LoginUser();
        u.setId(emp.getId());
        u.setUsername(emp.getUsername());
        u.setName(emp.getName());
        u.setGender(emp.getGender());
        u.setPhone(emp.getPhone());
        u.setEmail(emp.getEmail());
        u.setAvatar(emp.getAvatar() == null ? "" : emp.getAvatar());
        u.setDeptId(emp.getDeptId());
        u.setJobId(emp.getJobId());
        u.setDeptName(emp.getDeptName() == null ? "" : emp.getDeptName());
        u.setJobName(emp.getJobName() == null ? "" : emp.getJobName());
        u.setHireDate(emp.getHireDate());
        u.setRole(emp.getRole());
        return u;
    }
}
