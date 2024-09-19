package dev.qther.convenientcontainers.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.qther.convenientcontainers.ConvenientContainers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @ModifyExpressionValue(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;mayPlace(Lnet/minecraft/world/item/ItemStack;)Z", ordinal = 3))
    private boolean canSwap1(boolean original, @Local(ordinal = 1) ItemStack from, @Local(ordinal = 0) ItemStack to) {
        return original && ConvenientContainers$canSwap(from, to);
    }

    @ModifyExpressionValue(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;mayPickup(Lnet/minecraft/world/entity/player/Player;)Z", ordinal = 2))
    private boolean canSwap2(boolean original, @Local(ordinal = 1) ItemStack from, @Local(ordinal = 0) ItemStack to) {
        return original && ConvenientContainers$canSwap(from, to);
    }

    @ModifyExpressionValue(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;mayPlace(Lnet/minecraft/world/item/ItemStack;)Z", ordinal = 4))
    private boolean canSwap3(boolean original, @Local(ordinal = 1) ItemStack from, @Local(ordinal = 0) ItemStack to) {
        return original && ConvenientContainers$canSwap(from, to);
    }

    @ModifyExpressionValue(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;mayPickup(Lnet/minecraft/world/entity/player/Player;)Z", ordinal = 3))
    private boolean canSwap4(boolean original, @Local(ordinal = 1) ItemStack from, @Local(ordinal = 0) ItemStack to) {
        return original && ConvenientContainers$canSwap(from, to);
    }

    @Unique
    private boolean ConvenientContainers$canSwap(ItemStack from, ItemStack to) {
        return !ConvenientContainers.FROZEN_STACKS.contains(from) && !ConvenientContainers.FROZEN_STACKS.contains(to);
    }
}
