package dev.qther.convenientcontainers.mixin;

import dev.qther.convenientcontainers.ConvenientContainers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "remove", at = @At("HEAD"))
    private void remove(ServerPlayer player, CallbackInfo ci) {
        for (var item : player.getInventory().items) {
            ConvenientContainers.FROZEN_STACKS.remove(item);
        }
    }
}
