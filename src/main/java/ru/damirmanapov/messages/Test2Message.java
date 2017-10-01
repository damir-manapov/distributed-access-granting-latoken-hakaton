package ru.damirmanapov.messages;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class Test2Message implements CatalystSerializable {

    private String name;

    public Test2Message() {
    }

    public Test2Message(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestMessage{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void writeObject(BufferOutput<?> buffer, Serializer serializer) {
        buffer.writeString(getName());

    }

    @Override
    public void readObject(BufferInput<?> buffer, Serializer serializer) {
        setName(buffer.readString());
    }
}
