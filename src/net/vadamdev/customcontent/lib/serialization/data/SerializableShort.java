package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableShort implements ISerializableData {
    private final short data;

    public SerializableShort(short data) {
        this.data = data;
    }

    @Override
    public short getAsShort() {
        return data;
    }

    @Override
    public String serialize() {
        return String.valueOf(data);
    }

    @Override
    public DataType getType() {
        return DataType.SHORT;
    }
}
