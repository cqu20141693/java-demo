## superset

### 源码搭建

#### [后端](https://preset.io/blog/tutorial-contributing-code-to-apache-superset/)
``` 
1.superset db upgrade #初始化数据库
2.superset fab create-admin #初始化用户
3.superset init # 初始化
4.superset run -h 0.0.0.0 -p 8088 --with-threads --reload --debugger # 启动

SUPERSET_CONFIG_PATH=D:\python-project\superset\superset\conf\superset_config.py


SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join(DATA_DIR, "superset.db")
# SQLALCHEMY_DATABASE_URI = 'mysql+pymysql://superset:superset@192.168.96.163:3306/superset'
# SQLALCHEMY_DATABASE_URI = 'postgresql://root:password@localhost/myapp'
```
1. [load examples](https://blog.csdn.net/yan15625123250/article/details/121077103) 

#### 前端

1. 安装依赖包

```
cd superset-frontend
npm install
```

2. 运行前端

```
cd superset-frontend
npm run 
npm run dev-server --devserverPort=9000
```

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
