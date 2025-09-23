# AfterShip Shipping SDK 发布包测试 (3.0.0)

这是一个独立的测试项目，用于验证已发布的 AfterShip Shipping SDK Maven 包 (`com.aftership:shipping-sdk:3.0.0`) 的功能是否正常工作。

## 目录结构

```
published-sdk-test/
├── pom.xml                              # Maven 项目配置（使用发布的 3.0.0 版本）
├── README.md                            # 本文件
├── run_all_tests.sh                     # 运行所有测试的脚本
├── run_label_test.sh                    # 运行 Label 相关测试
├── run_rate_test.sh                     # 运行 Rate 相关测试  
├── run_cancel_label_test.sh             # 运行 Cancel Label 相关测试
├── 
├── # JSON 配置文件
├── label_payload.json                   # Label 创建请求数据
├── rate_payload.json                    # Rate 计算请求数据
├── cancel_label_payload.json            # Cancel Label 请求数据
├── 
├── # 功能测试文件
├── LabelFunctionalityTest.java          # Label 功能完整性测试
├── RateFunctionalityTest.java           # Rate 功能完整性测试
├── CancelLabelFunctionalityTest.java    # Cancel Label 功能完整性测试
├── 
└── # 参数验证测试文件
    ├── LabelParameterValidationTest.java       # Label 参数和枚举验证测试
    ├── RateParameterValidationTest.java        # Rate 参数和枚举验证测试
    └── CancelLabelParameterValidationTest.java # Cancel Label 参数验证测试
```

## 测试内容

### 功能测试
- **Label 功能测试**: 创建标签、获取单个标签、获取标签列表
- **Rate 功能测试**: 计算运费、获取单个运费记录、获取运费记录列表  
- **Cancel Label 功能测试**: 取消标签、获取取消记录、获取取消记录列表

### 参数验证测试
- **枚举值测试**: 文件类型 (PDF, ZPL)、重量单位 (lb, kg, oz, g)、尺寸单位 (cm, in, mm, m, ft, yd)
- **纸张尺寸测试**: 4x6, A4, A5, 3x5 等各种尺寸
- **服务类型测试**: FedEx Ground, FedEx 2 Day, FedEx Express 等
- **边界值测试**: 最小/最大重量、尺寸等
- **错误处理测试**: 无效参数、空值等异常情况

## 环境要求

- Java 8 或更高版本
- Maven 3.6+ 
- 网络连接（用于下载依赖和调用 API）

## 配置说明

### API Key 配置
在运行测试前，需要在各个测试文件中配置您的 API Key：

```java
private static final String API_KEY = "你的_API_KEY_在这里";
```

需要修改的文件：
- `LabelFunctionalityTest.java`
- `RateFunctionalityTest.java`
- `CancelLabelFunctionalityTest.java`
- `LabelParameterValidationTest.java`
- `RateParameterValidationTest.java`
- `CancelLabelParameterValidationTest.java`

### Shipper Account 配置
确保 JSON 配置文件中的 Shipper Account ID 是有效的：
- `label_payload.json`
- `rate_payload.json`

## 运行方式

### 方式一：运行所有测试
```bash
./run_all_tests.sh
```

### 方式二：运行特定类型测试
```bash
# 只运行 Label 相关测试
./run_label_test.sh

# 只运行 Rate 相关测试  
./run_rate_test.sh

# 只运行 Cancel Label 相关测试
./run_cancel_label_test.sh
```

### 方式三：手动运行单个测试
```bash
# 编译项目
mvn clean compile dependency:copy-dependencies

# 运行特定测试类
java -cp "target/classes:target/dependency/*" LabelFunctionalityTest
java -cp "target/classes:target/dependency/*" RateFunctionalityTest
# ... 其他测试类
```

## 预期结果

### 成功情况
- 所有测试通过，显示绿色 ✅ 标记
- 创建的资源显示正确的 ID 和状态
- 参数验证测试确认枚举值设置正确
- 最终显示测试总结和成功率

### 可能的问题
1. **凭据错误**: 如果看到 "credential error"，通常是 Shipper Account 配置问题，但 SDK 功能本身正常
2. **网络问题**: 请检查网络连接
3. **API 配额**: 如果请求过多可能遇到速率限制

## 测试验证点

这个测试项目验证了以下关键点：

### 发布包集成
- [x] Maven 依赖解析正确 (`com.aftership:shipping-sdk:3.0.0`)
- [x] 所有必要的类和方法都可访问
- [x] 依赖传递没有冲突

### API 功能性
- [x] SDK 初始化和配置
- [x] HTTP 客户端正常工作
- [x] JSON 序列化/反序列化
- [x] 错误处理机制

### 业务逻辑
- [x] 各种枚举值正确映射
- [x] 请求参数验证
- [x] 响应数据解析
- [x] 业务状态处理

## 故障排除

### 编译错误
```bash
# 清理并重新下载依赖
mvn clean
mvn dependency:resolve
mvn compile
```

### 运行时错误
- 检查 API Key 是否正确设置
- 确认 Shipper Account ID 有效
- 验证网络连接正常
- 查看 Maven Central 是否可访问

### 权限错误
```bash
# 确保脚本有执行权限
chmod +x *.sh
```

## 输出示例

成功运行后会看到类似输出：
```
=========================================
📊 测试总结  
=========================================
总测试数: 6
通过测试: 6
失败测试: 0
成功率: 100.0%

🎉 所有测试都通过了！
✅ AfterShip Shipping SDK 3.0.0 发布包功能正常
```

## 联系支持

如果遇到问题：
1. 检查本文档的故障排除部分
2. 确认使用的是正确的 API 凭据
3. 联系 AfterShip 技术支持: support@aftership.com

---
**注意**: 这些测试使用 AfterShip 的沙箱环境，不会产生实际费用或影响生产数据。
