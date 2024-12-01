package dev.qther.convenientcontainers.mixin;

import dev.qther.convenientcontainers.gui.EnderChestGui;
import dev.qther.convenientcontainers.gui.ShulkerBoxGui;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "doCloseContainer", at = @At("HEAD"))
    private void doCloseContainer(CallbackInfo ci) {
        var player = (ServerPlayer) (Object) this;

        switch (player.containerMenu) {
            case EnderChestGui.Menu ignored -> player.level().playSound(null, player, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 1f, 1f);
            case ShulkerBoxGui.Menu ignored -> player.level().playSound(null, player, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 1f, 1f);
            default -> {}
        }
    }
}
