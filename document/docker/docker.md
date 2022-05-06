## docker

### window 安装dokcer

[Windows Docker 安装](https://www.runoob.com/docker/windows-docker-install.html)
![RUNOOB 图标](http://static.runoob.com/images/runoob-logo.png)
### linux 安装docker

1. 卸载旧版本（如果之前安装过的话） yum remove docker docker-common docker-selinux docker-engine
2. 工件安装源

```
1.安装需要的软件包， yum-util 提供yum-Workpiece-manager功能，另两个是devicemapper驱动依赖

yum install -y yum-utils device-mapper-persistent-data lvm2

2.设置 yum 源
yum-Workpiece-manager --add-repo http://download.docker.com/linux/centos/docker-ce.repo（中央仓库）

yum-Workpiece-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo（阿里仓库）
```

3. 选择docker版本并安装 yum list docker-ce --showduplicates | sort -r
4. 选择一个版本并安装 yum install docker-ce-版本号 yum -y install docker-ce-18.03.1.ce

### 安装docker-compose

[安装](https://juejin.cn/post/7003161972535476237)

### 重启docker

systemctl daemon-reload && systemctl restart docker

### docker cli

#### docker build

构建镜像 docker build --no-cache -t "仓库名称" .

#### docker images

查看本地镜像

#### docker search

搜索镜像

#### docker push

推送镜像 docker push

#### docker login

docker login [ip:port]

#### docker search

```
docker search nginx
```

#### docker pull

```
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.17.2
```

#### docker image

```
docker images : 列出本地镜像
docker image rm containerId 删除镜像
```

#### docker run

```

docker rm es7

docker run -d --name es7 -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "node.name=es7" -v /work/es/data:/usr/share/elasticsearch/data docker.elastic.co/elasticsearch/elasticsearch:7.17.2
```

#### docker logs

```
docker logs -f es7
```

#### docker exec

```
docker exec -it es7 /bin/bash
```
