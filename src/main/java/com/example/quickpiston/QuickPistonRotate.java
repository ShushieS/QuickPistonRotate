package com.example.quickpiston;

import com.example.quickpiston.client.CreateRotation;
import com.example.quickpiston.client.Keybindings;
import com.example.quickpiston.client.PistonConfig;
import com.example.quickpiston.client.PistonConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

// Mod 100% cote client : aucune installation serveur requise.
// La rotation passe par le paquet natif de Create (voir CreateRotation).
@Mod(value = "quickpiston", dist = Dist.CLIENT)
public class QuickPistonRotate {

    public QuickPistonRotate() {
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    private void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        // Ouvre le menu GUI si on appuie sur M
        while (Keybindings.CONFIG_KEY.consumeClick()) {
            mc.setScreen(new PistonConfigScreen());
        }

        // Effectue la rotation si on appuie sur R
        while (Keybindings.ROTATE_KEY.consumeClick()) {
            HitResult hit = mc.hitResult;
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hit).getBlockPos();
                // Envoie la direction configuree dans le GUI via le paquet de Create
                CreateRotation.rotate(mc.level, pos, PistonConfig.getSelectedDirection());
            }
        }
    }
}
