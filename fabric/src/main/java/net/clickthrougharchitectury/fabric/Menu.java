package net.clickthrougharchitectury.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.clickthrougharchitectury.ClothScreen;
import net.fabricmc.loader.api.FabricLoader;

public class Menu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return ClothScreen::getConfigScreen;
        } else {
            return null;
        }
    }
}
