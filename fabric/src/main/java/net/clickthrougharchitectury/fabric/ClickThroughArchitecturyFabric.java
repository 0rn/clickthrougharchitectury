package net.clickthrougharchitectury.fabric;

import net.clickthrougharchitectury.ClickThroughArchitectury;
import net.fabricmc.api.ModInitializer;

public class ClickThroughArchitecturyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ClickThroughArchitectury.init();
    }
}
