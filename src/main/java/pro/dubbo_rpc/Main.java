package pro.dubbo_rpc;

import pro.dubbo_rpc.server.Server;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 23:30
 */
public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer("localhost", 8080);
    }
}
