package com.example.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.example.aiagent.constant.FileConstant;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class PDFGenerateTool {
    @Tool(description = "Generate a PDF with given content")
    public String generatePDF(
            @ToolParam(description = "Name of the file to save the generated PDF")
            String fileName,
            @ToolParam(description = "Content to be written in the PDF")
            String content) {
        // 设置下载地址
        String fileDir = FileConstant.FILE_SAVE_DIR + "/pdf";
        String filePath = fileDir + "/" + fileName ;

        try {
            FileUtil.mkdir(fileDir);
            try (PdfWriter pdfWriter = new PdfWriter(filePath);
                 PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                 Document document = new Document(pdfDocument)) {
                // 使用内置中文
                PdfFont pdfFont = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
                document.setFont(pdfFont);

                // 创建段落
                Paragraph paragraph = new Paragraph(content);
                document.add(paragraph);
            }
            return "PDF Generated Successfully to " + filePath;
        } catch (Exception e) {
            return "Error PDF Generated file: " + e.getMessage();
        }
    }
}