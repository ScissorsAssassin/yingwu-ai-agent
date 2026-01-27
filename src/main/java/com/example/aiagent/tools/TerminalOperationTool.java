package com.example.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TerminalOperationTool {

    @Tool(description = "Execute a terminal command and return the output")
    public String executeTerminalCommand(@ToolParam(description = "Execute command") String command) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            // Process process = Runtime.getRuntime().exec(command);
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new RuntimeException("Terminal command failed with exit code " + exitCode);
                }
                process.destroy();
            }
        } catch (Exception e) {
            return "Error executing terminal command: " + e.getMessage();
        }

        return stringBuilder.toString();
    }
}