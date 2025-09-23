#!/bin/bash

echo "========================================="
echo "AfterShip SDK Cancel Label 功能测试 (发布包 3.0.0)"
echo "========================================="

# 编译项目
echo "🔧 编译项目..."
mvn clean compile dependency:copy-dependencies -q

if [ $? -ne 0 ]; then
    echo "❌ 编译失败"
    exit 1
fi

# 设置 classpath
CLASSPATH="target/classes:target/dependency/*"

# 运行测试
echo "🚀 运行 Cancel Label 功能测试..."
java -cp "$CLASSPATH" CancelLabelFunctionalityTest

echo "🚀 运行 Cancel Label 参数验证测试..."
java -cp "$CLASSPATH" CancelLabelParameterValidationTest
