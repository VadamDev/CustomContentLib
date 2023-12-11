package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableBoolean implements ISerializableData {
    private final boolean data;

    public SerializableBoolean(boolean data) {
        this.data = data;
    }

    @Override
    public boolean getAsBoolean() {
        return data;
    }

    @Override
    public String serialize() {
        return Boolean.toString(data);
    }

    @Override
    public DataType getType() {
        return DataType.BOOLEAN;
    }
}
