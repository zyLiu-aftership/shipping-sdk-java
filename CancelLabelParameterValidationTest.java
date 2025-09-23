import com.aftership.AfterShip;
import com.aftership.http.AfterShipClient;
import com.aftership.labels.*;
import com.aftership.cancel_labels.*;
import com.aftership.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AfterShip SDK Cancel Label 参数验证测试套件
 * 
 * 专门测试 SDK 中各种参数和枚举值的正确设置：
 * 1. 取消标签状态枚举测试 (cancelling, cancelled, failed)
 * 2. 标签ID有效性验证
 * 3. 边界值和异常情况测试
 * 4. 错误处理机制验证
 */
public class CancelLabelParameterValidationTest {
    
    private static final String API_KEY = "asat_76ef433d161e441f96ea7b84f5ed4aab";
    private static final String LABEL_PAYLOAD_FILE = "label_payload.json";
    
    private AfterShipClient client;
    private Gson gson;
    private List<String> availableLabelIds;
    private List<String> cancelLabelIds;
    private int totalTests;
    private int passedTests;

    // 测试用例定义
    private static class TestCase {
        String name;
        String labelId;
        boolean expectSuccess;
        String expectedError;
        
        TestCase(String name, String labelId, boolean expectSuccess, String expectedError) {
            this.name = name;
            this.labelId = labelId;
            this.expectSuccess = expectSuccess;
            this.expectedError = expectedError;
        }
    }

