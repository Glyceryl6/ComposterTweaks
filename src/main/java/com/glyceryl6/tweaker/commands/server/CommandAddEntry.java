package com.glyceryl6.tweaker.commands.server;

import com.glyceryl6.tweaker.json.JsonHandler;
import com.glyceryl6.tweaker.json.JsonRemoval;
import com.glyceryl6.tweaker.json.objects.CompostingEntry;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.Objects;

public class CommandAddEntry {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("add").requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("item", ItemArgument.item())
                .then(Commands.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                .then(Commands.argument("save", BoolArgumentType.bool())
                .executes((commandSource) -> addEntry(commandSource.getSource(),
                ItemArgument.getItem(commandSource, "item"),
                FloatArgumentType.getFloat(commandSource, "chance"),
                BoolArgumentType.getBool(commandSource, "save"))))));
    }

    private static int addEntry(CommandSourceStack source, ItemInput itemInput, float chance, boolean save) {
        String entryName = Objects.requireNonNull(itemInput.getItem().getRegistryName()).toString();
        String itemName = itemInput.getItem().getDefaultInstance().getDisplayName().getString();
        CompostingEntry compostingEntry = new CompostingEntry(entryName, chance);
        if (save) {
            if (JsonHandler.addEntry(compostingEntry)) {
                JsonRemoval.removeEntry(entryName);
                source.sendSuccess(new TranslatableComponent("composting.info.add.success", itemName), true);
            } else {
                source.sendSuccess(new TranslatableComponent("composting.info.add.fail"), true);
            }
        } else {
            if (!ComposterBlock.COMPOSTABLES.containsKey(itemInput.getItem())) {
                ComposterBlock.add(chance, itemInput.getItem());
                source.sendSuccess(new TranslatableComponent("composting.info.add.success.temp", itemName), true);
            } else {
                source.sendSuccess(new TranslatableComponent("composting.info.add.fail.temp"), true);
            }
        }

        return 0;
    }

}