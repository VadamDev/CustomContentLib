package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableByteArray implements ISerializableData {
    private final byte[] data;

    public SerializableByteArray(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] getAsByteArray() {
        return data;
    }

    @Override
    public String serialize() {
        StringBuilder strBuilder = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            strBuilder.append(data[i]);

            if(i + 1 != data.length)
                strBuilder.append(":");
        }

        return strBuilder.toString();
    }

    @Override
    public DataType getType() {
        return DataType.BYTE_ARRAY;
    }
}
