package eu.matejkormuth.starving;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluginAccessor {
    private final Module mod;

    public PluginAccessor(Module mod) {
        this.mod = mod;
    }

    public JavaPlugin getPlugin() {
        return (JavaPlugin) mod.plugin;
    }
}