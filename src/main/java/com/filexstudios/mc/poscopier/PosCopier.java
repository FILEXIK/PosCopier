package com.filexstudios.mc.poscopier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PosCopier implements ModInitializer {
    public static KeyBinding myKeybind;
    @Override
    public void onInitialize() {
        myKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "poscopier.copy_position_key",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "Pos Copier"
        ));
    }
}
