## 功能描述

​	本系统主要包含用户注册登录，用户管理（增删改查），大学专业查询，大学专业推荐的功能。

## 环境依赖

#### 1.系统环境

Linux服务器：8G  Intel(R) Core(TM) i5

#### 2.各个组件版本

| 组件          | 版本   |
| ------------- | ------ |
| Spring Boot   | 2.5.1  |
| java          | 1.8    |
| spark         | 3.2.0  |
| scala         | 2.12   |
| redis         | 4.0.9  |
| mysql         | 5.7.3  |
| hbase         | 2.4.9  |
| elasticsearch | 7.12.1 |

#### 3.项目部署

```
1.将项目打包成jar并上传
2.开启Elasticsearch ./elasticsearch
3.开启redis ./redis-cli
4.开启hbase ./start-hbase.sh
5.运行jar包 java -jar api-1.0.1.jar
```

## 项目结构

#### 1.CollegeRecomm_SpringBoot的目录结构

```
├── api                        						 // api层
│   ├── src/main/java/com/li
│   						├── aop                	 //用户操作日志记录实现
│   						├── config         		 // 配置类
│   						├── controller           // 接口类
├── beans                      						 // 配置
│   ├── src/main/java/com/li
│   						├── entity               // 实体类
│   						├── filter         		 // 过滤器
│  							├── utils                // 连接工具类
│   						├── vo                   // 帮助类
├── common
│   ├── src/main/java/com/li
│   						├── utils                // 工具类
├── mapper                         					 // 文档
│   ├── src/main/java/com/li
│   						├── dao               	 //访问mysql的操作类
│   						├── repository         	 //访问Elasticsearch的操作类
├── service
│   ├── src/main/java/com/li
│   						├── service              //业务层接口
│   								   ├── impl      //业务层实现
└── pom.xml											 //依赖库
```

#### 2.项目各子工程之间的关系

api：依赖service，实现各个接口

service：依赖mapper和common，实现业务逻辑

mapper：依赖beans，访问数据库

beans：定义实体类

common：一些通用的工具类

# 接口说明

#### 1.用户模块接口

| 接口              | 请求方式 | 参数              | 说明                           |
| ----------------- | -------- | ----------------- | ------------------------------ |
| /user/login       | POST     | User              | 用户登录接口                   |
| /user/regist      | POST     | User              | 用户注册接口                   |
| /user/logout      | GET      |                   | 用户退出接口                   |
| /user/{id}        | GET      | id                | 根据id查询用户信息接口         |
| /user/list/{name} | GET      | name              | 根据用户名模糊查询用户信息接口 |
| /user/list        | GET      | pageNum  pageSize | 查询所有用户信息接口           |
| /user/{id}        | DELETE   |                   | 删除用户信息                   |
| /user             | POST     | User              | 添加用户信息接口               |
| /user             | PUT      | User              | 更新用户信息接口               |

#### 2.大学模块接口

| 接口                  | 请求方式 | 参数                                    | 说明                               |
| :-------------------- | -------- | --------------------------------------- | ---------------------------------- |
| /colleges/list        | GET      | type  province pageNum  pageSize        | 查询所有大学接口                   |
| /colleges/list/{name} | GET      | type  province  name  pageNum  pageSize | 根据名字查询大学接口               |
| /colleges/{id}        | GET      | id                                      | 根据id查询大学详细信息接口         |
| /colleges/details     | GET      | scode  spcode                           | 根据学校名和专业名查询专业详细信息 |
| /xkpgrank             | GET      | mid                                     | 学科评估等级排名接口               |
| /rkrank               | GET      | type  province                          | 大学软科排名接口                   |
| /xyhrank              | GET      | type  province                          | 校友会排名接口                     |

#### 3.专业模块接口

| 接口                | 请求方式 | 参数              | 说明                 |
| ------------------- | -------- | ----------------- | -------------------- |
| /majors/{name}      | GET      | name              | 根据专业名查询接口   |
| /majors/list        | GET      | pageNum  pageSize | 查询所有专业接口     |
| /majors/list/{name} | GET      | name              | 根据名字查询专业接口 |

