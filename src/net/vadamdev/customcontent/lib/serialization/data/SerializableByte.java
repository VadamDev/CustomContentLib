package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableByte implements ISerializableData {
    private final byte data;

    public SerializableByte(byte data) {
        this.data = data;
    }

    @Override
    public byte getAsByte() {
        return data;
    }

    @Override
    public String serialize() {
        return String.valueOf(data);
    }

    @Override
    public DataType getType() {
        return DataType.BYTE;
    }
}
