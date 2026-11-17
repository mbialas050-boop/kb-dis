package com.carter.kb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class KnockbackConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE =
            FabricLoader.getInstance().getConfigDir().resolve("knockback.json");

    public static boolean enabled = true;
    public static double horizontalMultiplier = 1.0;
    public static double verticalMultiplier = 1.0;

    private KnockbackConfig() {}

    public static synchronized void load() {
        try {
            if (Files.notExists(FILE)) {
                save();
                return;
            }

            ConfigData data = GSON.fromJson(Files.readString(FILE), ConfigData.class);
            if (data == null) {
                throw new IllegalStateException("Config file was empty or invalid.");
            }

            enabled = data.enabled;
            horizontalMultiplier = clamp(data.horizontalMultiplier, 0.1, 2.0);
            verticalMultiplier = clamp(data.verticalMultiplier, 0.1, 2.0);
            save(); // Rewrite normalized values and fill any missing fields.
        } catch (Exception exception) {
            KnockbackMod.LOGGER.error("Could not load knockback.json; defaults remain active.", exception);
        }
    }

    public static synchronized void save() {
        try {
            Files.createDirectories(FILE.getParent());
            ConfigData data = new ConfigData();
            data.enabled = enabled;
            data.horizontalMultiplier = horizontalMultiplier;
            data.verticalMultiplier = verticalMultiplier;
            Files.writeString(FILE, GSON.toJson(data));
        } catch (IOException exception) {
            KnockbackMod.LOGGER.error("Could not save knockback.json.", exception);
        }
    }

    private static double clamp(double value, double min, double max) {
        if (!Double.isFinite(value)) return 1.0;
        return Math.max(min, Math.min(max, value));
    }

    private static final class ConfigData {
        boolean enabled = true;
        double horizontalMultiplier = 1.0;
        double verticalMultiplier = 1.0;
    }
}
