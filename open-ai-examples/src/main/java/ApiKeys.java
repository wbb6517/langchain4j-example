import static dev.langchain4j.internal.Utils.getOrDefault;

/**
 * API密钥管理类
 * 提供安全统一的密钥获取方式，遵循以下优先级：
 * 1. 从系统环境变量读取
 * 2. 使用默认演示密钥（仅限开发测试）
 * 
 * 生产环境建议通过以下方式设置密钥：
 * - IDE运行配置的环境变量
 * - 服务器环境变量
 * - 密钥管理服务
 */
public class ApiKeys {

    /**
     * Open API 平台API访问密钥
     * 获取逻辑：
     * 1. 首先尝试读取 OPENAI_API_KEY 环境变量
     * 2. 未找到时使用"demo"作为演示密钥
     *
     * 安全提示：
     * - 正式环境请勿使用demo密钥
     * - 建议将真实密钥存储在安全的位置
     */
    public static final String OPENAI_API_KEY = getOrDefault(System.getenv("OPENAI_API_KEY"), "demo");

    /**
     * DeepSeek平台API访问密钥
     * 获取逻辑：
     * 1. 首先尝试读取 DEEP_SEEK_API_KEY 环境变量
     * 2. 未找到时使用"demo"作为演示密钥
     *
     * 安全提示：
     * - 正式环境请勿使用demo密钥
     * - 建议将真实密钥存储在安全的位置
     */
    public static final String DEEP_SEEK_KEY = getOrDefault(System.getenv("DEEP_SEEK_KEY"), "demo");
}
