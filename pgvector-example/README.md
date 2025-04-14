# PgVector 向量存储示例模块

本模块演示如何将LangChain4j与PgVector结合使用，实现高效的向量存储和相似性搜索。

## 模块功能

- 使用PgVector作为向量数据库
- 支持元数据过滤的相似性搜索
- 本地运行PostgreSQL容器（测试用途）
- 集成轻量级嵌入模型

## 模块结构

### 核心文件说明

- **pom.xml**  
  项目依赖配置：
  - `langchain4j-pgvector`: PgVector集成
  - `postgresql`: Testcontainers PostgreSQL支持
  - `langchain4j-embeddings-all-minilm-l6-v2`: 本地嵌入模型
  - `slf4j-simple`: 日志实现

### 功能示例

1. **PgVectorEmbeddingStoreExample.java**  
   基础向量存储与搜索流程：
   - 启动PgVector容器
   - 文本嵌入生成
   - 向量存储与相似性搜索

2. **PgVectorEmbeddingStoreWithMetadataExample.java**  
   带元数据过滤的增强示例：
   - 元数据附加与存储
   - 基于元数据的过滤搜索
   - 多条件查询演示

## 环境要求

1. JDK 17+
2. Docker（用于运行Testcontainers）
3. Maven 3.6+

## 快速开始

```bash
mvn clean compile exec:java \
  -Dexec.mainClass=PgVectorEmbeddingStoreExample
```

（或替换为PgVectorEmbeddingStoreWithMetadataExample运行带元数据的示例） 