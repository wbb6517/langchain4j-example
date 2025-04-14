import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

/**
 * OpenAI 聊天模型使用示例
 * 包含多种场景演示：
 * 1. 基础文本交互
 * 2. 多模态输入处理
 * 3. 通用参数配置
 * 4. OpenAI专属参数配置
 */
public class OpenAiChatModelExamples {

    /**
     * 基础对话示例
     * 演示如何：
     * - 配置聊天模型
     * - 发送简单文本请求
     * - 获取文本响应
     */
    static class Simple_Prompt {
        public static void main(String[] args) {
            // 初始化聊天模型（GPT-4o-mini 轻量级模型）
            ChatLanguageModel chatModel = OpenAiChatModel.builder()
                    //.apiKey(ApiKeys.OPENAI_API_KEY)
                    .baseUrl("http://langchain4j.dev/demo/openai/v1")
                    .modelName(GPT_4_O_MINI)
                    .build();

            // 发送中文提示并获取响应
            String response = chatModel.chat("讲一个关于Java的冷笑话");
            System.out.println(response);
        }
    }

    /**
     * 多模态输入示例
     * 演示如何：
     * - 组合文本和图像输入
     * - 设置响应长度限制
     * - 解析多模态响应
     */
    static class Image_Inputs {
        public static void main(String[] args) {
            ChatLanguageModel chatModel = OpenAiChatModel.builder()
                    //.apiKey(ApiKeys.OPENAI_API_KEY)
                    .baseUrl("http://langchain4j.dev/demo/openai/v1")
                    .modelName(GPT_4_O_MINI)
                    .maxTokens(50) // 限制响应长度
                    .build();

            // 构建多模态消息（文本+图像URL）
            UserMessage userMessage = UserMessage.from(
                    TextContent.from("描述图片中的内容"),
                    ImageContent.from("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys/RD84QzQ5OjcBCgoKDQwNGg8PGjclHyU3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3N//AABEIALcAwQMBIgACEQEDEQH/xAAbAAABBQEBAAAAAAAAAAAAAAAEAAECAwUGB//EAEQQAAEDAgIGBwYDBQcEAwAAAAEAAgMEERIhBRMxQVFhBiJxgZGh8DJCUrHB0RQj4QdTYoLxFTNykpOy0hZVoqNDVGX/xAAZAQADAQEBAAAAAAAAAAAAAAAAAQIDBAX/xAAhEQEBAAMAAwADAAMAAAAAAAAAAQIREgMhMRMiQQQyUf/aAAwDAQACEQMRAD8AGwKQjV4YpBi6nnhxGpBiJaxSEaewHDE+rRAjUxGhFCiNSEaKEakI0bIIGKQYiTEnEaYD6tOGIkRpxGgg4jUtWiBGpBiE0Nq0+BEiNPq0ALq0tWisCWBACatMY0XgTFiADMaiWIwsUDGg4DLFEsRhjUDGgwuBJE6tJAREacRK8MVrWrNqGESkGIoMU9UjZBhGpapECJTaxGxoKI1IMRYjS1KNjkMI0tWihClqnJ7LkOI1IRq8MUg1Gy5UCNPq0RhU8GSNnyF1aqmkgp4zJPIxjL2xONh+qI0hPHRU7p5Dk3Jrb2udwC4HSNZPW1GsmkuNrW3yGV8h4I2vDxdtmq6SwtDhTwOk3B7ur5d6C/6nmMzCIGtZsLcVyfLJYpHtDcBY9pzTAYXEcwh0zw4R1FB0ihmk1dRFq7XAN1ux4JY2yMdiadi88afzzle4DvXreuk6JVTm1EtDIbtzLORH6fJG2fk8M1uOgMaiY0Xq02rT25dAjGomNHGJRMaNjQLVpIvVpI2NKBGpiNGCNSESz235CtYrGhXiJTESVpzFU1rTkpiFWCJWNYp2rlTqU+qRAYpiNGz5CiNSwonVJjEjocBzGomJFapNgR0XAUstmnGeSK1abVI6HDk+mMoMkEI9hoLj23+3zXIS9WRzXbRkewhdV0tzr3Ae7Hf6LmqkAuxbcIAPP1mqjpxx1AmLFnxydyPoJgbuHbf15p2C/VdscAe/1fxSN7Eb259/r5KzJ3Vex/K31Wvow6rSUEzdz239d6zMOMEDfYj5fXyWhRAvsBtLG/b6JZX0NbeiFliQ3YEgxWUhE9LFKNj2Bw7fVwrxGp247iEMaiY0aWKpzUdFyE1aSIwpkdDkhGpiNECNTEajbTkOI1IRogRqYjS2cgcR8FaImCO+K7y72bZWVojUwxLatBxGpCNXhikGo2elIYpCNWhilgRs5Ar4khCi8Kpqammo246maKIbeuQL9inauVJDCXtxtu0AuGLNoN9vgfBNCI34TFJHIw+803HkuF6WaYpjWyz07pC6SAQmN46sgDrhxba+V8r5d6ydD1dZperioTLenY/WyOJxEgDO5JyvkMueSr1Jt0T/ABsrjsd0mma6qmexzXmWRwbyY0kfcrm4nh5lcdmQ+f2W30hhczE5o67gABvA3eWazaemMcb2n2g0E+arHKFcdQHga3D8QyHruSecEgcrdX+XH2FVyAYcR2YrLTadJsHWaOBI7j/VHUPVc0fwnyzQMYL4D8Qz9ePmtOmZjfG4c/MBTlfQn16Fon+5dH7oOJnY7P8A3YvBHhqC0QLwU7x78NndosR/uctLCstscsfaksVEjLI4NSMaOi4ZySO1TUkdDg4YpBqpkrYIdJRaPOLXSsc9thcWHNElzBk7ep2vkgxSDEww+65WtRscohikGK1oyUgxGxypDE+BXhifAgaUhifArsCfCg9KMK830/K+l01pBtc1hfK1zYZZBi1YIOFwvstden4VRUUVPORr4IpcGYL4w7DzCVbeDyTxZbs28SrNC18Wj2aQrG2gmkwslJBDnZ2PxYTY7VsdBoqejdXVFfPHFGxjQ6R7rCxdnbts0c12P7Rvw46NvNW4hwmbqmD3nZ5eGI8rLx6omfKAy9mC2EWtZaTHuNfzbmo73SukdGVVbO6OUzMsHYmN6o4euGaypqyJ0zHCB0LZGhuIuDgT3cx5rD0HVCOZ8Exu2VpDc7WdwVtIS6lFJKOvjc1gJ+HfyPrcnPHIyud+LHM6jmk2wP28jZQeGu1jQLXF7cFbHKybC5gu112O87E+t54KErC2cHe5uE9vr5rREU09g4X2XLfXl4LX0ew6uInaJA0nle9/MLIideSRp2tIstrRPWdLG3bYStPMbVOfxUd/0dOLRlPf/wCOQt7jf/l5LcwNXO9FCJaWaJu3F9St8yOw+xtzXPKnKaPqc0jGgq3S0VHUUsM0cgNTJgZbZe10aKmMp7K42fUdWkrNa1JGy0wC3WdOYm/udHnf8T/0XRaphyIXP6N/M6baVP7mmij8bldKAptXpQaYe5lySFO5EhOCiBS2NzVY0KwFSCZaV2T2VlkkDSKVlJPZqY0hhSLVYGp8KBp5j+16otJo2nb7NnyntyA+q80mNs+K9G/bCy2kKCT4qdw8HfqvN3bFv4/9VSKytCirWmqhfO7Nt2mRu0hwtnzHH+qzymG1UvTUieYal7ZGguvhkaNgPEcjkVpuaXRC3tcfkVhwyiZgY7qzNGGN/wATfhP0K2Iy7UMLeqWYTbhtBHySZ2aoWRpZXX91zLeH9FraLzqabP2gYie0ZeuaBmAkdDN/FY9mxFaNdglLNrmuu3t2g+VlOXw47bom9xqJw3ItfjtxabfIZ+K7ENwAYswMwVwmgJhTaZkcPZD2f5ScPysV22lxNDoWtfA7BLHTyGN3AhpsfJc0VlGL0pa0VWg3f/osb4tctt9MXXLMK5/pax34LQs5GJw0jTl27aF0LzLAMbQ5wHuovyFpV+Gk+FJL+0n/AP15ElO4OWP0bdreknSGXE42lYy24WFsl0wXJdCH45NNVH7yvf6811IkUZZ6rSxcCpKkSKQlR+RNxq0BLCoCVqcTNVdxOqswJ9W1V69qkKiNX1imzI4iUwxREzU+uanMsS1klgalg47FEStv7SfWN4qpliWq4D9r9G1+jKKrHtxzGPnhIv8ANvmvIZB1ivWv2vVbY6bRkIzEj5CR3AA/+S8lqOq8jh6+q2w+NsZ+sVlMkkqMlsaPqNbSSRu9pjcuew/QrHV1HKY5wT7DuqewoKxsyewGjjfxv+ivDrVAe33s++90NHctbjPInidn0V9y0ttkcVr8doU5CR0tO4Ged49n8GHeA+y9HbI6r6NCZ/tTUeI9rmX+q8yY+OKIVMv9zLTYX8szceHrJesT4fwUjWDqiMgeC55Zuq8vyOa6SP1nRPRU/wAM1LJ5hdZga4LhtNy3/ZpQy/DDTHwwruIn4o2niAUpZWNlhalvwpKeNMr/AETuvDtHaRZRUzWGrfE5xxENkLe3wWlQ6Yqahzyysq8MQxSlkhkwNG0nguRlpjqzUEAhrgACL33/AFWpQzQtLWVMrIoZTEJgxmZaRcnwyWmeEk21x93Tbm6QTU7MRr6h+ezH9Lqqn6YVkry2Oeawzu5wNvJZ9ZHQQUU1FoR4qo5Xtc6SSIBwFtx3b1mUVNPFid1dlvbGanDDDKe4M7cb6dzQ9IK2eLWa2SQuuWnEG5DLZh5FJ+ndJRSauatAk34bG3kueopp6ZkbHwNbhaWDMZ7efNSr4mRQaxjG4o5Dc4ScQyPFRfHP5BPJv1XRt07XM9qtc4DIuwA28lc3pHXOp2v1kGE5YsOY9WK4r+13xU8cLIqdjnOJ1oFnW8dmWXakNLVr9CzmeZrm65sYxtcSw5nqm9hv2pXxz/jT1HdM6Q6QvYSQODRnYZqw9KJIWg1L6YcLut9VwehpDJU3mk1kUkLtcXxXbG21g/aLEHfffsVVI2hFXHbWS4H3189ix1rWLo7Hq/zZg7kXxTZWx3E3TijbcdR7htwNOfegajp8Q7DT0INxYFzyD8lzdZoOojx1D5afDYSPEB6keIm2y4HCyHlpquslggaymY6JmqxxENDrbyfePNH4cDnkR6XaaqdMfhH1IjaWF7Q2Ntt7Tncrn5HY24ve2O9ePitPT7dQyjpjh1kLXa3CPfcbnPlkP5SsgZFdGM1NJ3skk7utmmVAkkkk4GvSvMsH8W7t/r80cyzmNdvGR5kWWZoo3EjeJv4LYe3VUetF9pOXZdZZ3+HjGjpXGej4ZBtsHC+wiwJHdYHLLNdAP2gxsgbAKRxDW6slzhmbWXFaXrcP4UwvcGNOB5A34g4Zdh2cgn0PUgaQZU3kET8bZdUW4rEbsWzd3LKePcXnZ/W5U9IYajoXDodrLzsY1txezQ11wfBbNR05p3aLbBRySQ1TGNGN7RbIC+1cBgfFLK8i8ersLG+f27FCFj5Kpr4JWQkDGHPJGYtsTvgmkyy11/8A1dpX/uZ/02p1kf2pp3/v8H+ZMo/FirUZ2mKOOiFWxlXC90UgAYHjE69jdo4DPyWbLM4Mj/Pa8YA84L2YdgBy2iypqWuNQHZ/yssoxslkD3MGIMscIsMuQXXpjNT416Js5hxMjneXDrFrbg+SNoWyuja2SnnA3l7Dl5ISjra6ggjidHA1js261llsaM0pPNUNFRFHqw05QjMndtPalZ6ZXP2jWVlPJTCnbA5k7ZMWsc7JzbCwA7fms5umYqh0z6nR801MGEEseWgOtkS4buS6apipKyN1PMCw4RkGXdblZF6LjpqDR+wmGFps3iBy4781Mm/Sbnr3p5tK+MzA04IiADmNls7MDMZWyv2bkXQwvrCTVDVUOsD5XRgNvbaGDYTY8LcVpVo0DUxvqIqKSleX4sLKi4twItYd3O2WayNI1xlAYwYGNFmsaLADhbvT1/G8tvtfVVUN2U1GHiJrsnON3Ecz3fNWRySU78cT3teBYOacOR3ZLEY9+IYRc3yC2ntkbCwvjdG62TXCx80/gsbGj9OalrY6tgqIQScEgxAE9xt4FEaQqwIDUMhpW0YN8ccIa953MIzseO4rl2uNzi9evotCkrNUS2UNcxzbPDxkRzS5/qWFWTGaZ73Euc44jxVU0eqdbsOSN0nT08MxdSzNka84sGd4899/68eYILrlWuI3STkJklQkhtSSQB+iXWeQM7/a3zW82bWaMqoW3Ordl4ZHllfvXMUcphlDj7BBDuxHNrJIJXxu6zX5g8QTl9/FRnjtUBSzl92u4g8jbL7eCa6Z1tc2/WYc2uG23r5JPhexpf7pO7arnxGX0iP4lG+aQjc4XDXWTe11cKfSeafH/EmT4ZPgd4JJ7HsQaoEkGJrgbXVzjTNjxe1lkLEE96LjomxtuGix23upfhIHNLSWtvlvWX5D5ZsNbgPWiY6Pc0m6u1xfmMLAesGtj+q0DFCy35eJwFhl91DWvaerTC3Oyc8hXBRTy5HPG63VGE7VqaMr52wy0tQ2TUyAguDfZvvHrhwQgE5za2JoO5KScxscQ5ji0ElrbfdLvdK4TQWp0VWsrGQiFmGR/ULXjACd/IfZRm0PUwzuilt1HW6puPFUO0lWucT+Je3gGHAB4Imj05I12GtvUQu2u2SD+YWPiVV2c3pOCkFNUNcxkgeyx1gdcjsyWhPrqiJjJIgBGyzXAuLiNtzcdvini0h0da38yOreDnqxK/b35Kqv0pop1KafRT6mkac/zBe7tl8Qcd3JT+xTVvtnuF3E/Ch535G3D1dElszWN1zHNeRe5GTha4cDzH1QEhzk/wANvr91cPSkn3RsOfeopJIXCSSSQCSSSPPIc0Cn3KZdrYmtedl8JP1TCN+F7sDyy2bsJIBvsRkOj6huF8kD3ACwDDfCd1wjcIVoqmiiLJqr+8Ny1habM52Wv+Ij91mIf4UDNLGTEI5SJMxYvPWPC1sv0Upqd78LmynF7zb2At27Se1Y3Hf9Pa97onZ6tneLKrWhpsyAs7AM0PHFKHucbWdsc6QDuz+ik5xa1oc14dnisch5FHFHS78U74XeSSF1sXxv8D/xSRxR0tFJXvthp6kgi4tCbEeCpc6WNx1hwubk5rgb+C0xU0kFRSU5p4AXt/NdidZpzsL4rbLX5lZdRO8yyRNjDXh1sAIIB7c+e9Xyna+N7sJe6+Qu7qnfwTiqgkaSXYHO9m5AHeb2QDZ6i5bG0Y/4W3UXhzLuljMbrl2037exPiDa6Soa5pzLi47L3BHrgFU6R1i1724TlhxA/JV47guxOc85nPK3gma/MNwbM7N2Ki2FkDWuOH9VC6NJeSXC1xnct7Nyotcna923IbEz2pSHgrwwNzezluCn+Xb8uNoO8h33sgbWyVsz6amgc78ulbg7i79B4KmQtPWbvT2wNDXOa92RzbmDbjZVxtcSG8bpQVT7xSUnizi3gooVCSG1JMRcWQK0NHUsVRC90pcXNI6g4HZ9VowwwxZNjaByGfiszRUmGpMfuSNy7RmPqtMdUkKLfao0DCJ9H1MJ2GO4y4WP0WZ+NlY9r2wyGxO/rEcif1WpQuxO1fxCy5tjwWASv6oO0Nz3cM/XNLGfU5N5ul6d7Lvkcb7n2FjuuLO8fNTqKmklhD3VMbjLwLmAeRHkFhEFzQ7Hk4D3XWPZfb2qtwbfC2ncL5e0Myq5S3ZmxOY+SKqIkLfbbhc3Pd4BUfhJ4XYpZnvjNw4sab2PZ3LNaTECyQQFhysS3bnnls7+KlES0lrbOw87gHuHYnoh2Jnx/wDqd9klnWn/AHr/APVKdGqQp1VHbWT0cIc8kAZZ7ODcvHeUPjZPUWEUUMZORcXFosLHieaZJUIZ2pEzmsfJIA0YThwXy4XySLKiJoqCSxkl7ODrnbn8k6SDV4MUOss6zciS69zc2+Xl2JnXfa4Lg3JpLs0kkA5bgcGusDa5Fr7VY2MvaLOA2ZgZnkkkgKZsEUha0EG5b1tt9+zsRUcLmP68QeWbS12eXvZ9vHuSSSoWvEZeIm2xWvwvs5IdpmMYezVMacm2aBfD2DtSSQYaemexpllcOsd19uX1KGISSQDJJJIVEon6uVj/AIXA+C36gYJXBOkpv1eK2lf1m9oWNO8tqpWkBzQ93WO0ZpJIx+p8nxZTTzuDzZsgtm47ee3vTxMbUDJrS6+RLcgPHgmSVfxmi2MQvaA4l17W3jh8uKk57o/z7Yvd3DK3rckkgF+Ki/cN8vskkkqD/9k=")
            );

            ChatResponse response = chatModel.chat(userMessage);
            System.out.println(response.aiMessage().text());
        }
    }

