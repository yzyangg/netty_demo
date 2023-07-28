

 ## 调用逻辑
![image](https://github.com/Mylxmlxx/netty_demo/assets/114983113/6da6f47d-5e91-4e34-a925-77c2c759ea00)!

## 为什么要使用代理类呢？

- 抽象网络通信细节：代理类屏蔽了底层的网络通信细节，使得客户端调用远程服务看起来就像调用本地方法一样简单。
- 代码复用：定义的RPC接口可以在客户端和服务端共用，使得客户端和服务端的方法定义保持一致。
- 解耦：代理类将网络通信和业务逻辑分离，提高了代码的解耦性，便于维护和扩展。
- 隐藏实现细节：代理类可以在底层处理连接管理、负载均衡、序列化等细节，使得应用层代码更简洁