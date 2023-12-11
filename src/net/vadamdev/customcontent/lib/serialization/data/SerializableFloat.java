package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

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
    public float getAsFloat() {
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
