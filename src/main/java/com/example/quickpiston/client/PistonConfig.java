package com.example.quickpiston.client;

import net.minecraft.core.Direction;

public class PistonConfig {
    // Direction sélectionnée par défaut (ex: UP)
    private static Direction selectedDirection = Direction.UP;

    public static Direction getSelectedDirection() {
        return selectedDirection;
    }

    public static void setSelectedDirection(Direction direction) {
        selectedDirection = direction;
    }
}