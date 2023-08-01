package net.vadamdev.customcontent.api.blocks.serialization;

import net.vadamdev.customcontent.api.blocks.serialization.primitive.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 19/09/2022
 */
public final class SerializableDataCompound {
    private final Map<String, ISerializableData> data;

    public SerializableDataCompound() {
        this.data = new HashMap<>();
    }

    public void put(String key, ISerializableData serializableData) {
        data.put(key, serializableData);
    }

    public void putString(String key, String data) {
        this.data.put(key, new SerializableString(data));
    }

    public void putLong(String key, long data) {
        this.data.put(key, new SerializableLong(data));
    }

    public void putInt(String key, int data) {
        this.data.put(key, new SerializableInt(data));
    }

    public void putShort(String key, short data) {
        this.data.put(key, new SerializableShort(data));
    }

    public void putByte(String key, byte data) {
        this.data.put(key, new SerializableByte(data));
    }

    public void putDouble(String key, double data) {
        this.data.put(key, new SerializableDouble(data));
    }

    public void putFloat(String key, float data) {
        this.data.put(key, new SerializableFloat(data));
    }

    public void putByteArray(String key, byte[] data) {
        this.data.put(key, new SerializableByteArray(data));
    }

    public void putIntArray(String key, int[] data) {
        this.data.put(key, new SerializableIntArray(data));
    }

    /*

     */

    public String getString(String key) {
        requireGoodTypeAndNonNull(key, DataType.STRING);
        return data.get(key).getString();
    }

    public long getLong(String key) {
        requireGoodTypeAndNonNull(key, DataType.LONG);
        return data.get(key).getLong();
    }

    public int getInt(String key) {
        requireGoodTypeAndNonNull(key, DataType.INT);
        return data.get(key).getInt();
    }

    public short getShort(String key) {
        requireGoodTypeAndNonNull(key, DataType.SHORT);
        return data.get(key).getShort();
    }

    public byte getByte(String key) {
        requireGoodTypeAndNonNull(key, DataType.BYTE);
        return data.get(key).getByte();
    }

    public double getDouble(String key) {
        requireGoodTypeAndNonNull(key, DataType.DOUBLE);
        return data.get(key).getDouble();
    }

    public float getFloat(String key) {
        requireGoodTypeAndNonNull(key, DataType.FLOAT);
        return data.get(key).getFloat();
    }

    public byte[] getByteArray(String key) {
        requireGoodTypeAndNonNull(key, DataType.BYTE_ARRAY);
        return data.get(key).getByteArray();
    }

    public int[] getIntArray(String key) {
        requireGoodTypeAndNonNull(key, DataType.INT_ARRAY);
        return data.get(key).getIntArray();
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public Map<String, ISerializableData> getMapCopy() {
        return new HashMap<>(data);
    }

    private void requireGoodTypeAndNonNull(String key, DataType type) {
        if(!data.containsKey(key))
            throw new NullPointerException();

        if(!data.get(key).getType().equals(type))
            throw new UnsupportedOperationException("Provided data type is not the good type");
    }
}
