import dev.langchain4j.agent.tool.*;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.ToolExecutor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

/**
 * OpenAI 函数调用示例
 * 演示如何通过工具调用实现复杂逻辑
 */
public class OpenAiFunctionCallingExamples {

    /**
     * This example demonstrates how to programmatically configure the low-level tool APIs, such as ToolSpecification,
     * ToolExecutionRequest, and ToolExecutor.
     * This sample is used in the LangChain4j tutorial: https://docs.langchain4j.dev/tutorials/tools/#low-level-tool-api.
     * But it is recommended to use higher-level APIs as demonstrated here: https://docs.langchain4j.dev/tutorials/tools/#high-level-tool-api
     * <p>
     * This sample goes through 4 different steps:
     * 1. Specify the tools (WeatherTools) and the query ("What will the weather be like in London tomorrow?")
     * 2. Model generates the tool execution request (model decides which tools to invoke and with which arguments)
     * 3. User execute tool(s) to obtain tool result(s) (using ToolExecutor)
     * 4. Model generate final response based on the query and the tool results

     * 天气工具低级配置示例
     * 演示完整工具调用流程：
     * 1. 配置工具并发送查询
     * 2. 模型生成工具执行请求
     * 3. 执行工具获取结果
     * 4. 模型生成最终响应
     */
    static class Weather_Low_Level_Configuration {

        // 初始化OpenAI聊天模型
        static ChatLanguageModel openAiModel = OpenAiChatModel.builder()
                //.apiKey(ApiKeys.OPENAI_API_KEY)
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .modelName(GPT_4_O_MINI)
                .strictTools(true)  // 强制模型严格使用工具
                .logRequests(true)  // 记录请求日志
                .logResponses(true) // 记录响应日志
                .build();

        public static void main(String[] args) {

            /* 步骤1: 用户指定工具和查询 */
            // 初始化天气工具集
            WeatherTools weatherTools = new WeatherTools();
            // 从工具类自动生成工具规格说明
            List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(weatherTools);

            // 构建聊天消息列表
            List<ChatMessage> chatMessages = new ArrayList<>();
            // 用户消息（已翻译为中文）
            UserMessage userMessage = userMessage("伦敦明天的天气怎么样？");
            chatMessages.add(userMessage);

            // 构建首次聊天请求
            ChatRequest chatRequest = ChatRequest.builder()
                    .messages(chatMessages)
                    .parameters(ChatRequestParameters.builder()
                            .toolSpecifications(toolSpecifications) // 注入工具规格
                            .build())
                    .build();

            /* 步骤2: 模型生成工具执行请求 */
            ChatResponse chatResponse = openAiModel.chat(chatRequest);
            AiMessage aiMessage = chatResponse.aiMessage();
            // 获取模型要求的工具执行请求列表
            List<ToolExecutionRequest> toolExecutionRequests = aiMessage.toolExecutionRequests();

            // 打印工具调用信息（中文化输出）
            System.out.printf("在WeatherTools定义的%d个工具中，需要调用%d个工具：%n",
                    toolSpecifications.size(), toolExecutionRequests.size());
            toolExecutionRequests.forEach(request -> {
                System.out.println("工具名称: " + request.name());
                System.out.println("参数内容: " + request.arguments());
            });
            chatMessages.add(aiMessage);

            /* 步骤3: 执行工具获取结果 */
            toolExecutionRequests.forEach(request -> {
                // 创建工具执行器
                ToolExecutor executor = new DefaultToolExecutor(weatherTools, request);
                System.out.printf("正在执行工具：%s%n", request.name());

                // 执行工具并获取结果
                String result = executor.execute(request, UUID.randomUUID().toString());

                // 将执行结果加入消息链
                ToolExecutionResultMessage resultMessage = ToolExecutionResultMessage.from(request, result);
                chatMessages.add(resultMessage);
            });

            /* 步骤4: 生成最终响应 */
            ChatRequest finalRequest = ChatRequest.builder()
                    .messages(chatMessages)
                    .parameters(ChatRequestParameters.builder()
                            .toolSpecifications(toolSpecifications)
                            .build())
                    .build();
            ChatResponse finalResponse = openAiModel.chat(finalRequest);
            System.out.println("最终响应：" + finalResponse.aiMessage().text());
        }
    }

    /**
     * 天气工具集合
     * 包含三个工具方法演示不同功能
     */
    static class WeatherTools {

        @Tool("获取指定城市明天的天气预报")
        String getWeather(
                @P("需要查询天气的城市名称") String city
        ) {
            return String.format("明天%s的天气是25°C", city);
        }

        @Tool("获取明天的日期")
        LocalDate getTomorrow() {
            return LocalDate.now().plusDays(1);
        }

        @Tool("将摄氏温度转换为华氏温度")
        double celsiusToFahrenheit(
                @P("需要转换的摄氏温度值") double celsius
        ) {
            return (celsius * 1.8) + 32;
        }

        // 未添加@Tool注解的方法不会被识别为工具
        String iAmNotATool() {
            return "我没有@Tool注解，不是工具方法";
        }
    }
}
