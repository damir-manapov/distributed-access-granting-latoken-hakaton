package ru.damirmanapov;

import io.atomix.catalyst.buffer.Buffer;
import io.atomix.catalyst.buffer.UnpooledHeapAllocator;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Client;
import io.atomix.catalyst.transport.Server;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyOptions;
import io.atomix.catalyst.transport.netty.NettyTransport;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class CatalystHelloWorld {

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
                sendMessage("Message 1");
                break;
            case "secondMessage":
                System.out.println("secondMessage");
                sendMessage("Message 2");
                break;

        }
    }

    public static void startServer() {

        Properties properties = new Properties();
        NettyOptions nettyProperties = new NettyOptions(properties);
        Transport transport = new NettyTransport(nettyProperties);
        Server server = transport.server();

        Serializer serializer = new Serializer(new UnpooledHeapAllocator());
        serializer.register(Message.class, 1);
        ThreadContext context = new SingleThreadContext("test-thread-%d", serializer);

        context.executor().execute(() -> {
            try {
                server.listen(new Address(new InetSocketAddress(InetAddress.getByName("localhost"), 5555)), connection -> {
                    connection.handler(Message.class, message -> {
                        System.out.println(message);
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
                serializer.register(Message.class, 1);

                Message message = new Message(messageName);

                client.connect(new Address(new InetSocketAddress(InetAddress.getByName("localhost"), 5555))).thenAccept(connection -> {
                    connection.sendAndReceive(message).thenAccept(response -> {
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
