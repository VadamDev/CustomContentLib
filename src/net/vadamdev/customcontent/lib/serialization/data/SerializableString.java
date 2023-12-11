package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableString implements ISerializableData {
    private final String data;

    public SerializableString(String data) {
        this.data = data;
    }

    @Override
    public String getAsString() {
        return data;
    }

    @Override
    public String serialize() {
        return data;
    }

    @Override
    public DataType getType() {
        return DataType.STRING;
    }
}
