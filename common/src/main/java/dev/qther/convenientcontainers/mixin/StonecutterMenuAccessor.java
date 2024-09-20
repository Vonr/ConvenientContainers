package dev.qther.convenientcontainers.mixin;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StonecutterMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StonecutterMenu.class)
public interface StonecutterMenuAccessor {
    @Accessor("inputSlot")
    Slot getInputSlot();

    @Invoker("isValidRecipeIndex")
    boolean callIsValidRecipeIndex(int index);
}
