package com.example.aiagent.rag;



import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;



@Component
public class LoveAppMyTokenTextSplitter {
    public List<Document> splitDocuments(List<Document> documents) {
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        return tokenTextSplitter.apply(documents);
    }

    public List<Document> splitCustomized(List<Document> documents) {
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(200, 100, 10, 500, true);
        return tokenTextSplitter.apply(documents);
    }

}



