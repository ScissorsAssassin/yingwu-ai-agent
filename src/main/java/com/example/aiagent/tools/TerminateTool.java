package com.example.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 *
 * 让智能体进行终止任务工作
 */
public class TerminateTool {
    @Tool(description = """
                      "Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task."
                        "When you have finished all the tasks, call this tool to end this work"
            """
  )
    public String doTerminate(){
        return "任务终止";
    }
}
