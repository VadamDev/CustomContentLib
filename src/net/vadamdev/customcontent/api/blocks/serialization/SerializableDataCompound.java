package net.vadamdev.customcontent.api.blocks.serialization;

import net.vadamdev.customcontent.api.blocks.serialization.primitive.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 19/09/2022
 */
public class SerializableDataCompound {
    private final Map<String, ISerializableData> map = new HashMap<>();

    public void put(String key, ISerializableData serializableData) {
        map.put(key, serializableData);
    }

    public void putString(String key, String data) {
        map.put(key, new SerializableString(data));
    }

    public void putLong(String key, long data) {
        map.put(key, new SerializableLong(data));
    }

    public void putInt(String key, int data) {
        map.put(key, new SerializableInt(data));
    }

    public void putShort(String key, short data) {
        map.put(key, new SerializableShort(data));
    }

    public void putByte(String key, byte data) {
        map.put(key, new SerializableByte(data));
    }

    public void putDouble(String key, double data) {
        map.put(key, new SerializableDouble(data));
    }

    public void putFloat(String key, float data) {
        map.put(key, new SerializableFloat(data));
    }

    public void putByteArray(String key, byte[] data) {
        map.put(key, new SerializableByteArray(data));
    }

    public void putIntArray(String key, int[] data) {
        map.put(key, new SerializableIntArray(data));
    }

    /*

     */

    public String getString(String key) {
        requireGoodTypeAndNonNull(key, DataType.STRING);
        return map.get(key).getString();
    }

    public long getLong(String key) {
        requireGoodTypeAndNonNull(key, DataType.LONG);
        return map.get(key).getLong();
    }

    public int getInt(String key) {
        requireGoodTypeAndNonNull(key, DataType.INT);
        return map.get(key).getInt();
    }

    public short getShort(String key) {
        requireGoodTypeAndNonNull(key, DataType.SHORT);
        return map.get(key).getShort();
    }

    public byte getByte(String key) {
        requireGoodTypeAndNonNull(key, DataType.BYTE);
        return map.get(key).getByte();
    }

    public double getDouble(String key) {
        requireGoodTypeAndNonNull(key, DataType.DOUBLE);
        return map.get(key).getDouble();
    }

    public float getFloat(String key) {
        requireGoodTypeAndNonNull(key, DataType.FLOAT);
        return map.get(key).getFloat();
    }

    public byte[] getByteArray(String key) {
        requireGoodTypeAndNonNull(key, DataType.BYTE_ARRAY);
        return map.get(key).getByteArray();
    }

    public int[] getIntArray(String key) {
        requireGoodTypeAndNonNull(key, DataType.INT_ARRAY);
        return map.get(key).getIntArray();
    }

    public Map<String, ISerializableData> getMapCopy() {
        return new HashMap<>(map);
    }

    private void requireGoodTypeAndNonNull(String key, DataType type) {
        if(!map.containsKey(key))
            throw new NullPointerException();

        ISerializableData data = map.get(key);

        if(!data.getType().equals(type))
            throw new UnsupportedOperationException("Provided data type is not the good type");
    }
}
