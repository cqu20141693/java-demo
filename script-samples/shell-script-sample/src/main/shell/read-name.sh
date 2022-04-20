#!/usr/bin/env bash

read -p "please input you first name:" -s firstname #提示使用者输入
read -p "please input you last name:" lastname
echo -e "\nYour full name is:${firstname} ${lastname}"
read -p "请输入姓名：" NAME
read -p "请输入性别：" GENDER
read -p "请输入年龄：" AGE

cat<<EOF
********************
your info:
name: $NAME
gender: $GENDER
age: $AGE
********************
EOF
