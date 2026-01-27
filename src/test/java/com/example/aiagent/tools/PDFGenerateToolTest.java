package com.example.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerateToolTest {

    @Test
    void generatePDF() {
        PDFGenerateTool pdfGenerateTool = new PDFGenerateTool();
        String filename = "火批大乱斗.pdf";
        String content = "玩火影忍者，就上 https://hyrz.qq.com/main.shtml";
        String result = pdfGenerateTool.generatePDF(filename, content);
        Assertions.assertNotNull(result);
    }
}