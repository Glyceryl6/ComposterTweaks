package com.glyceryl6.tweaker.json;

import com.glyceryl6.tweaker.ComposterTweaks;
import com.glyceryl6.tweaker.json.objects.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class JsonRemoval {

    private static final Gson gson = new Gson();
    private static final List<CompostingRemoveEntry> compostRemoveEntries = new ArrayList<>();
    private static CompostingRemoveConfig compostingRemoveConfig = new CompostingRemoveConfig(compostRemoveEntries);
    public static final File JSON_FILE = new File(FMLPaths.CONFIGDIR.get() + "/composter_tweaks/remove_list.json");

    public static void setup() {
        createDirectory();
        if (!JSON_FILE.exists()) {
            writeJson(JSON_FILE);
        }
        readJson(JSON_FILE);
    }

    public static void reload() {
        writeJson(JSON_FILE);
        readJson(JSON_FILE);
    }

    public static boolean containsEntry(CompostingRemoveEntry entry) {
        for (CompostingRemoveEntry entry1 : compostingRemoveConfig.getCompostRemoveConfig()) {
            if (entry1.getItem().equals(entry.getItem())) {
                return true;
            }
        }
        return false;
    }

    public static void addEntry(CompostingRemoveEntry entry) {
        if (!containsEntry(entry)) {
            compostingRemoveConfig.getCompostRemoveConfig().add(entry);
            reload();
        }
    }

    public static void removeEntry(String entryName) {
        if (containsEntry(new CompostingRemoveEntry(entryName))) {
            compostingRemoveConfig.getCompostRemoveConfig().removeIf(entry -> entry.getItem().equals(entryName));
            reload();
        }
    }

    public static void writeJson(File jsonFile) {
        try (Writer writer = new FileWriter(jsonFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(compostingRemoveConfig, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readJson(File jsonFile) {
        try (Reader reader = new FileReader(jsonFile)) {
            compostingRemoveConfig = gson.fromJson(reader, CompostingRemoveConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDirectory() {
        Path configPath = FMLPaths.CONFIGDIR.get().toAbsolutePath();
        Path compostConfigPath = Paths.get(configPath.toString(), ComposterTweaks.MOD_ID);
        try {Files.createDirectory(compostConfigPath);
        } catch (IOException ignored) {}
    }

    public static void removeCompostable() {
        for (CompostingRemoveEntry entry : compostingRemoveConfig.getCompostRemoveConfig()) {
            ResourceLocation resource = new ResourceLocation(entry.getItem());
            Item item = ForgeRegistries.ITEMS.getValue(resource);
            if (ComposterBlock.COMPOSTABLES.containsKey(item)) {
                ComposterBlock.COMPOSTABLES.keySet().remove(item);
            }
            if (item == null) {
                removeEntry(entry.getItem());
            }
        }
    }

}