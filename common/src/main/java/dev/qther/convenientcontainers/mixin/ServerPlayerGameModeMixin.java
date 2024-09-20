package dev.qther.convenientcontainers.mixin;

import dev.qther.convenientcontainers.gui.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
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

        var gui = switch (Block.byItem(stack.getItem())) {
            case AnvilBlock ignored -> new AnvilGui(player);
            case SmithingTableBlock ignored -> new SmithingTableGui(player);
            case CartographyTableBlock ignored -> new CartographyTableGui(player);
            case CraftingTableBlock ignored -> new CraftingTableGui(player);
            case EnderChestBlock ignored -> new EnderChestGui(player);
            case GrindstoneBlock ignored -> new GrindstoneGui(player);
            case LoomBlock ignored -> new LoomGui(player);
            case ShulkerBoxBlock ignored -> new ShulkerBoxGui(player);
            case StonecutterBlock ignored -> new StonecutterGui(player);
            default -> null;
        };

        if (gui != null) {
            gui.show();
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