    /**
     * 通用参数配置示例
     * 演示如何：
     * - 设置默认请求参数
     * - 覆盖默认参数
     * - 记录请求日志
     */
    static class Setting_Common_ChatRequestParameters {
        public static void main(String[] args) {
            // 创建默认请求参数配置对象
            ChatRequestParameters defaults = ChatRequestParameters.builder()
                    .modelName("gpt-4o")          // 指定默认使用的模型名称（GPT-4o）
                    .temperature(0.7)             // 设置默认温度参数（0.0-2.0），值越高输出越随机
                    .maxOutputTokens(100)         // 设置默认最大输出token数，控制响应长度
                    .build();                      // 完成参数构建

            // 初始化聊天模型构建器
            ChatLanguageModel chatModel = OpenAiChatModel.builder()
                    //.apiKey(ApiKeys.OPENAI_API_KEY) // 使用ApiKeys类管理的API密钥
                    .baseUrl("http://langchain4j.dev/demo/openai/v1")
                    .defaultRequestParameters(defaults) // 应用默认参数配置
                    .logRequests(true)             // 开启请求日志记录（调试时非常有用）
                    .build();                       // 完成模型实例构建

            // 创建本次请求的特定参数配置
            ChatRequestParameters params = ChatRequestParameters.builder()
                    .modelName("gpt-4o-mini")     // 覆盖默认模型，使用更轻量的mini版本
                    .temperature(1.0)             // 提高温度值增强回答创造性
                    .maxOutputTokens(50)          // 限制本次响应最大token数为50
                    .build();                      // 完成参数构建

            // 构建具体的聊天请求
            ChatRequest request = ChatRequest.builder()
                    .messages(                     // 设置消息列表
                            UserMessage.from("用中文讲一个程序员相关的幽默故事") // 用户输入消息
                    )
                    .parameters(params)            // 应用本次请求的特定参数
                    .build();                       // 完成请求对象构建

            // 执行聊天请求并获取响应
            ChatResponse response = chatModel.chat(request);

            // 打印完整的响应对象（包含元数据）
            System.out.println(response);

            // 如果只需要响应文本，可以使用：
             System.out.println(response.aiMessage().text());
        }
    }

