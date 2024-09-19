package dev.qther.convenientcontainers.mixin;

import dev.qther.convenientcontainers.ConvenientContainers;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "doCloseContainer", at = @At("RETURN"))
    private void doCloseContainer(CallbackInfo ci) {
        ConvenientContainers.FROZEN_STACKS.remove(((ServerPlayer) (Object) this).getMainHandItem());
    }
}
