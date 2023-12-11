package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableDouble implements ISerializableData {
    private final double data;

    public SerializableDouble(double data) {
        this.data = data;
    }

    @Override
    public double getAsDouble() {
        return data;
    }

    @Override
    public String serialize() {
        return String.valueOf(data);
    }

    @Override
    public DataType getType() {
        return DataType.DOUBLE;
    }
}
