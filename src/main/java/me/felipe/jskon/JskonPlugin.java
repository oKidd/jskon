package me.felipe.jskon;

import ch.njol.skript.Skript;
import me.felipe.jskon.skript.JskonSyntaxRegistrar;
import org.bukkit.plugin.java.JavaPlugin;

public final class JskonPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("Skript") == null) {
            getLogger().warning("Skript was not found. Jskon syntax was not registered.");
            return;
        }

        Skript.registerAddon(this);
        JskonSyntaxRegistrar.register(this);
        getLogger().info("Jskon enabled.");
    }
}
