package com.example.quickpiston.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class PistonConfigScreen extends Screen {

    public PistonConfigScreen() {
        super(Component.literal("Configuration Piston Rotate"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Bouton de sélection cyclique corrigé avec .withValues() et .withInitialValue()
        this.addRenderableWidget(
            CycleButton.<Direction>builder(direction -> Component.literal(direction.getName().toUpperCase()))
                .withValues(Direction.values())
                .withInitialValue(PistonConfig.getSelectedDirection())
                .create(centerX - 100, centerY - 20, 200, 20, Component.literal("Direction cible : "), (button, value) -> {
                    PistonConfig.setSelectedDirection(value);
                })
        );

        // Bouton de fermeture
        this.addRenderableWidget(
            Button.builder(Component.literal("Fermer"), button -> {
                if (this.minecraft != null) {
                    this.minecraft.setScreen(null);
                }
            }).bounds(centerX - 100, centerY + 10, 200, 20).build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 60, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}