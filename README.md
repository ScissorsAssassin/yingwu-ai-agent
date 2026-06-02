# AI Agent 智能助手系统

> 基于 Spring Boot、Spring AI Alibaba、阿里云百炼 DashScope、RAG、多轮记忆、工具调用与 MCP 扩展能力构建的 LLM Agent 应用项目。项目面向真实交互场景，探索大模型在多轮对话、知识增强、外部工具调用和智能任务编排中的工程化落地方式。

---

## 1. 项目简介

本项目是一个基于大语言模型的智能体应用系统，当前包含 **AI 恋爱大师** 与 **AI 超级智能体** 两类应用形态：

- **AI 恋爱大师**：面向情感沟通、关系分析、对话建议等场景，支持多轮对话和上下文记忆。
- **AI 超级智能体**：面向更通用的任务处理场景，支持模型调用、知识库增强、工具调用和 MCP 能力扩展。

项目不是简单调用大模型接口，而是围绕 LLM Agent 的完整执行链路进行设计，包括：

1. 用户输入理解；
2. 会话上下文读取；
3. 多轮记忆管理；
4. RAG 知识库检索；
5. 大模型推理生成；
6. 工具调用与结果整合；
7. 前端页面展示与接口调试。

在后续扩展方向上，本项目也可以进一步增加 **Agent 执行 Trace 记录、推理链完整性核验、工具调用审计、知识来源追踪和结构化审计报告生成** 等能力，用于支撑 AI 安全、可解释 AI 与可审计推理研究。

---

## 2. 项目功能

### 2.1 多轮对话能力

系统支持基于会话 ID 的多轮对话管理，能够在连续交互中保留用户上下文，使模型回答不局限于单轮输入。

主要能力包括：

- 会话 ID 管理；
- 历史上下文读取；
- 多轮问题连续回答；
- 不同会话之间的上下文隔离。

### 2.2 记忆功能

项目设计了智能体记忆模块，用于保存和读取用户在历史交互中的关键信息，使 Agent 能够在多轮任务中保持状态。

可扩展方向：

- 短期对话记忆；
- 长期用户画像记忆；
- 场景化记忆召回；
- 记忆内容过滤与安全控制。

### 2.3 RAG 知识库增强

系统支持接入本地知识库，将外部文档内容转化为可检索知识，结合用户问题进行语义检索，再将检索结果注入模型上下文中生成回答。

典型流程如下：

```text
用户问题 → 查询改写/语义检索 → 召回知识片段 → 构造 Prompt → 大模型生成回答
```

该模块可以降低模型幻觉，提高回答的事实依据，也为后续进行知识来源追踪和回答审计提供基础。

### 2.4 工具调用能力

项目支持将外部工具封装为 Agent 可调用能力，使模型不仅能生成文本，还可以根据任务需要调用工具并整合工具结果。

工具调用流程示例：

```text
用户任务 → 模型判断是否需要工具 → 调用外部工具 → 获取工具返回结果 → 模型整合生成最终回答
```

后续可扩展工具包括：

- 搜索工具；
- 文件解析工具；
- 数据分析工具；
- 业务接口工具；
- 第三方 API 服务。

### 2.5 MCP 扩展能力

项目支持 MCP（Model Context Protocol）相关能力接入，用于将外部工具、数据源或服务以标准化方式提供给大模型智能体。

MCP 的引入可以让 Agent 更方便地连接外部系统，使其从“聊天机器人”扩展为“可调用工具、可执行任务、可连接业务系统”的智能体。

### 2.6 前端交互页面

项目包含 Web 端交互页面，支持用户选择不同智能体应用并进行对话测试。当前页面包括：

- AI Agent 智能助手首页；
- AI 恋爱大师对话页面；
- AI 超级智能体页面；
- 智能体回复展示区域；
- 用户输入与发送模块。

---

## 3. 技术栈

