package com.example.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {
    @Test
    public void download() {
        String url = "https://static.gametalk.qq.com/image/25/1767578508_2825574c1a9e2f780afa0113c27791ca.png";
        String fileName = "obito.png";
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String res = tool.downloadResource(url, fileName);
        System.out.println(res);
    }
}