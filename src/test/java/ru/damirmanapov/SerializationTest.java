package ru.damirmanapov;

import io.atomix.catalyst.buffer.Buffer;
import io.atomix.catalyst.buffer.UnpooledHeapAllocator;
import io.atomix.catalyst.serializer.Serializer;
import net.jodah.concurrentunit.ConcurrentTestCase;
import org.testng.annotations.Test;

import java.util.concurrent.TimeoutException;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class SerializationTest extends ConcurrentTestCase {

    @Test
    public void testGetSomething() throws TimeoutException {

        Serializer serializer = new Serializer(new UnpooledHeapAllocator());
        serializer.register(Message.class, 1);

        Message originalMessage = new Message("Message 1");
        Buffer buffer = serializer.writeObject(originalMessage);
        buffer.flip();
        Message deserializedMessage = serializer.readObject(buffer);

        assertThat(deserializedMessage, samePropertyValuesAs(originalMessage));
    }

}