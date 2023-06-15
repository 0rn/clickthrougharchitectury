package net.clickthrougharchitectury.forge;

import net.clickthrougharchitectury.ClickThroughArchitectury;
import net.minecraftforge.fml.common.Mod;

@Mod(ClickThroughArchitectury.MOD_ID)
public class ClickThroughArchitecturyForge {
    public ClickThroughArchitecturyForge() {
        // Submit our event bus to let architectury register our content on the right time
        //EventBuses.registerModEventBus(ClickThroughArchitectury.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ClickThroughArchitectury.init();
    }
}
