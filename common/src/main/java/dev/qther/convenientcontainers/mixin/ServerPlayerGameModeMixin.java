package dev.qther.convenientcontainers.mixin;

import dev.qther.convenientcontainers.gui.CraftingTableGui;
import dev.qther.convenientcontainers.gui.EnderChestGui;
import dev.qther.convenientcontainers.gui.ShulkerGui;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    private void useItem(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }

        var item = stack.getItem();

        var block = Block.byItem(item);

        switch (block) {
            case ShulkerBoxBlock ignored -> {
                new ShulkerGui(player).show();
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
            case EnderChestBlock ignored -> {
                new EnderChestGui(player).show();
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
            case CraftingTableBlock ignored -> {
                new CraftingTableGui(player).show();
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
            default -> {
            }
        }

    }
}
