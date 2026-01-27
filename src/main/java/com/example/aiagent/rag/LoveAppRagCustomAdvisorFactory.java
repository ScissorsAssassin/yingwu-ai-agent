package com.example.aiagent.rag;



import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.expression.Expression;
import org.springframework.expression.common.ExpressionUtils;

public class LoveAppRagCustomAdvisorFactory {

    /**
     * 自定义的RAG检索增强顾问
     * @param vectorStore
     * @param status
     * @return
     */
    public static Advisor FilterAdvisor(VectorStore vectorStore, String status) {
        //根据特定状态进行过滤的条件
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();

        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .filterExpression(expression)
                .vectorStore(vectorStore)
                .topK(3)
                .similarityThreshold(0.4)
                .build();

        return  RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .queryAugmenter(LoveAppContextualQueryAugmenterFactory.createInstance())
                .build();
    }
}
