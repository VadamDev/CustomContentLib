package net.vadamdev.customcontent.lib.serialization;

import net.vadamdev.customcontent.lib.serialization.data.*;

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

    /*
       Put
     */

    public void put(String key, ISerializableData data) {
        this.data.put(key, data);
    }

    public void putString(String key, String data) {
        put(key, new SerializableString(data));
    }

    public void putBoolean(String key, boolean data) {
        put(key, new SerializableBoolean(data));
    }

    public void putLong(String key, long data) {
        put(key, new SerializableLong(data));
    }

    public void putInt(String key, int data) {
        put(key, new SerializableInt(data));
    }

    public void putShort(String key, short data) {
        put(key, new SerializableShort(data));
    }

    public void putByte(String key, byte data) {
        put(key, new SerializableByte(data));
    }

    public void putDouble(String key, double data) {
        put(key, new SerializableDouble(data));
    }

    public void putFloat(String key, float data) {
        put(key, new SerializableFloat(data));
    }

    public void putByteArray(String key, byte[] data) {
        put(key, new SerializableByteArray(data));
    }

    public void putIntArray(String key, int[] data) {
        put(key, new SerializableIntArray(data));
    }

    /*
       Get
     */

    public ISerializableData get(String key) {
        if(!data.containsKey(key))
            return null;

        return data.get(key);
    }

    public String getString(String key) {
        if(!data.containsKey(key))
            return null;

        requireGoodType(key, DataType.STRING);

        return data.get(key).getAsString();
    }

    public boolean getBoolean(String key) {
        if(!data.containsKey(key))
            return false;

        requireGoodType(key, DataType.BOOLEAN);

        return data.get(key).getAsBoolean();
    }

    public long getLong(String key) {
        if(!data.containsKey(key))
            return 0;

        requireGoodType(key, DataType.LONG);

        return data.get(key).getAsLong();
    }

    public int getInt(String key) {
        if(!data.containsKey(key))
            return 0;

        requireGoodType(key, DataType.INT);

        return data.get(key).getAsInt();
    }

    public short getShort(String key) {
        if(!data.containsKey(key))
            return 0;

        requireGoodType(key, DataType.SHORT);

        return data.get(key).getAsShort();
    }

    public byte getByte(String key) {
        if(!data.containsKey(key))
            return 0;

        requireGoodType(key, DataType.BYTE);

        return data.get(key).getAsByte();
    }

    public double getDouble(String key) {
        if(!data.containsKey(key))
            return 0;

        requireGoodType(key, DataType.DOUBLE);

        return data.get(key).getAsDouble();
    }

    public float getFloat(String key) {
        if(!data.containsKey(key))
            return 0;

        requireGoodType(key, DataType.FLOAT);

        return data.get(key).getAsFloat();
    }

    public byte[] getByteArray(String key) {
        if(!data.containsKey(key))
            return new byte[0];

        requireGoodType(key, DataType.BYTE_ARRAY);

        return data.get(key).getAsByteArray();
    }

    public int[] getIntArray(String key) {
        if(!data.containsKey(key))
            return new int[0];

        requireGoodType(key, DataType.INT_ARRAY);

        return data.get(key).getAsIntArray();
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public Map<String, ISerializableData> getMapCopy() {
        return new HashMap<>(data);
    }

    private void requireGoodType(String key, DataType type) {
        if(data.get(key).getType().equals(type))
            return;

        throw new UnsupportedOperationException("Provided data type is not the good type");
    }
}
