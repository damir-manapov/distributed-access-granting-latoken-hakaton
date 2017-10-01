package ru.damirmanapov.messages;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class WrapperMessage implements CatalystSerializable {

    private CatalystSerializable message;

    public WrapperMessage() {
    }

    public WrapperMessage(CatalystSerializable message) {
        this.message = message;
    }

    public CatalystSerializable getMessage() {
        return message;
    }

    public void setMessage(CatalystSerializable message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TestMessage{" +
                "message='" + message + '\'' +
                '}';
    }

    @Override
    public void writeObject(BufferOutput<?> buffer, Serializer serializer) {
//        buffer.writeString(getMessage());

        serializer.writeObject(message);

    }

    @Override
    public void readObject(BufferInput<?> buffer, Serializer serializer) {
        setMessage(serializer.readObject(buffer));
    }
}
