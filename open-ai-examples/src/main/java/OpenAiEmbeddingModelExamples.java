import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.output.Response;

import static dev.langchain4j.model.openai.OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL;

/**
 * OpenAI 文本嵌入模型使用示例
 * 演示如何将文本转换为向量表示
 * 
 * 典型应用场景：
 * - 文本相似度计算
 * - 语义搜索
 * - 机器学习模型输入
 */
public class OpenAiEmbeddingModelExamples {

    public static void main(String[] args) {
        
        // 初始化嵌入模型构建器
        EmbeddingModel model = OpenAiEmbeddingModel.builder()
                //.apiKey(ApiKeys.OPENAI_API_KEY)  // 使用ApiKeys管理的API密钥
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .modelName(TEXT_EMBEDDING_3_SMALL) // 指定嵌入模型版本：
                                                   // - 小型模型（性能与成本的平衡）
                                                   // - 输出维度：1536
                                                   // - 支持最大输入长度：8191 tokens
                .build();  // 完成模型实例构建

        // 生成文本嵌入
        Response<Embedding> response = model.embed("I love Java"); 
        // 注意：embed方法返回Response包装对象，包含：
        // - 元数据（响应时间、token用量等）
        // - 实际嵌入内容
        
        // 提取嵌入向量
        Embedding embedding = response.content(); 
        // Embedding对象包含：
        // - 浮点数向量（List<Float>）
        // - 维度信息
        // - 标准化方法

        // 输出嵌入向量信息
        System.out.println("向量维度: " + embedding.dimension());  // 输出向量维度
        System.out.println("原始向量: " + embedding.vector());  // 输出原始向量值
    }
}
