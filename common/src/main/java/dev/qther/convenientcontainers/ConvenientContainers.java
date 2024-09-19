package dev.qther.convenientcontainers;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class ConvenientContainers {
    public static final String MOD_ID = "convenientcontainers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final List<ItemStack> FROZEN_STACKS = new ObjectArrayList<>();

    public static void init() {
        LOGGER.info("Convenient Containers is starting.");
    }
}
