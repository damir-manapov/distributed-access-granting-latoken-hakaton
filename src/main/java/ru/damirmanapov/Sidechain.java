package ru.damirmanapov;

import io.atomix.catalyst.buffer.UnpooledHeapAllocator;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Client;
import io.atomix.catalyst.transport.Server;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyOptions;
import io.atomix.catalyst.transport.netty.NettyTransport;
import ru.damirmanapov.messages.TestMessage;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class Sidechain {

    public static Map<Integer, String> parts = new HashMap();

    public static void main(String[] args) {

        // Print arguments
        for (String s : args) {
            System.out.println("argument: " + s);
        }

        switch (args[0]) {
            case "test":
                System.out.println("test");
                break;
            case "server":
                startServer();
                break;
            case "firstMessage":
                System.out.println("firstMessage");
                sendMessage("TestMessage 1");
                break;
            case "secondMessage":
                System.out.println("secondMessage");
                sendMessage("TestMessage 2");
                break;

        }
    }

    public static void startServer() {

        Properties properties = new Properties();
        NettyOptions nettyProperties = new NettyOptions(properties);
        Transport transport = new NettyTransport(nettyProperties);
        Server server = transport.server();

        Serializer serializer = new Serializer(new UnpooledHeapAllocator());
        serializer.register(TestMessage.class, 1);
        ThreadContext context = new SingleThreadContext("test-thread-%d", serializer);

        context.executor().execute(() -> {
            try {
                server.listen(new Address(new InetSocketAddress(InetAddress.getByName("localhost"), 5555)), connection -> {
                    connection.handler(CatalystSerializable.class, testMessage -> {
                        System.out.println(testMessage);
                        return CompletableFuture.completedFuture("Hello world back!");
                    });
                });
            } catch (UnknownHostException e) {
                System.out.println(e);
            }
        });
    }

    public static void sendMessage(String messageName) {

        Properties properties = new Properties();
        NettyOptions nettyProperties = new NettyOptions(properties);

        Transport transport = new NettyTransport(nettyProperties);

        Client client = transport.client();

        ThreadContext context = new SingleThreadContext("test-thread-%d", new Serializer());

        context.executor().execute(() -> {
            try {

                Serializer serializer = new Serializer(new UnpooledHeapAllocator());
                serializer.register(TestMessage.class, 1);

                TestMessage testMessage = new TestMessage(messageName);

                client.connect(new Address(new InetSocketAddress(InetAddress.getByName("localhost"), 5555))).thenAccept(connection -> {
                    connection.sendAndReceive(testMessage).thenAccept(response -> {
                        System.out.println("response: " + response);
                        client.close();
                        System.exit(0);
                    });
                });
            } catch (UnknownHostException e) {
                System.out.println(e);
            }
        });
    }

}
