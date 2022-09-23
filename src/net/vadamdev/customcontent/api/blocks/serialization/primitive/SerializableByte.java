package net.vadamdev.customcontent.api.blocks.serialization.primitive;

import net.vadamdev.customcontent.api.blocks.serialization.DataType;
import net.vadamdev.customcontent.api.blocks.serialization.ISerializableData;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableByte implements ISerializableData {
    private final byte data;

    public SerializableByte(byte data) {
        this.data = data;
    }

    @Override
    public byte getByte() {
        return data;
    }

    @Override
    public String serialize() {
        return String.valueOf(data);
    }

    @Override
    public DataType getType() {
        return DataType.BYTE;
    }
}
