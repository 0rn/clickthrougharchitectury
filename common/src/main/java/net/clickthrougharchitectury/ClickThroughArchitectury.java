package net.clickthrougharchitectury;

/*
import com.google.common.base.Suppliers;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;
*/

import java.io.*;
import java.util.Properties;

public class ClickThroughArchitectury {
    public static final String MOD_ID = "clickthrougharchitectury";
    public static final String CONFIG_FILENAME = "clickthrougharchitectury.properties";


    // config
    public static File config_file;
    public static boolean sign_enabled;
    public static boolean item_frame_enabled;
    public static boolean banner_enabled;
    public static boolean only_containers;
    public static boolean to_sign;
    
    public static void init() {
        config_file = ClickThroughArchitecturyExpectPlatform.getConfigDirectory().resolve(CONFIG_FILENAME).toFile();

        loadConfig();
    }

    public static void saveConfig() {
        System.out.println(config_file);
        Properties p = new Properties();

        p.setProperty("sign_enabled",        sign_enabled         ? "true" : "false");
        p.setProperty("item_frame_enabled",  item_frame_enabled   ? "true" : "false");
        p.setProperty("banner_enabled",      banner_enabled       ? "true" : "false");
        p.setProperty("only_containers",     only_containers      ? "true" : "false");
        p.setProperty("to_sign",             to_sign              ? "true" : "false");

        try (FileOutputStream so = new FileOutputStream(config_file)) {
            p.store(so, "Click Through Architectury configuration file");
        } catch (IOException e) {
            // do nothing
        }
    }

    public static void loadConfig() {
        Properties p = new Properties();
        try(FileInputStream si = new FileInputStream(config_file)) {
            p.load(si);
        } catch (IOException e) {
            // do nothing
        }

        sign_enabled =          p.getOrDefault("sign_enabled",         "true").equals("true");
        item_frame_enabled =    p.getOrDefault("item_frame_enabled",   "true").equals("true");
        banner_enabled =        p.getOrDefault("banner_enabled",       "true").equals("true");
        only_containers =       p.getOrDefault("only_containers",      "true").equals("true");
        to_sign =               p.getOrDefault("to_sign",              "false").equals("true");
    }
}
