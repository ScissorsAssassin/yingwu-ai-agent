package com.example.aiagent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void dochat() {
        String chatId = "test_" + UUID.randomUUID().toString();

        // 第一次提问
        String answer1 = loveApp.dochat(chatId, "我分手了怎么办，她叫烟雨");
        System.out.println("回答1: " + answer1);
        Assertions.assertNotNull(answer1);

        // 第二次提问（依赖记忆）
        String answer2 = loveApp.dochat(chatId, "我刚才说的女友叫什么名字？");
        System.out.println("回答2: " + answer2);
        Assertions.assertTrue(answer2.contains("烟雨")); // 验证记忆功能

        // 第三次提问
        String answer3 = loveApp.dochat(chatId, "我现在应该怎么办？");
        System.out.println("回答3: " + answer3);
        Assertions.assertNotNull(answer3);
    }

    @Test
    void doChatWithReport() {
        String chatId = "test_" + UUID.randomUUID().toString();
        String message = "你好，我想在圣诞节赠送我的另一半礼物，应该怎么做？";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(chatId, message);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void chatWithRage() {
        String chatId = "test_" + UUID.randomUUID().toString();
        String message = ("我现在处于已婚状态如何维持关系？");
        String answer = loveApp.chatWithRage(chatId, message);
        System.out.println(answer);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        String chatId = UUID.randomUUID().toString();
        String message = "给我生成澳门的旅游的景点PDF，包含时间、计划和费用";
        loveApp.doChatWithTools(message, chatId);
    }

    @Test
    void doChatWithMCP() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我搜索一些能哄另一半开心的图片";
        loveApp.doChatWithMCP(message, chatId);
    }
}