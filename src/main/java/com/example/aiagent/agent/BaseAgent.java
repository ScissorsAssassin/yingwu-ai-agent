package com.example.aiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.example.aiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象基础类，用于管理代理状态和执行循环基础功能
 */
@Data
@Slf4j
public abstract class BaseAgent {
    /**
     * 代理名称
     */
    private String name;
    /**
     * 系统预设
     */
    private String systemPrompt;
    private String nextStepPrompt;

    /**
     * 模型信息
     */
    private ChatClient chatClient;
    /**
     * 存储AI聊天记录
     */
    private List<Message> messageList = new ArrayList<>();

    /**
     * 智能体运行状态
     */
    private AgentState state = AgentState.IDLE;

    /**
     * 执行步数
     */
    private Integer currentStep = 0;
    private Integer maxSteps = 10;


    public String run(String userPrompt) {
        // 判断状态是否为空闲状态
        if (this.state != AgentState.IDLE) {
            throw new RuntimeException("Can not run agent in current state: " + this.state);
        }
        // 判断用户预设是否为空
        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("User prompt can not be empty");
        }
        // 更新状态
        this.state = AgentState.RUNNING;
        // 记录消息上下文
        messageList.add(new UserMessage(userPrompt));
        // 保存消息
        List<String> results = new ArrayList<>();
        // 循环更新判断步数
        try {
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                int stepNum = i + 1;
                currentStep = stepNum;
                log.info("[CurrentStep {} / MaxSteps{}] Running...", stepNum, maxSteps);
                String result = step();
                result = "Step " + stepNum + ":" + result;
                results.add(result);
            }
            return StrUtil.join("\n", results);
        } catch (Exception e) {
            throw new RuntimeException("Error running agent", e);
        } finally {
            cleanup();
        }
    }

    /**
     * 执行单个步骤
     *
     * @return
     */
    public abstract String step();

    /**
     * 清除资源
     */
    protected void cleanup() {

    }
    
}