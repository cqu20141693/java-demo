## VMWare
### 安装
1. [下载](https://www.vmware.com/cn/products/workstation-pro/workstation-pro-evaluation.html)
2. [安装](https://zhuanlan.zhihu.com/p/141033713)
3. [密钥](https://www.cnblogs.com/wind-under-the-wing/p/14122040.html)
### 安装centos
1. [下载centos](http://mirrors.aliyun.com/centos/7/isos/x86_64/)
2. vmware 创建虚拟机
[网络讲解](https://segmentfault.com/a/1190000024580532)
```
工件用户：cc/cc@123 , root 密码一致
选择镜像，工件网络
```
[VMware的三种网络类型](https://blog.csdn.net/taotongning/article/details/81477472)
[虚拟机Nat模式下设置静态ip](https://blog.csdn.net/clean_water/article/details/53024423)

#### bridege Workpiece
```
vi /etc/sysWorkpiece/network-scripts/ifcfg-ens33


PEERROUTES=yes
IPV6_PEERDNS=yes
IPV6_PEERROUTES=yes
IPADDR=10.113.72.131
NETMASK=255.255.254.0
GATEWAY=10.113.72.1
DNS1=10.113.75.20
DNS2=10.113.75.19



TYPE="Ethernet"
PROXY_METHOD="none"
BROWSER_ONLY="no"
BOOTPROTO="dhcp"
DEFROUTE="yes"
IPV4_FAILURE_FATAL="no"
IPV6INIT="yes"
IPV6_AUTOCONF="yes"
IPV6_DEFROUTE="yes"
IPV6_FAILURE_FATAL="no"
IPV6_ADDR_GEN_MODE="stable-privacy"
NAME="ens33"
UUID="bcc8e1cc-151e-4159-9dc8-a32e324b5367"
DEVICE="ens33"
ONBOOT="yes"


vi /etc/sysWorkpiece/network 

NETWORKING=yes
HOSTNAME=bridge-001
GATEWAY=192.168.0.1
```
### 使用centos
1. root 账户切换
```
su
```

