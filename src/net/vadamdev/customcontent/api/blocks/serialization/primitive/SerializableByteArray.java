package net.vadamdev.customcontent.api.blocks.serialization.primitive;

import net.vadamdev.customcontent.api.blocks.serialization.DataType;
import net.vadamdev.customcontent.api.blocks.serialization.ISerializableData;

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
    public byte[] getByteArray() {
        return data;
    }

    @Override
    public String serialize() {
        StringBuilder strBuilder = new StringBuilder();

        int i = 1;
        for (byte b : data) {
            strBuilder.append(b);

            if(i != data.length)
                strBuilder.append(":");

            i++;
        }

        return strBuilder.toString();
    }

    @Override
    public DataType getType() {
        return DataType.BYTE_ARRAY;
    }
}
