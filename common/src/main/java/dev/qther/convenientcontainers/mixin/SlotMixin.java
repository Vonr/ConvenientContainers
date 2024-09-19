package dev.qther.convenientcontainers.mixin;

import dev.qther.convenientcontainers.ConvenientContainers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void mayPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (ConvenientContainers.FROZEN_STACKS.contains(this.getItem())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void mayPlace(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (ConvenientContainers.FROZEN_STACKS.contains(itemStack)) {
            cir.setReturnValue(false);
        }
    }
}
