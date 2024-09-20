package dev.qther.convenientcontainers.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilMenu.class)
public interface AnvilMenuAccessor {
    @Accessor("repairItemCountCost")
    int getRepairItemCountCost();

    @Accessor("cost")
    DataSlot getCost();
}