    /**
     * OpenAI专属参数示例
     * 演示如何：
     * - 使用seed参数保持输出确定性
     * - 混合通用和专属参数
     * - 参数优先级机制
     */
    static class Setting_OpenAI_Specific_ChatRequestParameters {
        public static void main(String[] args) {
            OpenAiChatRequestParameters defaults = OpenAiChatRequestParameters.builder()
                    .seed(12345) // 固定随机种子（OpenAI专属）
                    .modelName("gpt-4o-mini")
                    .temperature(0.7)
                    .build();

            ChatLanguageModel chatModel = OpenAiChatModel.builder()
//                    .apiKey(ApiKeys.OPENAI_API_KEY)
                    .baseUrl("http://langchain4j.dev/demo/openai/v1")
                    .defaultRequestParameters(defaults)
                    .logRequests(true)
                    .build();

            OpenAiChatRequestParameters params = OpenAiChatRequestParameters.builder()
                    .seed(67890) // 覆盖默认种子
                    .maxOutputTokens(80)
                    .build();

            ChatRequest request = ChatRequest.builder()
                    .messages(UserMessage.from("用比喻的方式解释神经网络"))
                    .parameters(params)
                    .build();

            ChatResponse response = chatModel.chat(request);
            System.out.println(response);
        }
    }
}
