package com.example.aiagent.agent;

import cn.hutool.core.collection.CollUtil;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.example.aiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理工具调用的基础类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {
    // 可调用的工具
    private final ToolCallback[] toolCallbacks;
    // 调用响应
    private ChatResponse chatResponse;
    // 工具调用管理者
    private final ToolCallingManager toolCallingManager;

    private ChatOptions chatOptions;

    // 构造方法停止Spring AI内部的chat options
    public ToolCallAgent(ToolCallback[] toolCallbacks) {
        this.toolCallbacks = toolCallbacks;
        this.toolCallingManager = ToolCallingManager.builder().build();
        // 禁用系统内部Spring AI的思考和行动
        this.chatOptions = DashScopeChatOptions.builder()
                .withInternalToolExecutionEnabled(false)
                .build();

    }

    @Override
    public boolean think() {
        // 1. 判断预设是否为空
        if (getNextStepPrompt() != null && getSystemPrompt() != null) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            // 添加消息
            getMessageList().add(userMessage);
        }
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        // 2.获取响应
        try {
            this.chatResponse = getChatClient()
                    .prompt(prompt)
                    .toolCallbacks(toolCallbacks)
                    //.tools(toolCallbacks)
                    .system(getSystemPrompt())
                    .call().chatResponse();

            // 3. 处理响应
            AssistantMessage assistantMessage = this.chatResponse.getResult().getOutput();
            String thinkResult = assistantMessage.getText();
            List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();
            log.info(getName() + " 思考 " + thinkResult);
            log.info(getName() + " 执行了" + toolCalls.size() + "个工具调用");
            // 4. 汇总工具调用信息，重新写入消息
            String toolCallInfo = toolCalls.stream()
                    .map(toolCall -> String.format("工具名称: %s,参数: %s", toolCall.name(), toolCall.arguments()))
                    .collect(Collectors.joining("\n"));
            log.info(getName() + " 工具调用信息: \n" + toolCallInfo);

            if (toolCallInfo.isEmpty()) {
                getMessageList().add(assistantMessage);
                return false;
            } else {
                // 需要调用的时候无需记录助手消息，调用工具会自动记录
                return true;
            }
        } catch (Exception e) {
            log.error(getName() + "助手思考出错" + e.getMessage());
            getMessageList().add(new AssistantMessage(getName() + "助手思考出错" + e.getMessage()));
            return false;
        }
    }

    @Override
    public String act() {
        // 判断是否有工具调用
        if (!chatResponse.hasToolCalls()) {
            return "no-tool-calls";
        }
        // 工具调用，设置消息
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse);
        setMessageList(toolExecutionResult.conversationHistory());
        // 返回工具调用结果，取最后一条消息
        ToolResponseMessage responseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        String res = responseMessage.getResponses().stream().map(response -> "工具" + response.name() + "调用结果：" + response.responseData())
                .collect(Collectors.joining("\n"));
        log.info(res);
        // 如果结束就终止运行
        boolean terminateToolCalled = responseMessage.getResponses().stream().anyMatch(response -> "doTerminate".equals(response.name()));
        if (terminateToolCalled) {
            setState(AgentState.FINISHED);
        }
        log.info("do terminate toll" + terminateToolCalled);
        return res;
    }
}
