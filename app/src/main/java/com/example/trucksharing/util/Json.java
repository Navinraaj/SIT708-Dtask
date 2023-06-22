package com.example.trucksharing.util;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * A utility class for handling JSON data stored locally.
 */
public class JsonUtils {

    /**
     * Reads a local file and generates a {@link JSONArray} with its content.
     *
     * @param context  the context where the operation is being performed.
     * @param fileName the path that leads to the targeted file.
     * @return a {@link JSONArray} containing the data from the file.
     */
    public static JSONArray loadFromFile(Context context, String fileName) {
        try {
            final InputStream fileStream = context.getAssets().open(fileName);
            return generateFromInputStream(fileStream);

        } catch (Exception e) {
            return new JSONArray();
        }
    }

    /**
     * Accesses a resource and creates a {@link JSONArray} with its content.
     *
     * @param context  the context where the operation is being performed.
     * @param resource the ID of the target resource located in the resource
     *                 folders.
     * @return a {@link JSONArray} containing the data from the resource.
     */
    public static JSONArray loadFromResources(Context context, int resource) {
        try {
            final InputStream resourceStream = context.getResources().openRawResource(resource);
            return generateFromInputStream(resourceStream);

        } catch (Exception e) {
            return new JSONArray();
        }
    }

    /**
     * Converts the content of an {@link InputStream} that contains
     * JSON data into a {@link JSONArray}.
     *
     * @param inputStream the stream containing JSON data.
     * @return a {@link JSONArray} with the data from the stream.
     * @throws JSONException if there are issues parsing the content.
     */
    private static JSONArray generateFromInputStream(InputStream inputStream) throws JSONException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final String collectedInput = bufferedReader.lines().collect(Collectors.joining());
        return new JSONArray(collectedInput);
    }
}
