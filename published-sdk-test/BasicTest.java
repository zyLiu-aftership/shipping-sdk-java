import com.aftership.shipping.ShippingSdk;
import com.aftership.shipping.http.AfterShipClient;

public class BasicTest {
    private static final String API_KEY = "test-api-key";
    
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("AfterShip Shipping SDK 基本测试 (发布包 3.0.0)");
        System.out.println("=====================================");
        
        try {
            System.out.println("🔧 初始化 Shipping SDK...");
            ShippingSdk.init(API_KEY);
            
            System.out.println("🔗 获取客户端...");
            AfterShipClient client = ShippingSdk.getRestClient();
            
            if (client != null) {
                System.out.println("✅ SDK 初始化成功!");
                System.out.println("✅ 客户端创建成功!");
                System.out.println("✅ 发布包 3.0.0 基本功能正常");
            } else {
                System.out.println("❌ 客户端为空");
            }
            
        } catch (Exception e) {
            System.out.println("❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=====================================");
    }
}
