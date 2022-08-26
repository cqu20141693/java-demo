### sql
### 新建用户
```
CREATE USER “test”@”localhost” IDENTIFIED BY “1234”; #本地登录
CREATE USER “superset”@”%” IDENTIFIED BY “superset”; #远程登录

利用root 用户为用户创建一个数据库(testDB)：
create database testDB;
create database testDB default charset utf8 collate utf8_general_ci;

授权test用户拥有testDB数据库的所有权限：

grant all privileges on testDB.* to “test”@”localhost” identified by “1234”;
flush privileges; #刷新系统权限表

利用root 用户修改指定用户密码

update mysql.user set authentication_string=password(“新密码”) where User=”test” and Host=”localhost”;
flush privileges;
```
#### 登录
``` 
1.  mysql -u root -p 
MySQL 8.0 Public Key Retrieval is not allowed 
在连接后面添加allowPublicKeyRetrieval=true
```
#### 清表数据
``` 
truncate table table_name;
delete * from table_name;
```
