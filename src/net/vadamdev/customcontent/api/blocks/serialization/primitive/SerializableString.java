package net.vadamdev.customcontent.api.blocks.serialization.primitive;

import net.vadamdev.customcontent.api.blocks.serialization.DataType;
import net.vadamdev.customcontent.api.blocks.serialization.ISerializableData;

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
    public String getString() {
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
