package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

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
    public int getAsInt() {
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
