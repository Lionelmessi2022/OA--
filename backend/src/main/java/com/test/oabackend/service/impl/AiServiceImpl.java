package com.test.oabackend.service.impl;

import com.test.oabackend.service.AiService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiServiceImpl implements AiService {

    private static final Logger log = LoggerFactory.getLogger(AiServiceImpl.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final String SYSTEM_PROMPT = "你是公司 OA 系统的智能助手，负责回答考勤统计、公司制度、周报生成等办公问题。回答简洁、专业、使用中文。";

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    private ChatClient chatClient;

    @PostConstruct
    private void init() {
        this.chatClient = chatClientBuilder.defaultSystem(SYSTEM_PROMPT).build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String chat(String message, HttpSession session) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) session.getAttribute("aiHistory");
        if (list == null) {
            list = new ArrayList<>();
        }
        String time = LocalTime.now().format(TIME_FORMATTER);

        List<Message> messages = new ArrayList<>();
        for (Map<String, Object> m : list) {
            String content = String.valueOf(m.get("content"));
            if ("user".equals(m.get("role"))) {
                messages.add(new UserMessage(content));
            } else {
                messages.add(new AssistantMessage(content));
            }
        }
        messages.add(new UserMessage(message));

        String reply;
        long start = System.currentTimeMillis();
        try {
            reply = chatClient.prompt().messages(messages).call().content();
            log.info("MiMo 调用成功，耗时 {} ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("MiMo 调用失败，耗时 {} ms", System.currentTimeMillis() - start, e);
            reply = "AI 服务调用失败：" + e.getMessage();
        }
        if (reply == null || reply.isBlank()) {
            reply = "模型未返回内容，请稍后重试。";
        }

        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", message);
        userMsg.put("time", time);
        list.add(userMsg);
        Map<String, Object> aiMsg = new HashMap<>();
        aiMsg.put("role", "assistant");
        aiMsg.put("content", reply);
        aiMsg.put("time", time);
        list.add(aiMsg);
        session.setAttribute("aiHistory", list);
        return reply;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> history(HttpSession session) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) session.getAttribute("aiHistory");
        return list == null ? new ArrayList<>() : list;
    }

    @Override
    public void clear(HttpSession session) {
        session.removeAttribute("aiHistory");
    }
}