| 类型 | 技术 |
| --- | --- |
| 后端框架 | Spring Boot |
| 大模型框架 | Spring AI / Spring AI Alibaba |
| 大模型服务 | 阿里云百炼 DashScope / 通义千问系列模型 |
| Agent 能力 | Prompt 编排、多轮记忆、RAG、工具调用、MCP |
| 接口文档 | Knife4j / OpenAPI |
| 构建工具 | Maven |
| 开发语言 | Java |
| 辅助工具 | Lombok、Hutool |

---

## 4. 项目结构

项目主要代码结构如下：

```text
ai-agent
├── pom.xml                         # Maven 项目配置
├── README.md                       # 项目说明文档
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com/example/aiagent
│   │   │       ├── AiAgentApplication.java       # Spring Boot 启动类
│   │   │       ├── controller/                    # 对外接口层
│   │   │       ├── app/                           # 智能体应用编排层
│   │   │       ├── agent/                         # Agent 执行逻辑
│   │   │       ├── advisor/                       # Spring AI Advisor 扩展
│   │   │       ├── chatmemory/                    # 多轮记忆模块
│   │   │       ├── config/                        # 模型、工具、MCP 等配置
│   │   │       ├── rag/                           # RAG 知识库模块
│   │   │       ├── tools/                         # 工具调用模块
│   │   │       └── demo/invoke/                   # 模型调用示例
│   │   └── resources
│   │       ├── application.yml                    # 主配置文件
│   │       ├── application-local.yml              # 本地环境配置
│   │       ├── static/                            # 前端静态资源
│   │       └── templates/                         # 页面模板
│   └── test
│       └── java/com/example/aiagent               # 单元测试
└── target/                                        # Maven 构建输出目录
```

说明：不同版本代码结构可能略有差异，请以实际提交版本为准。

---

## 5. 快速开始

### 5.1 环境要求

请先确保本地安装以下环境：

- JDK 21+
- Maven 3.8+
- 阿里云百炼 DashScope API Key
- IntelliJ IDEA 或其他 Java IDE

### 5.2 克隆项目

```bash
git clone https://github.com/your-username/ai-agent.git
cd ai-agent
```

### 5.3 配置 API Key

不要将 API Key 直接写入代码或提交到 GitHub。建议使用环境变量：

```bash
export DASHSCOPE_API_KEY=your_api_key_here
```

Windows PowerShell：

```powershell
$env:DASHSCOPE_API_KEY="your_api_key_here"
```

然后在 `application.yml` 中使用环境变量读取：

```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
  chat:
    options:
      model: qwen-plus
```

### 5.4 启动项目

```bash
mvn spring-boot:run
```

默认服务地址：

```text
http://localhost:8123/api
```

接口文档地址：

```text
http://localhost:8123/api/doc.html
http://localhost:8123/api/swagger-ui.html
```

---

## 6. 核心流程

### 6.1 普通对话流程

```text
用户输入
  ↓
Controller 接收请求
  ↓
构造 Prompt 和上下文
  ↓
调用大模型
  ↓
返回模型回答
  ↓
前端展示结果
```

### 6.2 RAG 增强问答流程

```text
用户问题
  ↓
知识库语义检索
  ↓
召回相关文档片段
  ↓
将检索结果注入 Prompt
  ↓
调用大模型生成回答
  ↓
返回带有知识依据的结果
```

### 6.3 Agent 工具调用流程

```text
用户任务
  ↓
Agent 分析任务意图
  ↓
判断是否需要调用工具
  ↓
执行工具调用
  ↓
读取工具返回结果
  ↓
模型整合结果并生成最终回答
```

---

## 7. 项目亮点

### 7.1 从单轮问答扩展到 Agent 执行链路

项目不仅实现了基础的大模型问答，还围绕多轮记忆、知识库检索和工具调用构建了较完整的 Agent 执行流程。

### 7.2 支持 RAG 知识增强

通过知识库检索增强模型回答，使系统能够结合外部材料生成更有依据的回复，减少纯模型生成带来的不确定性。

### 7.3 支持工具调用与 MCP 扩展

系统具备调用外部工具和接入 MCP 服务的能力，为后续扩展复杂任务执行、业务系统联动和多工具协同提供基础。

