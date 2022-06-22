## centos

### 解压缩

``` 
unzip deploy-tool.zip
tar -xvf ansible_v2.9.9_install.tar.gz
```

### 磁盘

``` 
df -hl 查看磁盘剩余空间
df -h 查看每个根路径的分区大小
du -sh [目录名] 返回该目录的大小
du -sm [文件夹] 返回该文件夹总M数 
du -h [目录名] 查看指定文件夹下的所有文件大小（包含子文件夹）

```

### rpm 安装卸载

``` 
卸载mysql
rpm -qa |grep -i mysql
yum remove mysql-community-common-5.7.20-1.el7.x86_64
```

### 进程命令

``` 
top
kill -9/15

netstat -tunlp 
```

### systemctl

``` 
systemctl start mysqld.service 
systemctl status mysqld.service 
systemctl stop mysqld.service 
systemctl enable mysqld.service 

```

### 时间

1. 查看，修改时区

``` 
列出时区：  timedatectl list-timezones
设置时区：timedatectl set-timezone Asia/Shanghai
```

2. 查看修改时间

``` 
1.显示时间 ：  date
2.修改时间  date -s  时间
如：设置当前时间为：2018年12月10点50分：date -s  ‘2018-12-14 10:50:00’
```

3. 同步时间

``` 
安装：yum install ntp
同步：ntpdate pool.ntp.org
```

### 文件管理

#### 文件权限

```
 
```

### vi、vim

#### 文件内容替换

``` 
sed -i 's/Search_String/Replacement_String/g' Input_File

```

#### dd 命令删除一整行，使用 . 命令会 ==重复删除当前行==。

### ssh 免密登录

``` 
ssh-keygen 
cd /root/.ssh

// 发送pub key 到192.168.96.234
ssh-copy-id -i ~/.ssh/id_rsa.pub root@192.168.96.234
免密登录
ssh 'root@192.168.96.234'

```

### 账户管理

#### 添加用户

``` 
sudo useradd username -m #创建用户
sudo userdel -r username #删除用户
su username #切换到指定用户，username指的是用户名
```

#### 账户切换

``` 
普通用户切换root
su root 
输入密码

root 切换普通用户


```

### yum

``` 

yum install python37 --downloadonly --downloaddir=/work/repo

```

### ifconfig

1. 添加虚拟网卡到wlp2s0命令 sudo ifconfig wlp2s0:1 192.168.10.11 up
2. 查看某个网段地址 ifconfig |grep 192

### vim

#### Esc 模式

1. 搜索文本

```
/value
继续查找此关键字，敲字符 n
```

2. 删除单个字符 x 命令会 ==删除光标下的字符==，使用 . 会让 ==重复删除光标下的字符==。

3. 删除一行 dd 命令删除一整行，使用 . 命令会 ==重复删除当前行==

4. 撤销修改 多次输入 u 撤销上述修改

#### Insert 模式

### ps

1. 查看进程

``` 
1. 查看进程启动信息
ps -ef |grep mysql

2. 查看进程内存和cpu使用情况，PID后的信息
ps -aux | grep kafka 

ps aux | sort -k4,4nr | head -n 10 查看内存占用前10名的程序
```

### top

1. 查看进程情况

```
top |grep mysql
```

2. 查看某个进程的内存使用情况

```
top -p PID
```

### grep

1. 搜索文件

``` 
grep file value ：搜索
```

2. -v : 排除

``` 
 ps -ef | grep zookeeper | grep -v ‘grep’ : 搜索zookeeper的启动命令，排查包括grep指令的
```
