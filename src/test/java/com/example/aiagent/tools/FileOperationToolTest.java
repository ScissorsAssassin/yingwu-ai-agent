package com.example.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class FileOperationToolTest {

    @Test
    public void writeFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "智能体AI.txt";
        String content = " 我是测试文件内容";
        String res = tool.writeFile(fileName, content);
        System.out.println(res);
    }

    @Test
    public void readFile(){
        FileOperationTool tool = new FileOperationTool();
        String fileName = "智能体AI.txt";
        String s = tool.readFile(fileName);
        System.out.println(s);
    }
}