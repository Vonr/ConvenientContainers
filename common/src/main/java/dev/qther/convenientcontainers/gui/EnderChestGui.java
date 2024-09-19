package dev.qther.convenientcontainers.gui;

import dev.qther.convenientcontainers.ConvenientContainers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class EnderChestGui {
    ServerPlayer player;

    public EnderChestGui(@NotNull ServerPlayer player) {
        this.player = player;
    }

    public void show() {
        var stack = player.getMainHandItem();
        assert !stack.isEmpty();

        var container = player.getEnderChestInventory();

        player.openMenu(new SimpleMenuProvider((i, inventory, _player) -> new Menu(MenuType.GENERIC_9x3, i, inventory, container, 3), Component.translatable("block.minecraft.ender_chest")));
        ConvenientContainers.FROZEN_STACKS.add(stack);
        player.awardStat(Stats.OPEN_ENDERCHEST);
        PiglinAi.angerNearbyPiglins(player, true);
    }

    public static class Menu extends ChestMenu {
        public Menu(MenuType<?> menuType, int i, Inventory inventory, Container container, int j) {
            super(menuType, i, inventory, container, j);
        }
    }
}
