package eu.codedsakura.fabricpvp;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.GameRules;

import java.util.Collection;
import java.util.Collections;

import static eu.codedsakura.fabricpvp.PlayerComponentInitializer.PVP_DATA;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class FabricPvP implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, listener) -> {
            dispatcher.register(literal("pvp")
                    .executes(this::togglePvP)
                    .then(literal("on")
                            .executes(ctx -> this.setPvP(ctx, true))
                            .then(argument("players", EntityArgumentType.players()).requires(req -> req.hasPermissionLevel(2))
                                    .executes(ctx -> this.setPvP(ctx, true, EntityArgumentType.getPlayers(ctx, "players")))))
                    .then(literal("off")
                            .executes(ctx -> this.setPvP(ctx, false))
                            .then(argument("players", EntityArgumentType.players()).requires(req -> req.hasPermissionLevel(2))
                                    .executes(ctx -> this.setPvP(ctx, false, EntityArgumentType.getPlayers(ctx, "players"))))));
        });
    }

    private int setPvP(CommandContext<ServerCommandSource> ctx, boolean b) throws CommandSyntaxException {
        return setPvP(ctx, b, Collections.singletonList(ctx.getSource().getPlayer()));
    }
    private int setPvP(CommandContext<ServerCommandSource> ctx, boolean b, Collection<ServerPlayerEntity> players) {
        players.forEach(v -> {
            PVP_DATA.get(v).set(b);
        });
        ctx.getSource().sendFeedback(new TranslatableText(players.size() == 1 ? "You have %s PvP" : "You have %s PvP for all selected players", b ? "enabled" : "disabled"), players.size() > 1);
        return 1;
    }

    private int togglePvP(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerComponentInitializer.IPvPComponent pvpComponent = PVP_DATA.get(context.getSource().getPlayer());
        pvpComponent.set(!pvpComponent.isOn());
        context.getSource().sendFeedback(new TranslatableText("You have %s PvP", pvpComponent.isOn() ? "enabled" : "disabled"), false);
        return 1;
    }
}
