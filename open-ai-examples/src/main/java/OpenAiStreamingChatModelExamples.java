import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import java.util.concurrent.CompletableFuture;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

/**
 * OpenAI 流式聊天模型示例
 * 演示如何实现实时响应处理
 */
public class OpenAiStreamingChatModelExamples {

    public static void main(String[] args) {

        // 初始化流式聊天模型
        StreamingChatLanguageModel chatModel = OpenAiStreamingChatModel.builder()
                //.apiKey(ApiKeys.OPENAI_API_KEY)  // 正式环境使用真实API密钥
                .baseUrl("http://langchain4j.dev/demo/openai/v1")  // 演示专用端点
                .modelName(GPT_4_O_MINI)          // 使用轻量级GPT-4o-mini模型
                .temperature(0.7)                 // 控制响应创造性（0-2）
                .maxTokens(100)                   // 限制最大输出长度
                .logRequests(true)                // 开启请求日志（调试用）
                .build();

        // 创建异步结果容器
        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        // 发起流式聊天请求
        chatModel.chat(
            "讲一个关于Java的冷笑话",  // 用户输入
            new StreamingChatResponseHandler() {  // 响应处理器

                // 收到部分响应时触发
                @Override
                public void onPartialResponse(String partialResponse) {
                    System.out.print(partialResponse); // 实时输出到控制台
                    System.out.flush(); // 确保及时显示
                }

                // 完整响应接收完成时触发
                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    System.out.println("\n\n完整响应元数据:");
                    System.out.println("使用模型: " + completeResponse.metadata().modelName());
                    System.out.println("Token用量: " + completeResponse.metadata().tokenUsage());
                    futureResponse.complete(completeResponse); // 完成异步操作
                }

                // 发生错误时触发
                @Override
                public void onError(Throwable error) {
                    System.err.println("\n请求发生错误:");
                    error.printStackTrace();
                    futureResponse.completeExceptionally(error); // 传递异常
                }
            }
        );

        // 阻塞等待直到请求完成
        ChatResponse finalResponse = futureResponse.join();
        System.out.println("最终响应状态: " + 
            (finalResponse != null ? "成功" : "失败"));
    }
}
