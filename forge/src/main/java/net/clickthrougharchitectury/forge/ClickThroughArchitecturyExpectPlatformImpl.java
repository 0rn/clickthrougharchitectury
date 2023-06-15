package net.clickthrougharchitectury.forge;

import net.clickthrougharchitectury.ClickThroughArchitecturyExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ClickThroughArchitecturyExpectPlatformImpl {
    /**
     * This is our actual method to {@link ClickThroughArchitecturyExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
