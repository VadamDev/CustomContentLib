package net.vadamdev.customcontent.api.blocks.serialization;

import net.vadamdev.customcontent.api.blocks.serialization.primitive.*;

import java.util.Optional;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public interface ISerializableData {
    default String getString() {
        return null;
    }

    default long getLong() {
        return 0;
    }

    default int getInt() {
        return 0;
    }

    default short getShort() {
        return 0;
    }

    default byte getByte() {
        return 0;
    }

    default double getDouble() {
        return 0.0;
    }

    default float getFloat() {
        return 0.0f;
    }

    default byte[] getByteArray() {
        return new byte[0];
    }

    default int[] getIntArray() {
        return new int[0];
    }

    String serialize();
    DataType getType();

    static Optional<ISerializableData> parseFrom(DataType type, String data) {
        ISerializableData serializableData = null;

        switch(type) {
            case STRING:
                serializableData = new SerializableString(data);
                break;
            case LONG:
                serializableData = new SerializableLong(Long.parseLong(data));
                break;
            case INT:
                serializableData = new SerializableInt(Integer.parseInt(data));
                break;
            case SHORT:
                serializableData = new SerializableShort(Short.parseShort(data));
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

        return Optional.ofNullable(serializableData);
    }
}
