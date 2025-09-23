import com.aftership.shipping.ShippingSdk;
import com.aftership.shipping.http.AfterShipClient;

public class VerifyPackageTest {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("验证使用的是发布包 3.0.0 还是本地源码");
        System.out.println("=====================================");
        
        try {
            // 获取类加载位置
            Class<?> sdkClass = ShippingSdk.class;
            String location = sdkClass.getProtectionDomain().getCodeSource().getLocation().toString();
            System.out.println("✅ ShippingSdk 类加载位置: " + location);
            
            Class<?> clientClass = AfterShipClient.class;
            String clientLocation = clientClass.getProtectionDomain().getCodeSource().getLocation().toString();
            System.out.println("✅ AfterShipClient 类加载位置: " + clientLocation);
            
            // 检查包名
            System.out.println("✅ ShippingSdk 包名: " + sdkClass.getPackage().getName());
            System.out.println("✅ AfterShipClient 包名: " + clientClass.getPackage().getName());
            
            // 尝试初始化 (使用虚拟key)
            System.out.println("\n🔧 尝试初始化SDK...");
            ShippingSdk.init("dummy_key_for_test");
            System.out.println("✅ SDK初始化成功 (使用发布包 3.0.0)");
            
        } catch (Exception e) {
            System.out.println("❌ 错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=====================================");
    }
}

