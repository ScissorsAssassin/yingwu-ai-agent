package com.example.aiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 联网搜索
 */
public class WebOperationTool {
    public static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";
    private final String apiKey;

    public WebOperationTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(description = "Search for information from Baidu Search Engine")
    public String getWebContent(@ToolParam(description = "Search key word") String query) {
        // 1. 构造请求参数
        Map<String, Object> param = new HashMap<>();

        param.put("q", query);
        param.put("api_key", apiKey);
        param.put("engine", "baidu");
        // 2. 发送请求
        try {
            String response = HttpUtil.get(SEARCH_API_URL, param);
            JSONObject jsonObject = JSONUtil.parseObj(response);
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            // 前5条
            List<Object> objects = organicResults.subList(0, 4);
            //拼接搜索结果为字符串
            String result = objects.stream().map(obj -> {
                JSONObject jsonObject1 = (JSONObject) obj;
                return jsonObject1.toString();
            }).collect(Collectors.joining(","));
            return result;
        } catch (Exception e) {
            return "Error search baidu: " + e.getMessage();
        }
    }
}