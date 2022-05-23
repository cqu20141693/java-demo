### ansible
#### 安装Ansible
1. 解压安装包

```bash
tar -xvf ansible_v2.9.9_install.tar.gz 
```

2. 执行安装

```bash
cd ansible_v2.9.9_install/
sh ansible_v2.9.0_install.sh
```

3. 免密登录

```bash
# 生成key
ssh-keygen
# 将生成的 .ssh/id_rsa.pub 内容拷贝到目标机器 /root/.ssh/authorized_keys 中
cat .ssh/id_rsa.pub
```

#### 编写部署任务
1. 配置hosts
2. 配置部署任务（nginx.yml）
3. 配置roles 具体任务
4. 配置全局变量group_var(配置全局变动变量)
