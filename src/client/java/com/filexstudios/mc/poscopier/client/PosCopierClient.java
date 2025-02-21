package com.filexstudios.mc.poscopier.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class PosCopierClient implements ClientModInitializer {

    public KeyBinding myKeybind;
    private String dimension = "error";
    private String template = "%x/%y/%z/%d";

    @Override
    public void onInitializeClient() {
        ModConfig.init();
        template = ModConfig.get().template;
        myKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "poscopier.copy_position_key",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "Pos Copier"
        ));
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
        template = ModConfig.get().template;
        String message = template
                .replace("%x", String.valueOf(posX))
                .replace("%y", String.valueOf(posY))
                .replace("%z", String.valueOf(posZ))
                .replace("%d", String.valueOf(dimension));
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
