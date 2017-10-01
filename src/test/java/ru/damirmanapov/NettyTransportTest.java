package ru.damirmanapov;

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
import net.jodah.concurrentunit.ConcurrentTestCase;
import org.testng.annotations.Test;
import ru.damirmanapov.messages.TestMessage;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class NettyTransportTest extends ConcurrentTestCase {

    @Test
    public void testSendingString() throws TimeoutException {

        Properties properties = new Properties();
        NettyOptions nettyProperties = new NettyOptions(properties);

        Transport transport = new NettyTransport(nettyProperties);

        Server server = transport.server();
        Client client = transport.client();

        ThreadContext context = new SingleThreadContext("test-thread-%d", new Serializer());

        context.executor().execute(() -> {
            try {
                server.listen(new Address(new InetSocketAddress(InetAddress.getByName("localhost"), 4444)), connection -> {
                    connection.<String, String>handler(String.class, message -> {
                        threadAssertEquals("Hello world!", message);
                        System.out.println(message);
                        return CompletableFuture.completedFuture("Hello world back!");
                    });
                }).thenRun(this::resume);
            } catch (UnknownHostException e) {
                threadFail(e);
            }
        });

        await(10000);
        context.executor().execute(() -> {
            try {
                client.connect(new Address(new InetSocketAddress(InetAddress.getByName("localhost"), 4444))).thenAccept(connection -> {
                    connection.sendAndReceive("Hello world!").thenAccept(response -> {
                        threadAssertEquals("Hello world back!", response);
                        resume();
                    });
                });
            } catch (UnknownHostException e) {
                threadFail(e);
            }
        });
        await(10000);

        context.executor().execute(() -> {
            client.close().thenRun(this::resume);
            server.close().thenRun(this::resume);
        });
        await(10000, 2);

        assertThat(true, is(true));
    }

    @Test
    public void testSendingObject() throws TimeoutException {

        Properties properties = new Properties();
        NettyOptions nettyProperties = new NettyOptions(properties);

        Transport transport = new NettyTransport(nettyProperties);

        Server server = transport.server();
        Client client = transport.client();

        Serializer serializer = new Serializer(new UnpooledHeapAllocator());
        serializer.register(TestMessage.class, 1);
        ThreadContext context = new SingleThreadContext("test-thread-%d", serializer);

        context.executor().execute(() -> {
            try {
                server.listen(new Address(new InetSocketAddress(InetAddress.getByName("localhost"), 4444)), connection -> {
                    connection.handler(TestMessage.class, testMessage -> {
                        threadAssertEquals("TestMessage", testMessage.getName());
                        System.out.println(testMessage);
                        return CompletableFuture.completedFuture("Hello world back!");
                    });
                }).thenRun(this::resume);
            } catch (UnknownHostException e) {
                threadFail(e);
            }
        });

        await(10000);
        context.executor().execute(() -> {
            try {

                TestMessage testMessage = new TestMessage("TestMessage");

                client.connect(new Address(new InetSocketAddress(InetAddress.getByName("localhost"), 4444))).thenAccept(connection -> {
                    connection.sendAndReceive(testMessage).thenAccept(response -> {
                        threadAssertEquals("Hello world back!", response);
                        resume();
                    });
                });
            } catch (UnknownHostException e) {
                threadFail(e);
            }
        });
        await(10000);

        context.executor().execute(() -> {
            client.close().thenRun(this::resume);
            server.close().thenRun(this::resume);
        });
        await(10000, 2);

        assertThat(true, is(true));
    }

}