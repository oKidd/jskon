package me.felipe.jskon.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JsonFlattener {
    private JsonFlattener() {
    }

    public static JsonElement parse(String input) {
        return JsonParser.parseString(input);
    }

    public static List<Entry> flatten(JsonElement element) {
        List<Entry> entries = new ArrayList<>();
        flattenInto(entries, "", element);
        return entries;
    }

    private static void flattenInto(List<Entry> entries, String path, JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return;
        }

        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                String childPath = append(path, entry.getKey());
                flattenInto(entries, childPath, entry.getValue());
            }
            return;
        }

        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            int index = 1;
            for (JsonElement child : array) {
                String childPath = append(path, String.valueOf(index++));
                if (child != null && (child.isJsonObject() || child.isJsonArray())) {
                    entries.add(new Entry(childPath, child.toString()));
                }
                flattenInto(entries, childPath, child);
            }
            return;
        }

        if (element.isJsonPrimitive()) {
            entries.add(new Entry(path, primitiveValue(element)));
        }
    }

    private static String append(String path, String segment) {
        if (path == null || path.isBlank()) {
            return segment;
        }
        return path + "::" + segment;
    }

    private static Object primitiveValue(JsonElement element) {
        if (element.isJsonPrimitive()) {
            if (element.getAsJsonPrimitive().isBoolean()) {
                return element.getAsBoolean();
            }
            if (element.getAsJsonPrimitive().isNumber()) {
                String raw = element.getAsJsonPrimitive().getAsNumber().toString();
                return raw.contains(".") || raw.contains("e") || raw.contains("E")
                        ? element.getAsDouble()
                        : element.getAsLong();
            }
            return element.getAsString();
        }
        return element.toString();
    }

    public record Entry(String path, Object value) {
    }
}
