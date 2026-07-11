package me.felipe.jskon.skript.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.KeyProviderExpression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.google.gson.JsonElement;
import me.felipe.jskon.json.JsonFlattener;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class ExprJsonFrom extends SimpleExpression<Object> implements KeyProviderExpression<Object> {
    private Expression<String> jsonExpression;
    private final Map<Event, List<JsonFlattener.Entry>> cache = Collections.synchronizedMap(new WeakHashMap<>());

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.jsonExpression = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    protected Object[] get(Event event) {
        List<JsonFlattener.Entry> entries = resolveEntries(event);
        List<Object> values = new ArrayList<>(entries.size());
        for (JsonFlattener.Entry entry : entries) {
            values.add(entry.value());
        }
        return values.toArray();
    }

    @Override
    public String[] getArrayKeys(Event event) {
        List<JsonFlattener.Entry> entries = resolveEntries(event);
        String[] keys = new String[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            keys[i] = entries.get(i).path();
        }
        return keys;
    }

    @Override
    public boolean canReturnKeys() {
        return true;
    }

    @Override
    public boolean canIterateWithKeys() {
        return true;
    }

    @Override
    public boolean areKeysRecommended() {
        return true;
    }

    @Override
    public Class<?>[] acceptChange(ch.njol.skript.classes.Changer.ChangeMode mode) {
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, ch.njol.skript.classes.Changer.ChangeMode mode) {
        throw new UnsupportedOperationException("json from is read-only");
    }

    @Override
    public Class<? extends Object> getReturnType() {
        return Object.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "json from " + jsonExpression.toString(event, debug);
    }

    @Override
    public String getSyntaxTypeName() {
        return "json from";
    }

    private List<JsonFlattener.Entry> resolveEntries(Event event) {
        List<JsonFlattener.Entry> cached = cache.get(event);
        if (cached != null) {
            return cached;
        }

        String json = jsonExpression.getSingle(event);
        if (json == null || json.isBlank()) {
            List<JsonFlattener.Entry> empty = List.of();
            cache.put(event, empty);
            return empty;
        }

        try {
            JsonElement element = JsonFlattener.parse(json);
            List<JsonFlattener.Entry> entries = List.copyOf(JsonFlattener.flatten(element));
            cache.put(event, entries);
            return entries;
        } catch (Exception ex) {
            List<JsonFlattener.Entry> empty = List.of();
            cache.put(event, empty);
            return empty;
        }
    }
}
