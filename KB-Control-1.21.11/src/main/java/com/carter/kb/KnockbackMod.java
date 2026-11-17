package com.carter.kb;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KnockbackMod implements ModInitializer {
    public static final String MOD_ID = "kb_control";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        KnockbackConfig.load();
        CommandRegistrationCallback.EVENT.register(KnockbackCommands::register);

        ServerLivingEntityEvents.AFTER_DAMAGE.register((victim, source, baseDamage, damage, blocked) -> {
            if (!KnockbackConfig.enabled) return;

            Entity attacker = source.getEntity();
            if (attacker == null || attacker == victim) return;

            // kierunek od trafionego do atakującego (tylko poziomo)
            Vec3 dir = attacker.position().subtract(victim.position());
            dir = new Vec3(dir.x, 0, dir.z);
            if (dir.lengthSqr() < 1.0E-6) return;
            dir = dir.normalize();

            // dystans w blokach = horizontalMultiplier
            double distance = KnockbackConfig.horizontalMultiplier;

            // przesuwamy krokami, żeby nie wejść w ścianę
            double moved = 0;
            double step = 0.25;
            while (moved + step <= distance) {
                Vec3 delta = dir.scale(moved + step);
                if (!victim.level().noCollision(victim, victim.getBoundingBox().move(delta))) break;
                moved += step;
            }
            if (moved <= 0) return;

            Vec3 target = victim.position().add(dir.scale(moved));
            if (victim instanceof ServerPlayer player) {
                player.connection.teleport(target.x, target.y, target.z, player.getYRot(), player.getXRot());
            } else {
                victim.setPos(target.x, target.y, target.z);
            }
        });

        LOGGER.info("KB Control initialized. Configuration management is ready.");
    }
}
