package ru.damirmanapov.messages;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class PutPartialKeyMessage implements CatalystSerializable {

    private String partialKey;

    public PutPartialKeyMessage() {
    }

    public PutPartialKeyMessage(String partialKey) {
        this.partialKey = partialKey;
    }

    public String getPartialKey() {
        return partialKey;
    }

    public void setPartialKey(String partialKey) {
        this.partialKey = partialKey;
    }

    @Override
    public String toString() {
        return "PutPartialKeyMessage{" +
                "partialKey='" + partialKey + '\'' +
                '}';
    }

    @Override
    public void writeObject(BufferOutput<?> buffer, Serializer serializer) {
        buffer.writeString(getPartialKey());

    }

    @Override
    public void readObject(BufferInput<?> buffer, Serializer serializer) {
        setPartialKey(buffer.readString());
    }
}
