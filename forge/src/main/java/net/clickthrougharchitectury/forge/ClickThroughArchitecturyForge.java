package net.clickthrougharchitectury.forge;

import net.clickthrougharchitectury.ClickThroughArchitectury;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

@Mod(ClickThroughArchitectury.MOD_ID)
public class ClickThroughArchitecturyForge {
    public ClickThroughArchitecturyForge() {
        // Submit our event bus to let architectury register our content on the right time
        //EventBuses.registerModEventBus(ClickThroughArchitectury.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ClickThroughArchitectury.init();

        ModLoadingContext.get().registerExtensionPoint(
                IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true)
        );
        if (ModList.get().isLoaded("cloth_config")) {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClickThroughArchitecturyCloth::register);
        }
    }
}
