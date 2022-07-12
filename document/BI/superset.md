## superset
### docker install
``` 

mkdir -p /work/superset

docker run -d --name superset -p 8088:8088 -v /work/superset:/home/superset amancevice/superset

# 设置本地superset系统管理员账户
docker exec -it superset superset fab create-admin \
              --username admin \
              --firstname Superset \
              --lastname Admin \
              --email admin@superset.com \
              --password admin
# 将本地数据库迁移到最新版本
docker exec -it superset superset db upgrade

# 加载例子(可忽略，慢需要翻墙)
docker exec -it superset superset load_examples
# 初始化设置角色
docker exec -it superset superset init
```
### 使用
1. 登录
``` 
admin/admin
http://192.168.96.168:8088/
```
2. add Database

3. 
