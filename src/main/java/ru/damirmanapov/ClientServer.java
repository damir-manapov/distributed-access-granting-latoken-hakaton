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
import ru.damirmanapov.messages.GetPartialKeyForRedistributionMessage;
import ru.damirmanapov.messages.GetPartialKeyMessage;
import ru.damirmanapov.messages.PutPartialKeyMessage;
import ru.damirmanapov.messages.TestMessage;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ClientServer {

    public static void startServer() {

        Properties properties = new Properties();
        NettyOptions nettyProperties = new NettyOptions(properties);
        Transport transport = new NettyTransport(nettyProperties);
        Server server = transport.server();

        ThreadContext context = new SingleThreadContext("test-thread-%d", getSerializer());

        context.executor().execute(() -> {
            try {
                server.listen(new Address(new InetSocketAddress(InetAddress.getByName("localhost"), 5555)), connection -> {
                    connection.handler(TestMessage.class, testMessage -> {
                        System.out.println(testMessage);
                        return CompletableFuture.completedFuture("Hello world back!");
                    });
                });
            } catch (UnknownHostException e) {
                System.out.println(e);
            }
        });
    }

    public static void sendMessage(Peer peer, CatalystSerializable message, Consumer<? super Object> action) {

        Properties properties = new Properties();
        NettyOptions nettyProperties = new NettyOptions(properties);

        Transport transport = new NettyTransport(nettyProperties);

        Client client = transport.client();

        ThreadContext context = new SingleThreadContext("test-thread-%d", getSerializer());

        context.executor().execute(() -> {
            try {
                client.connect(new Address(new InetSocketAddress(InetAddress.getByName(peer.getHost()), peer.getPort()))).thenAccept(connection -> {
                    connection.sendAndReceive(message).thenAccept(action);
                });
            } catch (UnknownHostException e) {
                System.out.println(e);
            }
        });
    }

    public static Serializer getSerializer() {

        Serializer serializer = new Serializer(new UnpooledHeapAllocator());
        serializer.register(PutPartialKeyMessage.class, 1);
        serializer.register(GetPartialKeyMessage.class, 2);
        serializer.register(GetPartialKeyForRedistributionMessage.class, 3);

        return serializer;
    }

}
