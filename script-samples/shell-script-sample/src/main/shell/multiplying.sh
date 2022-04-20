#!/usr/bin/env bash
# 运算符有：“ +, -, *, /, % ”
# | bc 可以用于小数点计算
echo -e "You SHOULD input 2 numbers, I will multiplying them! \n"
read -p "first number: " firstnu
read -p "second number: " secnu
total=$((${firstnu}*${secnu}))
echo -e "\nThe result of ${firstnu} x ${secnu} is ==> ${total}"
echo "123.123*55.9" | bc