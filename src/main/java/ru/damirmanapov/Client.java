package ru.damirmanapov;

import io.atomix.catalyst.buffer.UnpooledHeapAllocator;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Server;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyOptions;
import io.atomix.catalyst.transport.netty.NettyTransport;
import ru.damirmanapov.messages.TestMessage;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class Client {

    public static void sendMessage(String messageName) {

        Properties properties = new Properties();
        NettyOptions nettyProperties = new NettyOptions(properties);

        Transport transport = new NettyTransport(nettyProperties);

        io.atomix.catalyst.transport.Client client = transport.client();

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
