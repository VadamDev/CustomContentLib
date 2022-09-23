package net.vadamdev.customcontent.api.blocks.serialization.primitive;

import net.vadamdev.customcontent.api.blocks.serialization.DataType;
import net.vadamdev.customcontent.api.blocks.serialization.ISerializableData;

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
    public int[] getIntArray() {
        return data;
    }

    @Override
    public String serialize() {
        StringBuilder strBuilder = new StringBuilder();

        int i = 1;
        for (int b : data) {
            strBuilder.append(b);

            if(i != data.length)
                strBuilder.append(":");

            i++;
        }

        return strBuilder.toString();
    }

    @Override
    public DataType getType() {
        return DataType.INT_ARRAY;
    }
}
