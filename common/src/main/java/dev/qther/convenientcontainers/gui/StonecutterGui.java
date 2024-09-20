package dev.qther.convenientcontainers.gui;

import dev.qther.convenientcontainers.ConvenientContainers;
import dev.qther.convenientcontainers.mixin.StonecutterMenuAccessor;
import it.unimi.dsi.fastutil.objects.Object2ReferenceArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class StonecutterGui extends ServersideGui {
    public StonecutterGui(@NotNull ServerPlayer player) {
        super(player);
    }

    public void show() {
        var stack = player.getMainHandItem();
        assert !stack.isEmpty();

        player.openMenu(new SimpleMenuProvider(Menu::new, Component.translatable("block.minecraft.stonecutter")));
        ConvenientContainers.FROZEN_STACKS.add(stack);
        player.awardStat(Stats.INTERACT_WITH_STONECUTTER);
    }

    public static class Menu extends StonecutterMenu {
        public static final Map<Player, Map<Item, RecipeHolder<StonecutterRecipe>>> LAST_RECIPES = new Reference2ReferenceOpenHashMap<>();

        public Menu(int i, Inventory inventory, Player player) {
            super(i, inventory, new ContainerLevelAccess() {
                @Override
                public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> biFunction) {
                    return Optional.ofNullable(biFunction.apply(player.level(), player.blockPosition()));
                }
            });

            var menu = this;
            var accessor = (StonecutterMenuAccessor) this;
            var inputIdx = accessor.getInputSlot().index;

            this.addSlotListener(new ContainerListener() {
                @Override
                public void slotChanged(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack) {
                    if (i == inputIdx && !itemStack.isEmpty()) {
                        var map = Menu.LAST_RECIPES.get(player);
                        if (map != null) {
                            var recipe = map.get(accessor.getInputSlot().getItem().getItem());
                            if (recipe != null) {
                                var recipes = menu.getRecipes();
                                var idx = recipes.indexOf(recipe);
                                if (idx != -1) {
                                    menu.clickMenuButton(player, idx);
                                }
                            }
                        }
                    }
                }

                @Override
                public void dataChanged(AbstractContainerMenu abstractContainerMenu, int i, int j) {
                }
            });
        }

        @Override
        public boolean clickMenuButton(Player player, int i) {
            var result = super.clickMenuButton(player, i);

            if (((StonecutterMenuAccessor) this).callIsValidRecipeIndex(i)) {
                if (!LAST_RECIPES.containsKey(player)) {
                    LAST_RECIPES.put(player, new Object2ReferenceArrayMap<>());
                }

                LAST_RECIPES
                        .get(player)
                        .put(
                                ((StonecutterMenuAccessor) this).getInputSlot().getItem().getItem(),
                                this.getRecipes().get(i));
            }

            return result;
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }
}
