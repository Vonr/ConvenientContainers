package dev.qther.convenientcontainers.gui;

import dev.qther.convenientcontainers.mixin.ItemContainerContentsAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.jetbrains.annotations.NotNull;

public class ShulkerBoxGui extends ServersideGui {
    public ShulkerBoxGui(@NotNull ServerPlayer player) {
        super(player);
    }

    public void show() {
        var stack = player.getMainHandItem();
        assert !stack.isEmpty();

        var contents = stack.get(DataComponents.CONTAINER);
        assert contents != null;

        var items = ((ItemContainerContentsAccessor) (Object) contents).getItems();

        var newContainer = new SimpleContainer(27);
        for (int index = 0; index < 27; index++) {
            newContainer.setItem(index, index >= items.size() ? ItemStack.EMPTY : items.get(index));
        }

        player.openMenu(new SimpleMenuProvider((i, inv, container) -> new Menu(i, inv, newContainer, stack), stack.getHoverName()));

        player.level().playSound(null, player, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 1f, 1f);

        player.awardStat(Stats.OPEN_SHULKER_BOX);
        PiglinAi.angerNearbyPiglins(player.serverLevel(), player, true);
    }

    public static class Menu extends ShulkerBoxMenu {
        ItemStack stack;

        public Menu(int i, Inventory inventory, SimpleContainer container, ItemStack stack) {
            super(i, inventory, container);
            this.stack = stack;

            this.addSlotListener(new ContainerListener() {
                @Override
                public void slotChanged(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack) {
                    var newContents = ItemContainerContents.fromItems(container.getItems());
                    stack.set(DataComponents.CONTAINER, newContents);
                }

                @Override
                public void dataChanged(AbstractContainerMenu abstractContainerMenu, int i, int j) {
                }
            });
        }

        @Override
        public boolean stillValid(Player player) {
            var handStack = player.getMainHandItem();
            return Block.byItem(handStack.getItem()) instanceof ShulkerBoxBlock && ItemStack.isSameItemSameComponents(handStack, stack);
        }
    }
}
