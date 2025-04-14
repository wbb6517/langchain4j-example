import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiChatResponseMetadata;
import dev.langchain4j.model.output.TokenUsage;

import java.util.List;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

/**
 * OpenAI 可观测性示例
 * 演示如何监控模型请求、响应和错误
 */
public class OpenAiObservabilityExamples {

    static class Observe_OpenAiChatModel {

        public static void main(String[] args) {

            // 创建监听器实现
            ChatModelListener listener = new ChatModelListener() {

                // 请求发送前触发
                @Override
                public void onRequest(ChatModelRequestContext requestContext) {
                    // 获取请求对象
                    ChatRequest chatRequest = requestContext.chatRequest();

                    // 打印请求消息内容
                    System.out.println("请求消息: " + chatRequest.messages());

                    // 解析通用请求参数
                    ChatRequestParameters params = chatRequest.parameters();
                    System.out.println("模型名称: " + params.modelName());
                    System.out.println("温度参数: " + params.temperature());
                    System.out.println(params.topP());
                    System.out.println(params.topK());
                    System.out.println(params.frequencyPenalty());
                    System.out.println(params.presencePenalty());
                    System.out.println(params.maxOutputTokens());
                    System.out.println(params.stopSequences());
                    System.out.println(params.toolSpecifications());
                    System.out.println(params.toolChoice());
                    System.out.println(params.responseFormat());

                    // 处理OpenAI专属参数
                    if (params instanceof OpenAiChatRequestParameters openAiParams) {
                        System.out.println("随机种子: " + openAiParams.seed());
                        System.out.println("用户标识: " + openAiParams.user());
                        System.out.println(openAiParams.maxCompletionTokens());
                        System.out.println(openAiParams.logitBias());
                        System.out.println(openAiParams.parallelToolCalls());
                        System.out.println(openAiParams.store());
                        System.out.println(openAiParams.metadata());
                        System.out.println(openAiParams.serviceTier());
                        System.out.println(openAiParams.reasoningEffort());
                    }

                    // 添加自定义属性（可在后续流程中传递）
                    requestContext.attributes().put("请求开始时间", System.currentTimeMillis());
                }

                // 成功响应时触发
                @Override
                public void onResponse(ChatModelResponseContext responseContext) {
                    // 获取响应对象
                    ChatResponse response = responseContext.chatResponse();

                    // 解析AI响应消息
                    AiMessage aiMessage = response.aiMessage();
                    System.out.println("AI响应内容: " + aiMessage.text());

                    // 获取元数据
                    ChatResponseMetadata metadata = response.metadata();
                    System.out.println("请求ID: " + metadata.id());
                    System.out.println("使用模型: " + metadata.modelName());
                    System.out.println("完成原因: " + metadata.finishReason());

                    // 解析Token用量
                    TokenUsage tokenUsage = metadata.tokenUsage();
                    System.out.println("输入Token数: " + tokenUsage.inputTokenCount());
                    System.out.println("输出Token数: " + tokenUsage.outputTokenCount());

                    // 处理OpenAI专属元数据
                    if (metadata instanceof OpenAiChatResponseMetadata openAiMetadata) {
                        System.out.println("创建时间戳: " + openAiMetadata.created());
                        System.out.println("系统指纹: " + openAiMetadata.systemFingerprint());
                    }

                    // 计算请求耗时
                    long startTime = (Long) responseContext.attributes().get("请求开始时间");
                    System.out.println("请求耗时: " + (System.currentTimeMillis() - startTime) + "ms");
                }

                // 发生错误时触发
                @Override
                public void onError(ChatModelErrorContext errorContext) {
                    // 打印错误堆栈
                    System.err.println("请求发生错误:");
                    errorContext.error().printStackTrace();

                    // 获取相关请求信息
                    ChatRequest failedRequest = errorContext.chatRequest();
                    System.out.println("失败请求: " + failedRequest);

                    // 记录错误发生时间
                    System.out.println("错误时间: " + System.currentTimeMillis());

                    // 获取自定义属性
                    Object myAttr = errorContext.attributes().get("my-attribute");
                    System.out.println("自定义属性值: " + myAttr);
                }
            };

            // 初始化带监听器的聊天模型
            ChatLanguageModel model = OpenAiChatModel.builder()
                    //.apiKey(ApiKeys.OPENAI_API_KEY)
                    .baseUrl("http://langchain4j.dev/demo/openai/v1")
                    .modelName(GPT_4_O_MINI) // 使用GPT-4o-mini模型
                    .listeners(List.of(listener)) // 注册监听器
                    .logRequests(true)  // 开启请求日志
                    .logResponses(true) // 开启响应日志
                    .build();

            // 执行示例请求
            model.chat("讲一个关于Java的冷笑话");
        }
    }
}
