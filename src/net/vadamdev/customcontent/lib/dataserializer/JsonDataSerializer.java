package net.vadamdev.customcontent.lib.dataserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.serialization.DataType;
import net.vadamdev.customcontent.lib.serialization.SerializableDataCompound;
import net.vadamdev.customcontent.lib.serialization.data.ISerializableData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer IDataSerializer} implementation that save data to a JSON file
 *
 * @author VadamDev
 * @since 08/12/2023
 */
public class JsonDataSerializer extends AbstractDataSerializer {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File file;

    private final JSONObject jsonObject;

    public JsonDataSerializer(File file, long delay) {
        super(delay);

        this.file = file;
        this.jsonObject = parseFile(file);
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
            final BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
            writter.write(GSON.toJson(jsonObject));
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject parseFile(File file) {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            final StringBuilder jsonString = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null)
                jsonString.append(line);

            reader.close();

            if(jsonString.toString().isEmpty())
                return new JSONObject();

            return (JSONObject) new JSONParser().parse(jsonString.toString());
        }catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
}
