package dev.qther.convenientcontainers.fabric;

import dev.qther.convenientcontainers.ConvenientContainers;
import net.fabricmc.api.ModInitializer;

public final class ConvenientContainersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ConvenientContainers.init();
    }
}
