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

public class JsonHandler {

    private static final Gson gson = new Gson();
    private static final List<CompostingEntry> compostingEntries = new ArrayList<>();
    private static CompostingConfig compostingConfig = new CompostingConfig(compostingEntries);
    public static final File JSON_FILE = new File(FMLPaths.CONFIGDIR.get() + "/composter_tweaks/composting_list.json");

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

    public static boolean containsEntry(CompostingEntry entry) {
        for (CompostingEntry entry1 : compostingConfig.getCompostingConfig()) {
            if (entry1.getItem().equals(entry.getItem())) {
                return true;
            }
        }
        return false;
    }

    public static boolean addEntry(CompostingEntry entry) {
        ResourceLocation resource = new ResourceLocation(entry.getItem());
        Item item = ForgeRegistries.ITEMS.getValue(resource);
        boolean contain = ComposterBlock.COMPOSTABLES.containsKey(item);
        if (!containsEntry(entry) && item != null && !contain) {
            compostingConfig.getCompostingConfig().add(entry);
            ComposterBlock.add(entry.getChance(), item);
            reload();
            return true;
        }
        return false;
    }

    public static boolean removeEntry(String entryName) {
        ResourceLocation resource = new ResourceLocation(entryName);
        Item item = ForgeRegistries.ITEMS.getValue(resource);
        if (containsEntry(new CompostingEntry(entryName, 0.0F))) {
            compostingConfig.getCompostingConfig().removeIf(entry -> entry.getItem().equals(entryName));
            reload();
            return true;
        }
        if (ComposterBlock.COMPOSTABLES.containsKey(item)) {
            ComposterBlock.COMPOSTABLES.keySet().remove(item);
            reload();
            return true;
        }
        return false;
    }

    public static void writeJson(File jsonFile) {
        try (Writer writer = new FileWriter(jsonFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(compostingConfig, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readJson(File jsonFile) {
        try (Reader reader = new FileReader(jsonFile)) {
            compostingConfig = gson.fromJson(reader, CompostingConfig.class);
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

    public static void addCompostable() {
        for (CompostingEntry entry : compostingConfig.getCompostingConfig()) {
            ResourceLocation resource = new ResourceLocation(entry.getItem());
            Item item = ForgeRegistries.ITEMS.getValue(resource);
            float chance = entry.getChance();
            boolean contain = ComposterBlock.COMPOSTABLES.containsKey(item);
            if (item != null && chance <= 1.0F && !contain) {
                ComposterBlock.add(chance, item);
            }
            if (item == null) {
                removeEntry(entry.getItem());
            }
        }
    }

}