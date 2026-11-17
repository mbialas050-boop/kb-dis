package com.carter.kb;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public final class KnockbackCommands {
    private KnockbackCommands() {}

    public static void register(
            com.mojang.brigadier.CommandDispatcher<CommandSourceStack> dispatcher,
            net.minecraft.commands.CommandBuildContext registryAccess,
            Commands.CommandSelection environment) {
            dispatcher.register(Commands.literal("kb")
        .then(Commands.literal("status")
            .then(Commands.literal("status")
                .executes(context -> {
                    context.getSource().sendSuccess(() -> Component.literal(
                        "KB Control config: enabled=" + KnockbackConfig.enabled
                        + ", horizontal=" + KnockbackConfig.horizontalMultiplier
                        + ", vertical=" + KnockbackConfig.verticalMultiplier
                    ), false);
                    return 1;
                }))
            .then(Commands.literal("reload")
                .executes(context -> {
                    KnockbackConfig.load();
                    context.getSource().sendSuccess(
                        () -> Component.literal("KB Control configuration reloaded."),
                        true
                    );
                    return 1;
                }))
            .then(Commands.literal("set")
                .then(Commands.literal("enabled")
                    .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(context -> {
                            KnockbackConfig.enabled =
                                BoolArgumentType.getBool(context, "value");
                            KnockbackConfig.save();
                            context.getSource().sendSuccess(
                                () -> Component.literal("enabled=" + KnockbackConfig.enabled),
                                true
                            );
                            return 1;
                        })))
                .then(Commands.literal("horizontal")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.1, 2.0))
                        .executes(context -> {
                            KnockbackConfig.horizontalMultiplier =
                                DoubleArgumentType.getDouble(context, "value");
                            KnockbackConfig.save();
                            context.getSource().sendSuccess(
                                () -> Component.literal(
                                    "horizontalMultiplier=" + KnockbackConfig.horizontalMultiplier
                                ),
                                true
                            );
                            return 1;
                        })))
                .then(Commands.literal("vertical")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.1, 2.0))
                        .executes(context -> {
                            KnockbackConfig.verticalMultiplier =
                                DoubleArgumentType.getDouble(context, "value");
                            KnockbackConfig.save();
                            context.getSource().sendSuccess(
                                () -> Component.literal(
                                    "verticalMultiplier=" + KnockbackConfig.verticalMultiplier
                                ),
                                true
                            );
                            return 1;
                        })))));
    }
}
