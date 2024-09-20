package dev.qther.convenientcontainers.gui;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class ServersideGui {
    final ServerPlayer player;

    public ServersideGui(@NotNull ServerPlayer player) {
        this.player = player;
    }

    public void show() {
    }
}
