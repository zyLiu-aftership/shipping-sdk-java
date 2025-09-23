import com.aftership.shipping.ShippingSdk;
import com.aftership.shipping.http.AfterShipClient;

public class QuickVersionCheck {
    public static void main(String[] args) {
        System.out.println("=== SDK 版本快速检查 ===");
        
        try {
            // 显示类加载位置
            Class<?> sdkClass = ShippingSdk.class;
            String jarLocation = sdkClass.getProtectionDomain().getCodeSource().getLocation().toString();
            System.out.println("✅ SDK JAR 位置: " + jarLocation);
            
            // 显示包名
            System.out.println("✅ 包名: " + sdkClass.getPackage().getName());
            
            // 初始化测试
            ShippingSdk.init("test_key_for_version_check");
            AfterShipClient client = ShippingSdk.getRestClient();
            
            System.out.println("✅ SDK 初始化成功");
            System.out.println("✅ 客户端创建成功");
            
            if (jarLocation.contains("shipping-sdk-3.0.0.jar")) {
                System.out.println("🎉 确认使用的是发布包 3.0.0");
            } else {
                System.out.println("⚠️  警告：可能不是 3.0.0 版本");
            }
            
        } catch (Exception e) {
            System.err.println("❌ 错误: " + e.getMessage());
        }
        
        System.out.println("======================");
    }
}

