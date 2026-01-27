package com.example.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalOperationToolTest {

    @Test
    public void executeTerminalCommand() {
        String command = "dir";
        TerminalOperationTool tool = new TerminalOperationTool();
        String res = tool.executeTerminalCommand(command);
        System.out.println(res);
    }
}