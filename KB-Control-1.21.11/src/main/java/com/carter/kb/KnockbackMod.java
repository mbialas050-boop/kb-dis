package com.carter.kb;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KnockbackMod implements ModInitializer {
    public static final String MOD_ID = "kb_control";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        KnockbackConfig.load();
        CommandRegistrationCallback.EVENT.register(KnockbackCommands::register);
        LOGGER.info("KB Control initialized. Configuration management is ready.");
    }
}
