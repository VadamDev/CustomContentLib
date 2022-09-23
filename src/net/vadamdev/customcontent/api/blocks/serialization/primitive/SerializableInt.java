package net.vadamdev.customcontent.api.blocks.serialization.primitive;

import net.vadamdev.customcontent.api.blocks.serialization.DataType;
import net.vadamdev.customcontent.api.blocks.serialization.ISerializableData;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableInt implements ISerializableData {
    private final int data;

    public SerializableInt(int data) {
        this.data = data;
    }

    @Override
    public int getInt() {
        return data;
    }

    @Override
    public String serialize() {
        return String.valueOf(data);
    }

    @Override
    public DataType getType() {
        return DataType.INT;
    }
}
