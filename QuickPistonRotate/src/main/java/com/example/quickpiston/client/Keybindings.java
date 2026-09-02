package com.example.quickpiston.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = "quickpiston", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class Keybindings {
    
    // Touche pour pivoter le piston (R)
    public static final KeyMapping ROTATE_KEY = new KeyMapping(
            "key.quickpiston.rotate",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.quickpiston"
    );

    // Touche pour ouvrir le menu GUI (M)
    public static final KeyMapping CONFIG_KEY = new KeyMapping(
            "key.quickpiston.config",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "key.categories.quickpiston"
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(ROTATE_KEY);
        event.register(CONFIG_KEY);
    }
}