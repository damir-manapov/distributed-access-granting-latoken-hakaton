package ru.damirmanapov;

import io.atomix.catalyst.buffer.Buffer;
import io.atomix.catalyst.buffer.UnpooledHeapAllocator;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;
import net.jodah.concurrentunit.ConcurrentTestCase;
import org.testng.annotations.Test;
import ru.damirmanapov.messages.TestMessage;

import java.util.concurrent.TimeoutException;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class SerializationTest extends ConcurrentTestCase {

    @Test
    public void testStringhtForward() throws TimeoutException {

        Serializer serializer = new Serializer(new UnpooledHeapAllocator());
        serializer.register(TestMessage.class, 1);

        TestMessage originalTestMessage = new TestMessage("TestMessage 1");
        Buffer buffer = serializer.writeObject(originalTestMessage);
        buffer.flip();
        TestMessage deserializedTestMessage = serializer.readObject(buffer);

        assertThat(deserializedTestMessage, samePropertyValuesAs(originalTestMessage));
    }

    @Test
    public void testInterface() throws TimeoutException {

        Serializer serializer = new Serializer(new UnpooledHeapAllocator());
//        serializer.register(TestMessage.class, 1);
        serializer.register(CatalystSerializable.class, 2);

        CatalystSerializable originalTestMessage = new TestMessage("TestMessage 1");
        Buffer buffer = serializer.writeObject(originalTestMessage);
        buffer.flip();
        CatalystSerializable deserializedTestMessage = serializer.readObject(buffer);

        assertThat(deserializedTestMessage, samePropertyValuesAs(originalTestMessage));
    }

}