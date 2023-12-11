package net.vadamdev.customcontent.lib.serialization.data;

import net.vadamdev.customcontent.lib.serialization.DataType;

import javax.annotation.Nullable;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public interface ISerializableData {
    @Nullable
    static ISerializableData parseFrom(DataType type, String data) {
        ISerializableData serializableData = null;

        switch(type) {
            case BOOLEAN:
                serializableData = new SerializableBoolean(Boolean.parseBoolean(data));
                break;
            case BYTE:
                serializableData = new SerializableByte(Byte.parseByte(data));
                break;
            case DOUBLE:
                serializableData = new SerializableDouble(Double.parseDouble(data));
                break;
            case FLOAT:
                serializableData = new SerializableFloat(Float.parseFloat(data));
                break;
            case INT:
                serializableData = new SerializableInt(Integer.parseInt(data));
                break;
            case LONG:
                serializableData = new SerializableLong(Long.parseLong(data));
                break;
            case SHORT:
                serializableData = new SerializableShort(Short.parseShort(data));
                break;
            case STRING:
                serializableData = new SerializableString(data);
                break;
            case INT_ARRAY:
                String[] split = data.split(":");

                int[] intArray = new int[split.length];
                for (int j = 0; j < split.length; j++)
                    intArray[j] = Integer.parseInt(split[j]);

                serializableData = new SerializableIntArray(intArray);

                break;
            case BYTE_ARRAY:
                String[] split1 = data.split(":");

                byte[] byteArray = new byte[split1.length];
                for (int j = 0; j < split1.length; j++)
                    byteArray[j] = Byte.parseByte(split1[j]);

                serializableData = new SerializableByteArray(byteArray);

                break;
            default:
                break;
        }

        return serializableData;
    }

    String serialize();
    DataType getType();

    default String getAsString() {
        return null;
    }

    default boolean getAsBoolean() {
        return false;
    }

    default long getAsLong() {
        return 0;
    }

    default int getAsInt() {
        return 0;
    }

    default short getAsShort() {
        return 0;
    }

    default byte getAsByte() {
        return 0;
    }

    default double getAsDouble() {
        return 0.0D;
    }

    default float getAsFloat() {
        return 0.0f;
    }

    default byte[] getAsByteArray() {
        return new byte[0];
    }

    default int[] getAsIntArray() {
        return new int[0];
    }
}
