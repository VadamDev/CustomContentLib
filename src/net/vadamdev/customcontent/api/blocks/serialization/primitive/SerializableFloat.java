package net.vadamdev.customcontent.api.blocks.serialization.primitive;

import net.vadamdev.customcontent.api.blocks.serialization.DataType;
import net.vadamdev.customcontent.api.blocks.serialization.ISerializableData;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableFloat implements ISerializableData {
    private final float data;

    public SerializableFloat(float data) {
        this.data = data;
    }

    @Override
    public float getFloat() {
        return data;
    }

    @Override
    public String serialize() {
        return String.valueOf(data);
    }

    @Override
    public DataType getType() {
        return DataType.FLOAT;
    }
}
