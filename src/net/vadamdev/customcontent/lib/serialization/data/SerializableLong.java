package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableLong implements ISerializableData {
    private final long data;

    public SerializableLong(long data) {
        this.data = data;
    }

    @Override
    public long getAsLong() {
        return data;
    }

    @Override
    public String serialize() {
        return String.valueOf(data);
    }

    @Override
    public DataType getType() {
        return DataType.LONG;
    }
}
