package com.example.aiagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.boot.autoconfigure.jms.JmsProperties;

public class LoveAppContextualQueryAugmenterFactory {

    public static ContextualQueryAugmenter createInstance(){
        PromptTemplate emptyPrompt = new PromptTemplate
                ("你应该输出以下内容：" +
                        "很抱歉，您输入的问题不在我的知识库范围以内，" +
                        "如果有更多疑问，请咨询570036114@qq.com");

        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(emptyPrompt)
                .build();
    }
}
