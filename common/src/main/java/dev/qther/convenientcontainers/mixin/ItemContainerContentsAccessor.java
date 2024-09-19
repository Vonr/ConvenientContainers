package dev.qther.convenientcontainers.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemContainerContents.class)
public interface ItemContainerContentsAccessor {
    @Accessor("items")
    NonNullList<ItemStack> getItems();
}
