package com.test.oabackend.controller;

import com.test.oabackend.common.Result;
import com.test.oabackend.service.EmpService;
import com.test.oabackend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmpService empService;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> body,HttpSession session){
        return Result.success(userService.login(body.get("username"),body.get("password"),session),"登录成功");
    }

    @PostMapping("/logout")
    public Result logout(HttpSession session){
        userService.logout(session);
        return Result.success(null,"退出成功");
    }

    @GetMapping("/info")
    public Result info(HttpSession session){
        return Result.success(userService.info(session));
    }

    @PutMapping("/password")
    public Result changePassword(@RequestBody Map<String,String> body,HttpSession session) {
        userService.changePassword(body.get("oldPassword"), body.get("newPassword"), session);
        return Result.success(null, "密码修改成功");
    }

    @PostMapping("/avatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file, HttpSession session) {
        return Result.success(userService.uploadAvatar(file, session), "头像上传成功");
    }
}
