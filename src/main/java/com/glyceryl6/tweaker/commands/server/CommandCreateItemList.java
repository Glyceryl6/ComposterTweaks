package com.glyceryl6.tweaker.commands.server;

import com.glyceryl6.tweaker.ComposterTweaks;
import com.glyceryl6.tweaker.json.JsonHandler;
import com.glyceryl6.tweaker.json.JsonRemoval;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommandCreateItemList {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("export").requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("namespace", StringArgumentType.word())
                .executes((commandSource) -> createList(commandSource.getSource(),
                StringArgumentType.getString(commandSource, "namespace"))));
    }

    private static int createList(CommandSourceStack source, String namespace) {
        createDirectory();
        JsonHandler.reload();
        JsonHandler.addCompostable();
        JsonRemoval.removeCompostable();
        String s = DATE_FORMAT.format(new Date());
        File itemListFile = new File(FMLPaths.CONFIGDIR.get() + "/composter_tweaks/" + namespace + "_" + s + ".csv");
        try (FileWriter fileWriter = new FileWriter(itemListFile)) {
            itemListFile.createNewFile();
            fileWriter.write(new TranslatableComponent("composting.info.file.item_name").getString() + ",");
            fileWriter.append(new TranslatableComponent("composting.info.file.registry_name").getString()).append(",");
            fileWriter.append(new TranslatableComponent("composting.info.file.can_compost").getString()).append("\n");
            for (Item item : ForgeRegistries.ITEMS) {
                if (item != null && item.getRegistryName() != null) {
                    String itemName = item.getDefaultInstance().getDisplayName().getString();
                    itemName = itemName.replaceAll("\\[", "").replaceAll("]", "");
                    String canCompost = Boolean.toString(ComposterBlock.COMPOSTABLES.containsKey(item)).toLowerCase(Locale.ROOT);
                    if (namespace.equals("all")) {
                        String registryName = item.getRegistryName().toString();
                        String content = itemName + "," + registryName + "," + canCompost + "\n";
                        fileWriter.write(content);
                        fileWriter.flush();
                    } else {
                        if (ModList.get().isLoaded(namespace)) {
                            String id = item.getRegistryName().getNamespace();
                            if (id.equals(namespace)) {
                                String path = item.getRegistryName().getPath();
                                String content = itemName + "," + id + ":" + path + "," + canCompost + "\n";
                                fileWriter.write(content);
                                fileWriter.flush();
                            }
                        } else {
                            source.sendSuccess(new TranslatableComponent("composting.info.file.invalid"), true);
                            fileWriter.close();
                            itemListFile.delete();
                            break;
                        }
                    }
                }
            }
            if (itemListFile.exists()) {
                String fileName = itemListFile.getName();
                Component component = (new TextComponent(fileName))
                        .withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.YELLOW).withStyle((style)
                        -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, itemListFile.getAbsolutePath())));
                source.sendSuccess(new TranslatableComponent("composting.info.file.output", component), true);
            }
        } catch (IOException ignored) {}
        return 0;
    }

    private static void createDirectory() {
        Path configPath = FMLPaths.CONFIGDIR.get().toAbsolutePath();
        Path compostConfigPath = Paths.get(configPath.toString(), ComposterTweaks.MOD_ID);
        try {Files.createDirectory(compostConfigPath);
        } catch (IOException ignored) {}
    }

}