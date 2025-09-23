#!/bin/bash

# AfterShip Shipping SDK Published Package Test Runner (Version 3.0.0)
# 此脚本用于测试已发布的 SDK 包，确保发布版本的功能正常

echo "========================================="
echo "AfterShip Shipping SDK 发布包测试 (3.0.0)"
echo "========================================="
echo "测试目标: 验证已发布的 Maven 包功能是否正常"
echo "包版本: 3.0.0"
echo "测试时间: $(date)"
echo

# 检查当前目录是否包含必要的文件
if [ ! -f "pom.xml" ]; then
    echo "❌ 错误: 未找到 pom.xml 文件"
    echo "请确保在正确的测试目录中运行此脚本"
    exit 1
fi

# 检查 Java 环境
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到 Java"
    echo "请安装 Java 8 或更高版本"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo "❌ 错误: 未找到 Maven"
    echo "请安装 Maven"
    exit 1
fi

echo "🔍 环境检查:"
java -version
echo
mvn -version
echo

# 编译项目
echo "🔧 编译项目..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "❌ 编译失败"
    exit 1
fi
echo "✅ 编译成功"
echo

# 运行测试统计
total_tests=0
passed_tests=0
failed_tests=0

# 定义测试函数
run_test() {
    local test_name="$1"
    local test_class="$2"
    
    echo "🚀 运行测试: $test_name"
    echo "----------------------------------------"
    
    # 设置 classpath
    CLASSPATH="target/classes:target/dependency/*"
    
    # 运行测试
    java -cp "$CLASSPATH" "$test_class"
    local result=$?
    
    total_tests=$((total_tests + 1))
    
    if [ $result -eq 0 ]; then
        echo "✅ $test_name - 通过"
        passed_tests=$((passed_tests + 1))
    else
        echo "❌ $test_name - 失败"
        failed_tests=$((failed_tests + 1))
    fi
    echo
}

# 复制依赖到 target/dependency
echo "📦 复制依赖..."
mvn dependency:copy-dependencies
if [ $? -ne 0 ]; then
    echo "❌ 依赖复制失败"
    exit 1
fi
echo "✅ 依赖复制成功"
echo

# 运行功能测试
echo "📋 开始运行功能测试..."
echo "========================================="

run_test "Label 功能测试" "LabelFunctionalityTest"
run_test "Rate 功能测试" "RateFunctionalityTest"  
run_test "Cancel Label 功能测试" "CancelLabelFunctionalityTest"

echo "📋 开始运行参数验证测试..."
echo "========================================="

run_test "Label 参数验证测试" "LabelParameterValidationTest"
run_test "Rate 参数验证测试" "RateParameterValidationTest"
run_test "Cancel Label 参数验证测试" "CancelLabelParameterValidationTest"

# 打印总结
echo "========================================="
echo "📊 测试总结"
echo "========================================="
echo "总测试数: $total_tests"
echo "通过测试: $passed_tests"  
echo "失败测试: $failed_tests"

if [ $failed_tests -eq 0 ]; then
    success_rate="100.0"
else
    success_rate=$(echo "scale=1; $passed_tests * 100 / $total_tests" | bc)
fi

echo "成功率: ${success_rate}%"
echo "测试完成时间: $(date)"

if [ $failed_tests -eq 0 ]; then
    echo
    echo "🎉 所有测试都通过了！"
    echo "✅ AfterShip Shipping SDK 3.0.0 发布包功能正常"
    echo "✅ 主要功能验证:"
    echo "   - Labels API (创建、获取、列表)"
    echo "   - Rates API (计算、获取、列表)"  
    echo "   - Cancel Labels API (取消、获取、列表)"
    echo "   - 参数验证和枚举值设置"
    echo "   - 错误处理机制"
    echo
    echo "📝 发布包测试结论:"
    echo "   已发布的 Maven 包 (com.aftership:shipping-sdk:3.0.0) 功能完整且正常工作"
    exit 0
else
    echo
    echo "⚠️  有 $failed_tests 个测试未通过"
    echo "请检查上面的错误信息并修复相关问题"
    exit 1
fi
