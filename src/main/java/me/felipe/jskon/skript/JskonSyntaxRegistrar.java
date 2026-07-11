package me.felipe.jskon.skript;

import ch.njol.skript.Skript;
import me.felipe.jskon.skript.expressions.ExprJsonFrom;
import org.bukkit.plugin.java.JavaPlugin;

public final class JskonSyntaxRegistrar {
    private JskonSyntaxRegistrar() {
    }

    public static void register(JavaPlugin plugin) {
        Skript.registerExpression(ExprJsonFrom.class, Object.class, ch.njol.skript.lang.ExpressionType.SIMPLE,
                "json from %string%");
    }
}
