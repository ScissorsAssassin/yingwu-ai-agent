package com.example.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoveAppMyKeywordEnricher {
    @Resource
    private ChatModel dashscopeChatModel;

    /**
     *基于AI的文档关键词增强器
     */
    public List<Document> loveAppMyKeywordEnricher(List<Document> documentlist) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashscopeChatModel, 5);
        return keywordMetadataEnricher.apply(documentlist);
    }
}
