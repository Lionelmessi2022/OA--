package com.test.oabackend.service;



import com.test.oabackend.domain.LoginUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {

    LoginUser login(String userName, String password, HttpSession session);
    void logout(HttpSession session);
    LoginUser info(HttpSession session);
    void changePassword(String oldPassword,String newPassword,HttpSession session);
    String uploadAvatar(MultipartFile file, HttpSession session);
}
