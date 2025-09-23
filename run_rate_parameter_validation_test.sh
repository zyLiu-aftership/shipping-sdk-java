#!/bin/bash

echo "==============================================="
echo "AfterShip SDK Rate 参数验证测试运行器"
echo "==============================================="

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # 无颜色

# 打印状态信息的函数
print_status() {
    echo -e "${2}${1}${NC}"
}

print_status "🔍 检查环境..." $BLUE

# 检查是否在正确的目录中
if [ ! -f "pom.xml" ]; then
    print_status "❌ 错误: 未找到 pom.xml。请在 SDK 根目录中运行此脚本。" $RED
    exit 1
fi

# 检查测试文件是否存在
if [ ! -f "RateParameterValidationTest.java" ]; then
    print_status "❌ 错误: 未找到 RateParameterValidationTest.java" $RED
    exit 1
fi

print_status "✅ 找到参数验证测试文件" $GREEN

print_status "🏗️  编译 SDK..." $BLUE

# 编译 SDK
mvn compile -q
if [ $? -ne 0 ]; then
    print_status "❌ SDK 编译失败" $RED
    exit 1
fi

print_status "✅ SDK 编译成功" $GREEN

print_status "📦 准备依赖..." $BLUE

# 复制依赖到 target/dependency
mvn dependency:copy-dependencies -DoutputDirectory=target/dependency -q
if [ $? -ne 0 ]; then
    print_status "❌ 依赖复制失败" $RED
    exit 1
fi

print_status "✅ 依赖准备完成" $GREEN

print_status "🏗️  编译参数验证测试类..." $BLUE

# 设置 classpath
CLASSPATH="target/classes"
for jar in target/dependency/*.jar; do
    CLASSPATH="$CLASSPATH:$jar"
done

# 编译测试类
javac -cp "$CLASSPATH" -d target/classes RateParameterValidationTest.java
if [ $? -ne 0 ]; then
    print_status "❌ 参数验证测试类编译失败" $RED
    print_status "ℹ️  请检查代码是否有语法错误" $YELLOW
    exit 1
fi

print_status "✅ 参数验证测试类编译成功" $GREEN

print_status "🚀 运行 Rate 参数验证测试..." $CYAN
print_status "📋 此测试将验证以下枚举类型和参数:" $BLUE
print_status "   • 运费状态: calculating, calculated, failed" $BLUE
print_status "   • 重量单位: lb, kg, oz, g" $BLUE  
print_status "   • 尺寸单位: cm, in, mm, m, ft, yd" $BLUE
print_status "   • 货币类型: USD, EUR, GBP, CAD等" $BLUE
print_status "   • 异步/同步计算模式" $BLUE
print_status "   • 文件/包裹运输类型" $BLUE
print_status "   • 边界值和异常值处理" $BLUE
echo ""

# 运行测试
java -cp "$CLASSPATH" RateParameterValidationTest

TEST_EXIT_CODE=$?

echo ""
if [ $TEST_EXIT_CODE -eq 0 ]; then
    print_status "🎉 参数验证测试执行完成!" $GREEN
else
    print_status "⚠️  参数验证测试执行完成，存在一些问题 (退出代码: $TEST_EXIT_CODE)" $YELLOW
fi

print_status "📋 测试产物:" $BLUE
echo "  - target/classes/RateParameterValidationTest.class (编译后的测试类)"
echo "  - 控制台输出显示了详细的参数验证结果"

echo ""
print_status "===============================================" $PURPLE
print_status "AfterShip SDK Rate 参数验证测试完成" $PURPLE  
print_status "===============================================" $PURPLE
