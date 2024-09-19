package dev.qther.convenientcontainers.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    private void overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (Block.byItem((Item) (Object) this) instanceof ShulkerBoxBlock) {
            if (clickAction != ClickAction.SECONDARY) {
                return;
            }

            var container = itemStack.get(DataComponents.CONTAINER);
            assert container != null;

            var iter = container.nonEmptyItems().iterator();
            ItemStack lookingFor;
            if (slot.hasItem()) {
                lookingFor = slot.getItem();

                if (!slot.mayPlace(lookingFor)) {
                    return;
                }
            } else {
                if (!iter.hasNext()) {
                    return;
                }
                lookingFor = iter.next();

                if (!slot.mayPlace(lookingFor)) {
                    return;
                }

                while (iter.hasNext()) {
                    var s = iter.next();
                    if (!ItemStack.isSameItemSameComponents(lookingFor, s)) {
                        return;
                    }
                }
            }

            var contents = ((ItemContainerContentsAccessor) (Object) container).getItems();

            var newContainer = new SimpleContainer(27);
            for (int i = 0; i < contents.size(); i++) {
                newContainer.setItem(i, contents.get(i));
            }

            var removed = newContainer.removeItemType(lookingFor.getItem(), lookingFor.getMaxStackSize() - slot.getItem().getCount());
            if (slot.hasItem()) {
                slot.getItem().setCount(slot.getItem().getCount() + removed.getCount());
                slot.setChanged();
            } else {
                slot.set(removed);
            }
            itemStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(newContainer.getItems()));

            cir.setReturnValue(true);
        }
    }
}
