package me.felipe.jskon.skript.effects;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.skript.variables.Variables;
import com.google.gson.JsonElement;
import me.felipe.jskon.json.JsonFlattener;
import org.bukkit.event.Event;

public final class EffCopyJsonToVariables extends ch.njol.skript.lang.Effect {
    private Expression<String> jsonExpression;
    private Expression<?> targetExpression;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.jsonExpression = (Expression<String>) expressions[0];
        this.targetExpression = expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        String json = jsonExpression.getSingle(event);
        if (json == null || json.isBlank()) {
            return;
        }

        JsonElement element;
        try {
            element = JsonFlattener.parse(json);
        } catch (Exception ex) {
            return;
        }

        String target = resolveTargetPath(event);
        if (target == null || target.isBlank()) {
            return;
        }

        for (JsonFlattener.Entry entry : JsonFlattener.flatten(element)) {
            String fullPath = entry.path().isBlank() ? target : target + "::" + entry.path();
            Variables.setVariable(fullPath, entry.value(), event, isLocal(target));
        }
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "copy json " + jsonExpression.toString(event, debug) + " to " + targetExpression.toString(event, debug);
    }

    @Override
    public String getSyntaxTypeName() {
        return "copy json to variables";
    }

    private String resolveTargetPath(Event event) {
        String raw = targetExpression.toString(event, false);
        if (raw == null) {
            return null;
        }

        String trimmed = raw.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        if (trimmed.endsWith("::*")) {
            trimmed = trimmed.substring(0, trimmed.length() - 3);
        }
        return trimmed;
    }

    private boolean isLocal(String path) {
        String trimmed = path.trim();
        String root = trimmed.contains("::") ? trimmed.substring(0, trimmed.indexOf("::")) : trimmed;
        return root.startsWith("_");
    }
}
