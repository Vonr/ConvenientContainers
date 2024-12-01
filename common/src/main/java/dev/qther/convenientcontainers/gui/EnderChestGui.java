package dev.qther.convenientcontainers.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnderChestBlock;
import org.jetbrains.annotations.NotNull;

public class EnderChestGui extends ServersideGui {
    public EnderChestGui(@NotNull ServerPlayer player) {
        super(player);
    }

    public void show() {
        var stack = player.getMainHandItem();
        assert !stack.isEmpty();

        var container = player.getEnderChestInventory();

        // TODO: Find a sane way to prevent putting your ender chest inside your ender chest menu.
        player.openMenu(new SimpleMenuProvider((i, inventory, _player) -> new Menu(MenuType.GENERIC_9x3, i, inventory, container, 3), Component.translatable("block.minecraft.ender_chest")));
        player.level().playSound(null, player, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 1f, 1f);
        player.awardStat(Stats.OPEN_ENDERCHEST);
        PiglinAi.angerNearbyPiglins(player, true);
    }

    public static class Menu extends ChestMenu {
        public Menu(MenuType<?> menuType, int i, Inventory inventory, Container container, int j) {
            super(menuType, i, inventory, container, j);
        }

        @Override
        public boolean stillValid(Player player) {
            return Block.byItem(player.getMainHandItem().getItem()) instanceof EnderChestBlock;
        }
    }
}
