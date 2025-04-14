# OpenAI 示例模块

本模块演示如何使用 LangChain4j 与 OpenAI 的各种模型进行交互，包含完整的 API 使用示例。

## 模块结构

### 核心文件说明

- **pom.xml**  
  Maven 项目配置文件，包含以下关键依赖：
  - `langchain4j-open-ai`: OpenAI 集成核心库
  - `langchain4j`: LangChain4j 基础功能
  - `tinylog`: 轻量级日志实现

- **ApiKeys.java**  
  OpenAI API 密钥管理类，优先从环境变量`OPENAI_API_KEY`读取密钥，未设置时使用演示密钥

### 功能示例

1. **OpenAiChatModelExamples.java**  
   演示聊天模型使用，包含：
   - 简单文本交互
   - 图片内容理解
   - 通用参数配置
   - OpenAI 专属参数配置

2. **OpenAiEmbeddingModelExamples.java**  
   文本嵌入模型示例，展示如何将文本转换为向量表示

3. **OpenAiImageModelExamples.java**  
   DALL-E 图像生成示例，根据文本描述创建图片

4. **OpenAiFunctionCallingExamples.java**  
   函数调用演示，包含：
   - 天气工具的低级API配置
   - 工具方法注解使用(@Tool)
   - 多工具协同工作流程

5. **OpenAiStreamingChatModelExamples.java**  
   流式聊天接口示例，实现实时响应处理

6. **OpenAiObservabilityExamples.java**  
   模型可观测性演示，包含：
   - 请求/响应监听
   - 元数据提取
   - 错误处理

### 资源文件

- **story-about-happy-carrot.txt**  
  示例文本文件，用于测试长文本处理能力

## 环境要求

1. JDK 17+
2. 有效的 OpenAI API 密钥（设置环境变量`OPENAI_API_KEY`）
3. Maven 3.6+

## 快速开始


