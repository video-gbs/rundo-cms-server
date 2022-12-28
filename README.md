# rundo-cms-server

## 中间件与支撑服务版本详情

| 中间件      | 版本     | 
|----------|--------|
| MySQL    | 8.0.31 |
| Redis    | 6.2.8  |
| RabbitMQ | 3.11.5 |
| Nacos    | 2.2.0  |
|          |        |
|          |        |


## 服务域名映射关系
| IP        | 域名           | 服务       |
|-----------|--------------|----------|
| 127.0.0.1 | rundo-mysql  | MySQL    |
| 127.0.0.1 | rundo-redis  | Redis    |
| 127.0.0.1 | rundo-rabbit | RabbitMQ |
| 127.0.0.1 | rundo-nacos  | Nacos    |
|           |              |          |
|           |              |          |

- **注意：** 
1. 请将上述IP `127.0.0.1` 替换为服务所在的 ip地址
2. 推荐在 `host文件` 中添加 ip域名映射；开发环境可以使用 [SwitchHosts](https://github.com/oldj/SwitchHosts/releases) 工具来管理hosts文件


