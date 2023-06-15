package net.clickthrougharchitectury.fabric;

import net.clickthrougharchitectury.ClickThroughArchitecturyExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ClickThroughArchitecturyExpectPlatformImpl {
    /**
     * This is our actual method to {@link ClickThroughArchitecturyExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
