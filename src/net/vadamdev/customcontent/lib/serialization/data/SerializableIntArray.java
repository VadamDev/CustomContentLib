package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class SerializableIntArray implements ISerializableData {
    private final int[] data;

    public SerializableIntArray(int[] data) {
        this.data = data;
    }

    @Override
    public int[] getAsIntArray() {
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
        return DataType.INT_ARRAY;
    }
}
