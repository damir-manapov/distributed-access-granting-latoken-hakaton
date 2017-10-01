package ru.damirmanapov.messages;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class GetPartialKeyForRedistributionMessage implements CatalystSerializable {

    private int partialIndex;

    public GetPartialKeyForRedistributionMessage() {
    }

    public GetPartialKeyForRedistributionMessage(int partialIndex) {
        this.partialIndex = partialIndex;
    }

    public int getPartialIndex() {
        return partialIndex;
    }

    public void setPartialIndex(int partialIndex) {
        this.partialIndex = partialIndex;
    }

    @Override
    public String toString() {
        return "GetPartialKeyForRedistributionMessage{" +
                "partialIndex='" + partialIndex + '\'' +
                '}';
    }

    @Override
    public void writeObject(BufferOutput<?> buffer, Serializer serializer) {
        buffer.writeInt(getPartialIndex());

    }

    @Override
    public void readObject(BufferInput<?> buffer, Serializer serializer) {
        setPartialIndex(buffer.readInt());
    }
}
