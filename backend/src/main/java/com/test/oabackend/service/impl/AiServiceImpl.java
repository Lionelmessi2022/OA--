package com.test.oabackend.service.impl;

import com.test.oabackend.domain.LoginUser;
import com.test.oabackend.service.AiService;
import com.test.oabackend.utils.AIQueryTools;
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

import java.time.LocalDate;
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
    private static final String SYSTEM_PROMPT =
            "你是公司 OA 系统的智能办公助手“小欧”，人设是一位熟悉公司业务、好相处的同事：语气自然亲切，可以偶尔带一点轻幽默或表情，但涉及工作数据时严谨不含糊。"
            + "回复规范：1) 先给结论再给细节，数据类回答用换行列表展示，不要用代码表格；"
            + "2) 回答结束后，主动追问一个相关问题或给出一条建议，引导对话继续；"
            + "3) 涉及人数、名单、部门、职位、入职等真实数据时，必须调用数据库查询工具获取真实数据后再回答，严禁编造；工具返回“查询失败”或空结果时，如实说明并给出替代角度；"
            + "4) 制度、流程类知识问题可直接回答，但不要承诺考勤等没有数据支撑的能力；"
            + "5) 不要向用户提及工具、数据库、接口等技术细节；6) 全程中文，篇幅适中。";

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @Autowired
    private AIQueryTools aiQueryTools;

    private ChatClient chatClient;

    @PostConstruct
    private void init() {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultTools(aiQueryTools)
                .build();
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

        LoginUser loginUser = (LoginUser) session.getAttribute("LoginUser");
        StringBuilder dyn = new StringBuilder("今天日期：").append(LocalDate.now()).append("。");
        if (loginUser != null) {
            dyn.append("当前与你对话的员工是：").append(loginUser.getName())
                    .append("（").append(loginUser.getDeptName() == null ? "" : loginUser.getDeptName())
                    .append("/").append(loginUser.getJobName() == null ? "" : loginUser.getJobName())
                    .append("），你可以称呼其姓名或职位，让对话更亲切。");
        }

        String reply;
        long start = System.currentTimeMillis();
        try {
            reply = chatClient.prompt().system(dyn.toString()).messages(messages).call().content();
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