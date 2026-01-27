package com.example.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebOperationToolTest {

    @Value("${search.api-key}")
    private String searchApikey;

    @Test
    public void searchOnLine() {
        WebOperationTool tool = new WebOperationTool(searchApikey);
        String query = "大熊猫";
        String content = tool.getWebContent(query);
        Assertions.assertNotNull(content);
        System.out.println(content);
    }
}