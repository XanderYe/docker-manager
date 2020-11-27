<h1 align="center">Docker Manager</h1>

## 描述

使用 Vue + SpringBoot 开发的docker容器可视化配置UI，支持不删除容器情况下修改容器的端口映射、路径挂载、环境变量配置

## 界面截图

### 登录界面

![登录界面](https://raw.githubusercontent.com/XanderYe/docker-manager/1.0/login.png)

### 容器管理界面

![容器管理界面](https://raw.githubusercontent.com/XanderYe/docker-manager/1.0/main.png)

### 容器配置编辑界面

![容器配置编辑界面](https://raw.githubusercontent.com/XanderYe/docker-manager/1.0/edit.png)

## 使用说明

1. 必须在宿主机使用root账户启动服务
2. 每次操作后都会生成备份文件，如果出现问题请手动恢复，在 /var/lib/docker/containers/容器id/ 下，备份文件为 config.v2.json.bak 和 hostconfig.json.bak

## 部署方法

### 直接使用

1. 下载1.0版本[docker-manager1.0.tar.gz](https://github.com/XanderYe/docker-manager/releases/download/1.0/dockermanager1.0.tar.gz)
2. 解压dockermanager文件夹，编辑配置 `vim start.sh`，修改系统的用户名与密码
3. 安装 java 1.8 运行环境<br/>Debian: `apt install -y openjdk-8-jdk`<br/>CentOS: `yum install -y java-1.8.0-openjdk`
4. 执行 `./start.sh`

### 自己编译

1. `git clone https://github.com/XanderYe/docker-manager.git`
2. 执行 `mvn install`
3. 静态资源路径：`web/dist`<br/>后台服务路径 `api/target/docker-manager.jar`
4. 修改dist/static/setting.js，指向后台服务接口<br/>，部署到web容器中
5. 执行 `nohup java -jar docker-manager.jar \
       --server.port=8080 \
       --user.username=admin \
       --user.password=123456 \ 
       --logging.file.name=logs/docker-manager.log &` 启动后台服务 
