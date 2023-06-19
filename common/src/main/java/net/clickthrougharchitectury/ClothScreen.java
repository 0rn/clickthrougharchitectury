package net.clickthrougharchitectury;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.network.chat.Component;

public class ClothScreen {
    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder buidler = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("key.clickthrougharchitectury.title"));

        ConfigEntryBuilder entries = buidler.entryBuilder();
        ConfigCategory main = buidler.getOrCreateCategory(Component.translatable("config.clickthrougharchitectury.category"));
        // sign enabled
        main.addEntry(entries.startBooleanToggle(
                Component.translatable("config.clickthrougharchitectury.sign_enabled"),
                ClickThroughArchitectury.sign_enabled
            )
            .setSaveConsumer(bool -> ClickThroughArchitectury.sign_enabled = bool)
            .build()
        );
        // item frame enabled
        main.addEntry(entries.startBooleanToggle(
                                Component.translatable("config.clickthrougharchitectury.item_frame_enabled"),
                                ClickThroughArchitectury.item_frame_enabled
                        )
                        .setSaveConsumer(bool -> ClickThroughArchitectury.item_frame_enabled = bool)
                        .build()
        );
        // banner enabled
        main.addEntry(entries.startBooleanToggle(
                                Component.translatable("config.clickthrougharchitectury.banner_enabled"),
                                ClickThroughArchitectury.banner_enabled
                        )
                        .setSaveConsumer(bool -> ClickThroughArchitectury.banner_enabled = bool)
                        .build()
        );
        // only containers
        main.addEntry(entries.startBooleanToggle(
                                Component.translatable("config.clickthrougharchitectury.only_containers"),
                                ClickThroughArchitectury.only_containers
                        )
                        .setSaveConsumer(bool -> ClickThroughArchitectury.only_containers = bool)
                        .build()
        );
        // to sign
        main.addEntry(entries.startBooleanToggle(
                Component.translatable("config.clickthrougharchitectury.to_sign"),
                ClickThroughArchitectury.to_sign
            )
            .setSaveConsumer(bool -> ClickThroughArchitectury.to_sign = bool)
            .build()
        );

        return buidler.setSavingRunnable(() -> {
            ClickThroughArchitectury.saveConfig();
        }).build();
    }
}
