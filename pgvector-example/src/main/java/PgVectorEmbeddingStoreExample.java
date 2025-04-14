import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

import java.util.List;

/**
 * PgVector 向量存储基础示例
 * 演示完整的向量存储和相似性搜索流程
 */
public class PgVectorEmbeddingStoreExample {

    public static void main(String[] args) {
        
        // 初始化本地嵌入模型
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        // 配置本地PgVector连接
        EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                .host("localhost")       // 本地PostgreSQL地址
                .port(5432)              // 默认端口
                .database("demo1")  // 预先创建的数据库名
                .user("postgres")          // 数据库用户
                .password("postgres")  // 用户密码
                .table("vectors")        // 自定义表名
                .dimension(embeddingModel.dimension())
                .createTable(true)       // 自动创建表（首次运行）
                .build();

        // 4. 生成并存储第一个文本嵌入
        TextSegment segment1 = TextSegment.from("I like football."); // 原始文本
        Embedding embedding1 = embeddingModel.embed(segment1).content(); // 生成嵌入向量
        embeddingStore.add(embedding1, segment1); // 存储到数据库

        // 5. 生成并存储第二个文本嵌入
        TextSegment segment2 = TextSegment.from("The weather is good today.");
        Embedding embedding2 = embeddingModel.embed(segment2).content();
        embeddingStore.add(embedding2, segment2);

        // 6. 生成查询嵌入
        Embedding queryEmbedding = embeddingModel.embed("What is your favourite sport?").content();

        // 7. 构建搜索请求
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding) // 查询向量
                .maxResults(1)                  // 返回最相关的结果
                .build();

        // 8. 执行相似性搜索
        List<EmbeddingMatch<TextSegment>> results = embeddingStore.search(request).matches();

        // 9. 处理搜索结果
        EmbeddingMatch<TextSegment> bestMatch = results.get(0);
        System.out.println("相似度得分: " + bestMatch.score()); // 余弦相似度（0-1）
        System.out.println("匹配文本: " + bestMatch.embedded().text());
    }
}
