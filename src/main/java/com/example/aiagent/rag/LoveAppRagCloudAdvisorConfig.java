package com.example.aiagent.rag;



import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@Slf4j
public class LoveAppRagCloudAdvisorConfig {
    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApiKey;

    @Bean
    public Advisor LoveAppRagCloudAdvisor(){
                // 1. 构建DashScopeApi
                DashScopeApi dashScopeApi = new DashScopeApi(
                        "https://dashscope.aliyuncs.com",  // baseUrl
                        new SimpleApiKey(dashScopeApiKey),       // apiKey
                        new LinkedMultiValueMap<>(),       // header
                        null,                              // workSpaceId (可选)
                        RestClient.builder(),              // restClientBuilder
                        WebClient.builder(),               // webClientBuilder
                        new DefaultResponseErrorHandler()  // errorHandler
                );

        final String KNOWLEDGE_INDEX = "恋爱大师";
        DocumentRetriever dashscopeDocumentRetriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(KNOWLEDGE_INDEX)
                        .build());
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(dashscopeDocumentRetriever)
                .build();
    }
}

