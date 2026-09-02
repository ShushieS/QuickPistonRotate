package com.example.quickpiston.client; // Note: géré via les écouteurs globaux

import com.example.quickpiston.client.Keybindings;
import com.example.quickpiston.client.PistonConfig;
import com.example.quickpiston.client.PistonConfigScreen;
import com.example.quickpiston.network.RotatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod("quickpiston")
public class QuickPistonRotate {

    public QuickPistonRotate(IEventBus modEventBus) {
        modEventBus.addListener(this::registerNetwork);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    private void registerNetwork(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1")
                .optional();

        registrar.playToServer(
                RotatePacket.TYPE,
                RotatePacket.STREAM_CODEC,
                RotatePacket::handle
        );
    }

    private void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Ouvre le menu GUI si on appuie sur M
        while (Keybindings.CONFIG_KEY.consumeClick()) {
            mc.setScreen(new PistonConfigScreen());
        }

        // Effectue la rotation si on appuie sur R
        while (Keybindings.ROTATE_KEY.consumeClick()) {
            HitResult hit = mc.hitResult;
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                // Envoie la position du bloc et la direction configurée dans le GUI au serveur
                PacketDistributor.sendToServer(new RotatePacket(blockHit.getBlockPos(), PistonConfig.getSelectedDirection()));
            }
        }
    }
}