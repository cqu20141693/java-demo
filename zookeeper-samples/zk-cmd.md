### zk 命令
1. zkCli.sh : 连接服务端
2. get [-s] [-w] path
```
-w 添加一个 watch
eg: get -s /mynode
```
3. set [-s] [-v version] path data
``` 
修改已经存在的节点的值
set /mynode hello

```
4. create [-s] [-e] [-c] [-t ttl] path [data] [acl]
```
-s 创建有序节点
-e 创建临时节点
eg: create /mynode hello
```
5. ls [-s] [-w] [-R] path
``` 
-w 添加一个 watch（监视器）
eg: ls -s /
```
6. get [-s] [-w] path
``` 
-w 添加一个 watch
eg: get -s /mynode
```
7. stat [-w] path
``` 
-w 添加一个 watch
eg: stat /mynode
```
8. delete path [version] | deleteall path [-b batch size]
``` 
eg: delete /zk
eg : deleteall /zk
```

### zk 状态信息
``` 
czxid，创建（create）该 znode 的 zxid
mzxid，最后一次修改（modify）该 znode 的 zxid
pzxid，最后一次修改该 znode 子节点的 zxid
ctime，创建该 znode 的时间
mtime，最后一次修改该 znode 的时间
dataVersion，该节点内容的版本，每次修改内容，版本都会增加
cversion，该节点子节点的版本
aclVersion，该节点的 ACL 版本
ephemeralOwner，如果该节点是临时节点（ephemeral node），会列出该节点所在客户端的 session id；如果不是临时节点，该值为 0
dataLength，该节点存储的数据长度
numChildren，该节点子节点的个数

```