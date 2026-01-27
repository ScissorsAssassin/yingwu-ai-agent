package com.example.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 向量数据库的初始化(初始化基于内存的向量数据库Bean)
 */

@Configuration
public class LoveAppVectorStoreConfig {
    @Resource
    private LoveAppDocumentReader loveAppDocumentReader;
    @Resource
    private LoveAppMyTokenTextSplitter loveAppMyTokenTextSplitter;
    @Resource
    private LoveAppMyKeywordEnricher loveAppMyKeywordEnricher;

    @Bean
    public VectorStore loveAppVectorStore(EmbeddingModel dashscopeembeddingmodel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeembeddingmodel).build();

        //加载文档
        List<Document> documentlist = loveAppDocumentReader.loadMarkdown();
        //未采用智能拆分的格式
        //simpleVectorStore.add(documentlist);

        //采用自主切分的文档格式
//        List<Document> splittedDocumentlist = loveAppMyTokenTextSplitter.splitDocuments(documentlist);
//        simpleVectorStore.add(splittedDocumentlist);

        //基于AI的元信息关键词增强器，自动补充更多指定数量的关键词信息
        List<Document> enrichedDocumentlist = loveAppMyKeywordEnricher.loveAppMyKeywordEnricher(documentlist);
        simpleVectorStore.add(enrichedDocumentlist);

        return simpleVectorStore;
    }
}
