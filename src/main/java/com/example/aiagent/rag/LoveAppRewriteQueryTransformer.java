package com.example.aiagent.rag;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

@Component
public class LoveAppRewriteQueryTransformer {

    private final QueryTransformer queryTransformer;

    public LoveAppRewriteQueryTransformer(ChatModel dashscopeChatModel) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(dashscopeChatModel);

        queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();
    }

    public String doQueryRewrite(String prompt){
        Query query = new Query(prompt);
        Query  rewrittenquery = queryTransformer.transform(query);
        return rewrittenquery.toString();
    }
}
