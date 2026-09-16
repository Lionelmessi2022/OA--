package com.test.oabackend.service;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

public interface AiService {
    String chat(String message, HttpSession session);
    List<Map<String, Object>> history(HttpSession session);
    void clear(HttpSession session);
}