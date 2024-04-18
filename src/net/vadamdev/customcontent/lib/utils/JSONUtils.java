package net.vadamdev.customcontent.lib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONAware;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author VadamDev
 * @since 15/04/2024
 */
public final class JSONUtils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JSONUtils() {}

    public static <T extends JSONAware> T readFile(File file, Class<T> clazz) throws IOException, ParseException {
        return readFile(new FileInputStream(file), clazz);
    }

    public static <T extends JSONAware> T readFile(InputStream stream, Class<T> clazz) throws IOException, ParseException, ClassCastException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        final StringBuilder jsonString = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null)
            jsonString.append(line);

        reader.close();

        final Object object = new JSONParser().parse(jsonString.toString());
        if(!object.getClass().isAssignableFrom(clazz))
            throw new ClassCastException();

        return (T) object;
    }

    public static void saveJSONAwareToFile(JSONAware jsonAware, File file) throws IOException {
        final BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
        writter.write(GSON.toJson(jsonAware));
        writter.close();
    }

    public static Gson getGSON() {
        return GSON;
    }
}
