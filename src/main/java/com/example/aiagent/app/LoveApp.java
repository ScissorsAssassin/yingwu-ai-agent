package com.example.aiagent.app;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.example.aiagent.advisor.MyLoggerAdvisor;
//import com.example.aiagent.advisor.ReReadingAdvisor;
import com.example.aiagent.chatmemory.FileBasedChatMemory;
import com.example.aiagent.rag.LoveAppRagCloudAdvisorConfig;
import com.example.aiagent.rag.LoveAppRagCustomAdvisorFactory;
import com.example.aiagent.rag.LoveAppRewriteQueryTransformer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.client.ChatClient;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

@Component
@Slf4j
public class LoveApp {

    private static final String SYSTEM_PROMPT = "你是一个恋爱大师，请根据用户的回答解答恋爱问题。";
    private final ChatClient chatClient;
    private final ChatMemoryRepository chatMemoryRepository;
    private final DashScopeChatModel dashscopeChatModel;
    private final String baseSystemPrompt;
    private static final String CONVERSATION_ID_PARAM = "conversationId";

    public LoveApp(ChatClient.Builder chatClientBuilder, DashScopeChatModel dashscopeChatModel) throws IOException {

        //初始化基于文件对话记忆
//        String baseDir = System.getProperty("user.dir") + "/tmp/chat-memory";
//        ChatMemory chatMemory = new FileBasedChatMemory(baseDir);

        // 初始化内存存储的对话记忆仓库
        this.chatMemoryRepository = new InMemoryChatMemoryRepository();
        ChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .maxMessages(10)
                .build();
        this.baseSystemPrompt = "你是一个恋爱大师，请根据用户的回答解答恋爱问题。"; // 显式保存
        // 构建ChatClient并配置记忆功能
        this.chatClient = ChatClient.builder(dashscopeChatModel)
                // 配置记忆advisor
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor(),
                        new MyLoggerAdvisor())
                        //new ReReadingAdvisor())
                .defaultSystem(baseSystemPrompt)
                .build();
        this.dashscopeChatModel = dashscopeChatModel;
    }


    // 使用示例方法
    public String dochat(String userId, String message) {
        String conversationId = userId;
        ChatResponse chatResponse= chatClient
                .prompt()
                .user(message)
                .advisors(a -> a.param(CONVERSATION_ID_PARAM, conversationId))
                .call()
                .chatResponse();
        if (chatResponse != null) {
            return chatResponse.getResult().getOutput().getText();
        }
        return chatResponse.toString();
    }

record LoveReport(String title, List<String> suggestions){

}
    // 结构化输出测试
    public LoveReport doChatWithReport(String userId, String message) {
        String conversationId = userId;
        LoveReport loveReport= chatClient
                .prompt()
                .system( baseSystemPrompt + "每次对话后都要生成标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(a -> a.param(CONVERSATION_ID_PARAM, conversationId))
                .call()
                .entity(LoveReport.class);
        log.info("LoveReport {}", loveReport);
        return loveReport;
    }

    //本地RAG知识库问答功能
    @Resource
    private VectorStore loveAppVectorStore;
    @Resource
    private Advisor LoveAppRagCloudAdvisor;
    @Resource
    private LoveAppRewriteQueryTransformer  queryTransformer;

    public String chatWithRage(String userId, String message) {
        String conversationId = userId;
        //用户prompt重写转换
        //String rewrittenMessage = queryTransformer.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient
                //.prompt(rewrittenMessage)//重写版用户查询
                .prompt()
                .user(message)
                .advisors(a -> a.param(CONVERSATION_ID_PARAM, conversationId))
                //RAG本地实现
                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                //阿里云平台实现检索增强服务
                //.advisors(LoveAppRagCloudAdvisor)
//                .advisors(
//                        LoveAppRagCustomAdvisorFactory.FilterAdvisor(loveAppVectorStore, "单身")
//                        )
                .call()
                .chatResponse();
        if (chatResponse != null) {
            return chatResponse.getResult().getOutput().getText();
        }
        return chatResponse.toString();
    }

    @Resource
    private ToolCallback[] allTools;


    public String doChatWithTools(String message, String chatId) {
        String conversationId = chatId;
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .system(SYSTEM_PROMPT)
                .advisors(a -> a.param(CONVERSATION_ID_PARAM, conversationId))
                .toolCallbacks(allTools)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info(content);
        return content;
    }


    //AI调用MCP服务
    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    public String doChatWithMCP(String message, String chatId) {
        String conversationId = chatId;
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .system(SYSTEM_PROMPT)
                .advisors(a -> a.param(CONVERSATION_ID_PARAM, conversationId))
                .advisors(new MyLoggerAdvisor())
                .toolCallbacks(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info(content);
        return content;
    }
}
