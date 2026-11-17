package com.carter.kb.mixin;

import com.carter.kb.KnockbackConfig;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    private void kb$cancelVanillaKnockback(double strength, double x, double z, CallbackInfo ci) {
        if (KnockbackConfig.enabled) {
            ci.cancel();
        }
    }
}
