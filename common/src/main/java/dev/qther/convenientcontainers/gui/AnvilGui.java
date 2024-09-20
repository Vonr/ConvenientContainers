package dev.qther.convenientcontainers.gui;

import dev.qther.convenientcontainers.ConvenientContainers;
import dev.qther.convenientcontainers.mixin.AnvilMenuAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiFunction;

public class AnvilGui extends ServersideGui {
    public AnvilGui(@NotNull ServerPlayer player) {
        super(player);
    }

    public void show() {
        var stack = player.getMainHandItem();
        assert !stack.isEmpty();

        player.openMenu(new SimpleMenuProvider(Menu::new, stack.getHoverName()));
        ConvenientContainers.FROZEN_STACKS.add(stack);
        player.awardStat(Stats.INTERACT_WITH_ANVIL);
    }

    public static class Menu extends AnvilMenu {
        public Menu(int i, Inventory inventory, Player player) {
            super(i, inventory, new ContainerLevelAccess() {
                @Override
                public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> biFunction) {
                    return Optional.ofNullable(biFunction.apply(player.level(), player.blockPosition()));
                }
            });
        }

        @Override
        protected void onTake(Player player_, ItemStack itemStack) {
            if (!(player_ instanceof ServerPlayer player)) {
                return;
            }

            var accessor = (AnvilMenuAccessor) this;

            if (!player.getAbilities().instabuild) {
                player.giveExperienceLevels(-accessor.getCost().get());
            }

            this.inputSlots.setItem(0, ItemStack.EMPTY);
            var repairItemCountCost = accessor.getRepairItemCountCost();
            if (repairItemCountCost > 0) {
                ItemStack itemStack2 = this.inputSlots.getItem(1);
                if (!itemStack2.isEmpty() && itemStack2.getCount() > repairItemCountCost) {
                    itemStack2.shrink(repairItemCountCost);
                    this.inputSlots.setItem(1, itemStack2);
                } else {
                    this.inputSlots.setItem(1, ItemStack.EMPTY);
                }
            } else {
                this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
            player.level().playSound(null, player, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);

            accessor.getCost().set(0);
            if (!player.hasInfiniteMaterials() && player.getRandom().nextFloat() < 0.12F) {
                var anvil = player.getMainHandItem();
                var item = anvil.getItem();

                player.level().playSound(null, player, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 1f, 1f);

                Item newItem;
                if (item == Items.ANVIL) {
                    newItem = Items.CHIPPED_ANVIL;
                } else if (item == Items.CHIPPED_ANVIL) {
                    newItem = Items.DAMAGED_ANVIL;
                } else if (item == Items.DAMAGED_ANVIL) {
                    newItem = Items.AIR;
                    player.closeContainer();
                } else {
                    ConvenientContainers.LOGGER.warn("{} somehow used an anvil without holding one", player.getName().getString());
                    return;
                }

                if (anvil.getCount() == 1) {
                    ConvenientContainers.FROZEN_STACKS.remove(player.getMainHandItem());
                    player.setItemInHand(InteractionHand.MAIN_HAND, newItem == Items.AIR ? ItemStack.EMPTY : new ItemStack(newItem, 1));
                    ConvenientContainers.FROZEN_STACKS.add(player.getMainHandItem());
                } else {
                    anvil.setCount(anvil.getCount() - 1);
                    if (newItem != Items.AIR) {
                        var newStack = new ItemStack(newItem, 1);
                        if (!player.addItem(newStack)) {
                            player.drop(newStack, false);
                        }
                    }
                }
            }
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }
}
