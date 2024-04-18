package net.vadamdev.customcontent.lib.dataserializer;

import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.serialization.DataType;
import net.vadamdev.customcontent.lib.serialization.SerializableDataCompound;
import net.vadamdev.customcontent.lib.serialization.data.ISerializableData;
import net.vadamdev.customcontent.lib.utils.JSONUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer IDataSerializer} implementation that save data to a JSON file
 *
 * @author VadamDev
 * @since 08/12/2023
 */
public class JsonDataSerializer extends AbstractDataSerializer {
    private final File file;

    private final JSONObject jsonObject;

    public JsonDataSerializer(File file, long delay) {
        super(delay);

        this.file = file;

        JSONObject jsonObject = null;
        try {
            jsonObject = JSONUtils.readFile(file, JSONObject.class);
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        this.jsonObject = jsonObject;
    }

    @Override
    public void write(BlockPos blockPos, SerializableDataCompound dataCompound) {
        final JSONObject json = new JSONObject();

        for(Map.Entry<String, ISerializableData> entry : dataCompound.getMapCopy().entrySet()) {
            final ISerializableData data = entry.getValue();

            JSONObject subJson = new JSONObject();
            subJson.put("type", data.getType().name());
            subJson.put("data", data.serialize());

            json.put(entry.getKey(), subJson);
        }

        jsonObject.put(blockPos.toSerializableString(), json);
    }

    @Override
    public void remove(BlockPos blockPos) {
        if(!contains(blockPos))
            return;

        jsonObject.remove(blockPos.toSerializableString());
    }

    @Override
    public SerializableDataCompound read(BlockPos blockPos) {
        final SerializableDataCompound compound = new SerializableDataCompound();
        if(!contains(blockPos))
            return compound;

        final JSONObject json = (JSONObject) jsonObject.get(blockPos.toSerializableString());
        for (Object o : json.entrySet()) {
            final Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>) o;

            final JSONObject subJson = entry.getValue();
            final ISerializableData serializableData = ISerializableData.parseFrom(DataType.valueOf((String) subJson.get("type")), (String) subJson.get("data"));

            if(serializableData != null)
                compound.put(entry.getKey(), serializableData);
        }

        return compound;
    }

    @Override
    public Map<BlockPos, SerializableDataCompound> readAll() {
        final Map<BlockPos, SerializableDataCompound> dataMap = new HashMap<>();

        for (Object o : jsonObject.entrySet()) {
            final Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>) o;

            final SerializableDataCompound compound = new SerializableDataCompound();

            for (Object o1 : entry.getValue().entrySet()) {
                final Map.Entry<String, JSONObject> entry1 = (Map.Entry<String, JSONObject>) o1;

                final JSONObject subJson = entry1.getValue();
                final ISerializableData serializableData = ISerializableData.parseFrom(DataType.valueOf((String) subJson.get("type")), (String) subJson.get("data"));

                if(serializableData != null)
                    compound.put(entry1.getKey(), serializableData);
            }

            dataMap.put(BlockPos.fromSerializableString(entry.getKey()), compound);
        }

        return dataMap;
    }

    @Override
    public boolean contains(BlockPos blockPos) {
        return jsonObject.containsKey(blockPos.toSerializableString());
    }

    @Override
    protected void save() {
        try {
            JSONUtils.saveJSONAwareToFile(jsonObject, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