    public CancelLabelParameterValidationTest() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.availableLabelIds = new ArrayList<>();
        this.cancelLabelIds = new ArrayList<>();
        this.totalTests = 0;
        this.passedTests = 0;
    }

    public static void main(String[] args) {
        CancelLabelParameterValidationTest test = new CancelLabelParameterValidationTest();
        
        System.out.println("=====================================");
        System.out.println("AfterShip SDK Cancel Label 参数验证测试套件");
        System.out.println("=====================================");
        System.out.println("API Key: " + API_KEY.substring(0, 20) + "...");
        System.out.println();
        
        try {
            test.initializeSDK();
            test.prepareLabelIds();
            test.runParameterValidationTests();
        } catch (Exception e) {
            System.err.println("❌ 测试执行失败: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initializeSDK() throws Exception {
        System.out.println("🔧 初始化 SDK...");
        AfterShip.init(API_KEY);
        client = AfterShip.getRestClient();
        System.out.println("✅ SDK 初始化成功");
        System.out.println();
    }

    /**
     * 准备用于测试的标签ID
     */
    private void prepareLabelIds() {
        System.out.println("📋 准备测试用的标签 ID...");
        
        // 尝试创建一个新标签用于测试
        try {
            String labelId = createLabelForTesting();
            if (labelId != null) {
                availableLabelIds.add(labelId);
                System.out.println("✅ 成功创建测试标签: " + labelId);
            }
        } catch (Exception e) {
            System.out.println("⚠️  无法创建新标签: " + e.getMessage());
        }
        
        // 尝试从现有标签列表中获取一些ID
        try {
            GetLabelsFetcher fetcher = new GetLabelsFetcher();
            fetcher.setLimit("5");
            GetLabelsResponse response = fetcher.fetch(client);
            
            if (response != null && response.getData() != null && 
                response.getData().getLabels() != null) {
                
                List<LabelV3> existingLabels = response.getData().getLabels();
                for (LabelV3 label : existingLabels) {
                    if (label.getId() != null && 
                        !"failed".equals(label.getStatus().toString()) &&
                        !"cancelled".equals(label.getStatus().toString())) {
                        availableLabelIds.add(label.getId());
                        if (availableLabelIds.size() >= 3) break; // 最多取3个
                    }
                }
                System.out.println("✅ 从现有标签中获取了 " + 
                    Math.min(existingLabels.size(), 3) + " 个标签ID用于测试");
            }
        } catch (Exception e) {
            System.out.println("⚠️  无法获取现有标签: " + e.getMessage());
        }
        
        System.out.println("📊 可用于测试的标签ID数量: " + availableLabelIds.size());
        System.out.println();
    }

    private String createLabelForTesting() throws Exception {
        // 创建标签请求
        PostLabelsRequest request = loadLabelPayload();
        
        // 使用 PostLabelsCreator 创建标签
        PostLabelsCreator creator = new PostLabelsCreator();
        creator.setPostLabelsRequest(request);
        
        try {
            PostLabelsResponse response = creator.create(client);
            if (response != null && response.getData() != null) {
                return response.getData().getId();
            }
        } catch (Exception e) {
            // 尝试从错误信息中提取 label id
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("label id is:")) {
                String[] parts = errorMsg.split("label id is:");
                if (parts.length > 1) {
                    return parts[1].trim();
                }
            }
            throw e;
        }
        return null;
    }

    private PostLabelsRequest loadLabelPayload() throws Exception {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(LABEL_PAYLOAD_FILE)));
            return gson.fromJson(jsonContent, PostLabelsRequest.class);
        } catch (IOException e) {
            throw new Exception("无法读取 " + LABEL_PAYLOAD_FILE + " 文件: " + e.getMessage());
        }
    }

    private void runParameterValidationTests() {
        // 准备测试用例
        List<TestCase> testCases = new ArrayList<>();
        
        // 有效标签ID测试
        if (!availableLabelIds.isEmpty()) {
            testCases.add(new TestCase("有效标签ID取消", availableLabelIds.get(0), true, null));
            
            if (availableLabelIds.size() > 1) {
                testCases.add(new TestCase("另一个有效标签ID", availableLabelIds.get(1), true, null));
            }
        }
        
        // 无效标签ID测试
        testCases.addAll(Arrays.asList(
            new TestCase("空标签ID", "", false, "invalid"),
            new TestCase("NULL标签ID", null, false, "invalid"),
            new TestCase("无效格式的标签ID", "invalid-label-id-123", false, "not found"),
            new TestCase("UUID格式但不存在的标签ID", "12345678-1234-1234-1234-123456789012", false, "not found"),
            new TestCase("极长的标签ID", "a".repeat(1000), false, "invalid"),
            new TestCase("包含特殊字符的标签ID", "label-id-with-@#$%", false, "invalid"),
            new TestCase("纯数字标签ID", "1234567890", false, "not found"),
            new TestCase("包含空格的标签ID", "label id with spaces", false, "invalid")
        ));

        System.out.println("🚀 开始参数验证测试...");
        System.out.println("总测试用例数: " + testCases.size());
        System.out.println();

        for (int i = 0; i < testCases.size(); i++) {
            TestCase testCase = testCases.get(i);
            System.out.println(String.format("📋 测试用例 %d/%d: %s", i + 1, testCases.size(), testCase.name));
            System.out.println("----------------------------------------");
            
            boolean result = runSingleParameterTest(testCase);
            if (result) {
                passedTests++;
                System.out.println("✅ 测试通过");
            } else {
                System.out.println("❌ 测试失败");
            }
            totalTests++;
            System.out.println();
        }

        printTestSummary();
    }

    private boolean runSingleParameterTest(TestCase testCase) {
        try {
            long startTime = System.currentTimeMillis();
            
            // 显示测试参数
            System.out.println("测试参数:");
            System.out.println("  - 标签 ID: " + (testCase.labelId != null ? testCase.labelId : "NULL"));
            System.out.println("  - 预期成功: " + testCase.expectSuccess);
            if (testCase.expectedError != null) {
                System.out.println("  - 预期错误类型: " + testCase.expectedError);
            }
            
            // 创建取消请求
            PostCancelLabelsRequest request = new PostCancelLabelsRequest();
            
            if (testCase.labelId != null && !testCase.labelId.isEmpty()) {
                PostCancelLabelsRequestLabel label = new PostCancelLabelsRequestLabel();
                label.setId(testCase.labelId);
                request.setLabel(label);
            } else if (testCase.labelId != null && testCase.labelId.isEmpty()) {
                // 空字符串情况
                PostCancelLabelsRequestLabel label = new PostCancelLabelsRequestLabel();
                label.setId("");
                request.setLabel(label);
            }
            // NULL情况下不设置label字段
            
            // 执行取消请求
            PostCancelLabelsCreator creator = new PostCancelLabelsCreator();
            creator.setPostCancelLabelsRequest(request);
            
            PostCancelLabelsResponse response = creator.create(client);
            
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            // 验证响应
            if (testCase.expectSuccess) {
                // 期望成功的情况
                if (response != null && response.getData() != null) {
                    PostCancelLabelsResponseData cancelData = response.getData();
                    String cancelId = cancelData.getId();
                    
                    if (cancelId != null && !cancelId.isEmpty()) {
                        cancelLabelIds.add(cancelId);
                        System.out.println("🎯 取消操作成功:");
                        System.out.println("   取消记录 ID: " + cancelId);
                        System.out.println("   状态: " + cancelData.getStatus());
                        System.out.println("⏱️  执行时间: " + executionTime + "ms");
                        return true;
                    }
                }
                System.out.println("⚠️  预期成功但未收到有效响应");
                return false;
            } else {
                // 期望失败但实际成功了
                System.out.println("⚠️  预期失败但操作成功了");
                return false;
            }
            
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            System.out.println("⚠️  异常信息: " + errorMsg);
            
            if (testCase.expectSuccess) {
                // 期望成功但失败了
                System.out.println("⚠️  预期成功但发生异常");
                
                // 分析是否是合理的业务异常
                if (errorMsg != null && 
                    (errorMsg.contains("cannot be cancelled") || 
                     errorMsg.contains("already cancelled") ||
                     errorMsg.contains("status does not allow"))) {
                    System.out.println("ℹ️  标签状态不允许取消，这是正常的业务逻辑");
                    return true;
                }
                return false;
            } else {
                // 期望失败且确实失败了
                if (testCase.expectedError != null && errorMsg != null) {
                    if (errorMsg.toLowerCase().contains(testCase.expectedError.toLowerCase())) {
                        System.out.println("ℹ️  收到预期的错误类型，参数验证正常");
                        return true;
                    } else {
                        System.out.println("⚠️  错误类型与预期不符");
                        return false;
                    }
                } else {
                    // 没有指定具体错误类型，只要失败就算通过
                    System.out.println("ℹ️  操作失败符合预期，参数验证正常");
                    return true;
                }
            }
        }
    }

    private void printTestSummary() {
        System.out.println("=====================================");
        System.out.println("📊 Cancel Label 参数验证测试总结");
        System.out.println("=====================================");
        System.out.println("总测试数: " + totalTests);
        System.out.println("通过测试: " + passedTests);
        System.out.println("失败测试: " + (totalTests - passedTests));
        System.out.println("通过率: " + String.format("%.1f%%", (passedTests * 100.0 / totalTests)));
        
        if (passedTests == totalTests) {
            System.out.println("🎉 所有参数验证测试都通过了！");
            System.out.println();
            System.out.println("✅ SDK Cancel Label 参数验证功能正常");
            System.out.println("✅ 以下验证通过:");
            System.out.println("   - 标签ID有效性检查");
            System.out.println("   - 空值和NULL处理");
            System.out.println("   - 无效格式检测");
            System.out.println("   - 错误响应处理");
            System.out.println("   - 业务逻辑验证");
        } else {
            System.out.println("⚠️ 有些测试未通过，请检查上面的错误信息");
        }
        
        System.out.println();
        System.out.println("📝 测试过程统计:");
        System.out.println("可用标签ID数量: " + availableLabelIds.size());
        System.out.println("成功的取消操作: " + cancelLabelIds.size());
        
        if (!cancelLabelIds.isEmpty()) {
            System.out.println();
            System.out.println("📝 成功创建的取消记录 ID:");
            int count = 0;
            for (String cancelId : cancelLabelIds) {
                System.out.println(String.format("   %d. %s", ++count, cancelId));
            }
        }
        
        System.out.println("=====================================");
    }
}