### 7.4 具备可审计推理扩展基础

项目中的会话记录、知识检索结果、工具调用过程和最终回答可以进一步组织为结构化 Trace，为以下方向提供基础：

- 推理链完整性核验；
- 跨步骤前提一致性检测；
- 工具调用过程审计；
- RAG 知识来源追踪；
- Agent 执行报告生成。

---

## 8. 面向推理链核验的后续规划

为了进一步提升 Agent 的可靠性和可审计性，后续计划增加以下模块：

### 8.1 Agent Trace 记录模块

对每次 Agent 执行过程进行结构化记录，包括：

- 用户输入；
- 会话历史；
- 召回的知识片段；
- 调用的工具名称；
- 工具输入参数；
- 工具返回结果；
- 模型中间推理摘要；
- 最终回答。

示例 Trace：

```json
{
  "sessionId": "demo-session-001",
  "userInput": "帮我分析这个问题应该怎么处理",
  "memoryUsed": true,
  "retrievedDocs": [
    {
      "docId": "doc-001",
      "score": 0.87,
      "content": "..."
    }
  ],
  "toolCalls": [
    {
      "toolName": "searchTool",
      "input": "...",
      "output": "..."
    }
  ],
  "finalAnswer": "..."
}
```

### 8.2 Reasoning Template 推理模板

针对不同任务定义预期推理步骤，例如：

```text
问题理解 → 信息检索 → 证据筛选 → 逻辑分析 → 风险提示 → 最终建议
```

通过模板约束 Agent 的回答过程，避免模型直接跳到结论。

### 8.3 CoT Chain Verifier 推理链核验器

设计推理链完整性核验器，对 Agent 的实际执行过程进行检查：

- 是否遗漏关键步骤；
- 是否存在隐性假设；
- 是否出现前提漂移；
- 是否过度归纳；
- 是否缺少证据支撑。

### 8.4 Audit Report 审计报告生成

为每次 Agent 执行生成结构化审计报告，输出内容包括：

- 本次任务输入；
- 使用的记忆内容；
- 召回的知识来源；
- 工具调用记录；
- 推理完整性评分；
- 风险提示；
- 最终回答。

---

## 9. 注意事项

1. 上传 GitHub 前，请删除所有本地配置文件中的真实 API Key。
2. 不要提交 `target/`、`.idea/`、日志文件和本地临时文件。
3. 建议将敏感配置统一放入环境变量或 `.env` 文件中。
4. 如果项目包含个人隐私数据或测试数据，上传前应先脱敏处理。
5. 如果接入第三方模型或工具服务，应遵守对应平台的使用协议。

推荐 `.gitignore`：

```gitignore
# Maven
/target/

# IDE
.idea/
*.iml
.vscode/

# Logs
*.log
logs/

# Local config
.env
application-local.yml

# OS
.DS_Store
Thumbs.db
```

---

## 10. 项目截图

可以在仓库中新建 `docs/images/` 目录，并放入以下截图：

```text
docs/images/home.png
docs/images/love-agent.png
docs/images/super-agent.png
docs/images/tool-call.png
```

README 中引用示例：

```markdown
![首页](docs/images/home.png)
![AI 恋爱大师](docs/images/love-agent.png)
![AI 超级智能体](docs/images/super-agent.png)
```

---

## 11. 未来优化方向

- 增加用户画像与长期记忆管理；
- 增加知识库文档上传与自动向量化能力；
- 增加多工具协同调用能力；
- 增加 Agent 执行 Trace 可视化页面；
- 增加推理链完整性评分；
- 增加回答依据引用与知识来源展示；
- 增加工具调用权限控制与安全审计；
- 增加单元测试与接口测试覆盖率。

---

## 12. 作者

包维达  
西安电子科技大学 网络与信息安全学院  
研究方向：大模型应用、LLM Agent、RAG、AI 安全、可审计推理

---

## 13. License

本项目仅用于学习、科研和课程/项目展示。如需开源发布，建议根据实际情况选择 MIT、Apache-2.0 或其他开源协议。
