package com.example.aiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ManusTest {

    @Resource
    Manus manus;
    @Test
    void manusTest() {
        String prompt = "请在西安雁塔区附近五公里找到适合约会的地点，结合一些网络图片，最后给我生成一份PDF文件";
        String answer = manus.run(prompt);
        Assertions.assertNotNull(answer);
    }

}