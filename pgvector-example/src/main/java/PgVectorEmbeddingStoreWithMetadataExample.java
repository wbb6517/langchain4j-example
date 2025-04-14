import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

/**
 * 带元数据过滤的PgVector示例
 * 演示如何：
 * 1. 为文本片段附加元数据
 * 2. 执行带过滤条件的向量搜索
 * 3. 实现多租户数据隔离
 */
public class PgVectorEmbeddingStoreWithMetadataExample {

    public static void main(String[] args) {
        // 1. 初始化PgVector测试容器（PostgreSQL 16 + pgvector扩展）
        DockerImageName dockerImageName = DockerImageName.parse("pgvector/pgvector:pg16");
        try (PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(dockerImageName)) {
            postgreSQLContainer.start(); // 启动容器（自动下载镜像）

            // 2. 初始化本地嵌入模型（All-MiniLM-L6-v2）
            EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

            // 3. 配置PgVector存储（自动创建表结构）
            EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                    .host(postgreSQLContainer.getHost())       // 容器主机地址
                    .port(postgreSQLContainer.getFirstMappedPort()) // 映射端口
                    .database(postgreSQLContainer.getDatabaseName()) // 默认数据库
                    .user(postgreSQLContainer.getUsername())   // 默认用户
                    .password(postgreSQLContainer.getPassword()) // 随机密码
                    .table("user_documents")                   // 存储表名
                    .dimension(embeddingModel.dimension())     // 向量维度384
                    .build();

            // 4. 准备测试数据（带用户元数据）
            // 用户1的数据
            TextSegment segment1 = TextSegment.from(
                "I like football.", 
                Metadata.metadata("userId", "1") // 添加用户ID元数据
            );
            Embedding embedding1 = embeddingModel.embed(segment1).content();
            embeddingStore.add(embedding1, segment1);

            // 用户2的数据
            TextSegment segment2 = TextSegment.from(
                "I like basketball.", 
                Metadata.metadata("userId", "2")
            );
            Embedding embedding2 = embeddingModel.embed(segment2).content();
            embeddingStore.add(embedding2, segment2);

            // 5. 生成查询向量
            Embedding queryEmbedding = embeddingModel.embed("What is your favourite sport?").content();

            // 6. 用户1的过滤查询
            Filter user1Filter = metadataKey("userId").isEqualTo("1");
            EmbeddingSearchRequest request1 = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .filter(user1Filter)  // 应用用户过滤
                    .maxResults(1)        // 返回最相关结果
                    .build();

            // 7. 执行搜索并处理结果
            EmbeddingMatch<TextSegment> user1Match = embeddingStore.search(request1).matches().get(0);
            System.out.println("用户1最佳匹配:");
            System.out.printf("相似度: %.4f%n", user1Match.score()); 
            System.out.println("内容: " + user1Match.embedded().text());

            // 8. 用户2的过滤查询
            Filter user2Filter = metadataKey("userId").isEqualTo("2");
            EmbeddingSearchRequest request2 = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .filter(user2Filter)
                    .maxResults(1)
                    .build();

            // 9. 执行搜索并处理结果
            EmbeddingMatch<TextSegment> user2Match = embeddingStore.search(request2).matches().get(0);
            System.out.println("\n用户2最佳匹配:");
            System.out.printf("相似度: %.4f%n", user2Match.score());
            System.out.println("内容: " + user2Match.embedded().text());

            postgreSQLContainer.stop(); // 停止并清理容器
        }
    }
}
