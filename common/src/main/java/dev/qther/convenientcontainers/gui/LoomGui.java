package dev.qther.convenientcontainers.gui;

import dev.qther.convenientcontainers.ConvenientContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiFunction;

public class LoomGui extends ServersideGui {
    public LoomGui(@NotNull ServerPlayer player) {
        super(player);
    }

    public void show() {
        var stack = player.getMainHandItem();
        assert !stack.isEmpty();

        player.openMenu(new SimpleMenuProvider(Menu::new, Component.translatable("block.minecraft.loom")));
        ConvenientContainers.FROZEN_STACKS.add(stack);
        player.awardStat(Stats.INTERACT_WITH_LOOM);
    }

    public static class Menu extends LoomMenu {
        public Menu(int i, Inventory inventory, Player player) {
            super(i, inventory, new ContainerLevelAccess() {
                @Override
                public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> biFunction) {
                    return Optional.ofNullable(biFunction.apply(player.level(), player.blockPosition()));
                }
            });
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }
}
