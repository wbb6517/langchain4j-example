import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;

import static dev.langchain4j.model.openai.OpenAiImageModelName.DALL_E_3;

/**
 * OpenAI 图像生成示例
 * 演示如何使用DALL-E模型根据文本描述生成图片
 */
public class OpenAiImageModelExamples {

    public static void main(String[] args) {
        
        // 初始化DALL-E图像生成模型
        ImageModel model = OpenAiImageModel.builder()
                .apiKey(ApiKeys.OPENAI_API_KEY)  // 使用ApiKeys管理的API密钥
                .modelName(DALL_E_3)             // 指定模型版本：
                                                 // - DALL-E 3：最新版本，支持1024x1024分辨率
                                                 // - DALL-E 2：旧版本，支持256x256/512x512
                .size("1024x1024")               // 设置生成图片尺寸（默认1024x1024）
                .quality("hd")                   // 画质选项：standard（默认）或hd（更高细节）
                .style("vivid")                  // 风格选项：vivid（鲜艳）或natural（自然）
                .build();                        // 完成模型构建

        // 生成图片请求
        Response<Image> response = model.generate(
            "唐老鸭在纽约，卡通风格"  // 图片描述（prompt）
            // 可选参数：
            // .withN(1)          // 生成图片数量（默认1，最大4）
            // .withResponseFormat("url") // 返回格式：url（默认）或b64_json
        );

        // 处理响应
        Image image = response.content();
        System.out.println("生成图片URL: " + image.url());          // 输出图片访问链接
        System.out.println("修订原因: " + image.revisedPrompt()); // 输出自动优化的提示词
    }
}
