package com.filexstudios.mc.poscopier.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

import static com.filexstudios.mc.poscopier.PosCopier.myKeybind;

public class PosCopierClient implements ClientModInitializer {
    private String dimension = "error";
    @Override
    public void onInitializeClient() {
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity == MinecraftClient.getInstance().player) {
                detectDimension(entity);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (myKeybind.wasPressed()) {
                if (client.player != null) {
                    copyPos();
                }
            }
        });
    }

    private void copyPos(){
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        int posX = (int) Math.round(client.player.getX());
        int posY = (int) Math.round(client.player.getY());
        int posZ = (int) Math.round(client.player.getZ());
        String message = String.format("%s/%s/%s/%s", posX, posY, posZ, dimension);
        MinecraftClient.getInstance().keyboard.setClipboard(message);
        MinecraftClient.getInstance().player.sendMessage(Text.translatable("poscopier.position_copied").formatted(Formatting.AQUA), false);
    }


    private void detectDimension(Entity entity) {
        if (entity.getWorld().getRegistryKey().equals(World.NETHER)) {
            dimension = "Nether";
        } else if (entity.getWorld().getRegistryKey().equals(World.END)) {
            dimension = "End";
        } else if (entity.getWorld().getRegistryKey().equals(World.OVERWORLD)) {
            dimension = "Overworld";
        } else {
            dimension = String.valueOf(entity.getWorld().getRegistryKey().getValue());
        }
    }
}
