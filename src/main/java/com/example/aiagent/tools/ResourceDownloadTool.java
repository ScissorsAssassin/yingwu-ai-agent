package com.example.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;

import com.example.aiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;


public class ResourceDownloadTool {

    @Tool(description = "Download file from url")
    public String downloadResource(
            @ToolParam(description = "URL of the resource to download")
            String url,
            @ToolParam(description = "Name of the file to save the download resource")
            String fileName) {
        // 设置下载地址
        String fileDir = FileConstant.FILE_SAVE_DIR + "/download";
        String filePath = fileDir + "/" + fileName;
        // 下载文件
        try {
            FileUtil.mkdir(fileDir);
            HttpUtil.downloadFile(url, new File(filePath));
            return "download file: " + fileName + " success";
        } catch (Exception e) {
            return "Error download file " + e.getMessage();
        }
    }
}