package com.filexstudios.mc.poscopier.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Config(name = "poscopier")
public class ModConfig implements ConfigData {
    public String template = "%x/%y/%z/%d";

    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Ustawienia modyfikacji"));

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("Ogólne"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startStrField(Text.literal("Tekst"), get().template)
                .setDefaultValue("%x/%y/%z/%d") // Domyślna wartość
                .setTooltip(Text.literal("Wprowadź swój tekst tutaj"))
                .setSaveConsumer(newValue -> get().template = newValue) // Zapisanie wartości
                .build());

        builder.setSavingRunnable(() -> {
            AutoConfig.getConfigHolder(ModConfig.class).save();
        });

        return builder.build();
    }

    public static void init() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
