package dev.qther.convenientcontainers.gui;

import dev.qther.convenientcontainers.ConvenientContainers;
import dev.qther.convenientcontainers.mixin.ItemContainerContentsAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
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
import org.jetbrains.annotations.NotNull;

public class ShulkerGui {
    ServerPlayer player;

    public ShulkerGui(@NotNull ServerPlayer player) {
        this.player = player;
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

        player.openMenu(new SimpleMenuProvider((i, player, container) -> new Menu(i, player, newContainer), stack.getHoverName()));
        ConvenientContainers.FROZEN_STACKS.add(stack);

        player.containerMenu.addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack) {
                var newContents = ItemContainerContents.fromItems(newContainer.getItems());
                stack.set(DataComponents.CONTAINER, newContents);
            }

            @Override
            public void dataChanged(AbstractContainerMenu abstractContainerMenu, int i, int j) {
            }
        });

        player.awardStat(Stats.OPEN_SHULKER_BOX);
        PiglinAi.angerNearbyPiglins(player, true);
    }

    public static class Menu extends ShulkerBoxMenu {
        public Menu(int i, Inventory inventory, Container container) {
            super(i, inventory, container);
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }
}
