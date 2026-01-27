package com.example.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {

    @Test
    public void scrapWeb() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String url = "https://www.bilibili.com/";
        String content =  webScrapingTool.scrapeWebPage(url);
        System.out.println(content);
    }
}