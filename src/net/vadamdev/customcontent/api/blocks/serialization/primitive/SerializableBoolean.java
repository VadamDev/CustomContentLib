package net.vadamdev.customcontent.api.blocks.serialization.primitive;

import net.vadamdev.customcontent.api.blocks.serialization.DataType;
import net.vadamdev.customcontent.api.blocks.serialization.ISerializableData;

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
    public boolean getBoolean() {
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
