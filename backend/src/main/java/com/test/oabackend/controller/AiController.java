package com.test.oabackend.controller;

import com.test.oabackend.common.Result;
import com.test.oabackend.service.AiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiService aiService;
    @PostMapping("/chat")
    public Result chat(@RequestBody Map<String, String> body, HttpSession session) {
        String reply = aiService.chat(body.get("message"), session);
        return Result.success(Map.of("reply", reply));
    }

    @GetMapping("/history")
    public Result history(HttpSession session) {
        return Result.success(aiService.history(session));
    }

    @DeleteMapping("/history")
    public Result clear(HttpSession session) {
        aiService.clear(session);
        return Result.success(null, "聊天记录已清空");
    }
}
