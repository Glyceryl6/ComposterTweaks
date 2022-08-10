package com.glyceryl6.tweaker.commands.server;

import com.glyceryl6.tweaker.json.JsonHandler;
import com.glyceryl6.tweaker.json.JsonRemoval;
import com.glyceryl6.tweaker.json.objects.CompostingRemoveEntry;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.Objects;

public class CommandRemoveEntry {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("remove").requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("item", ItemArgument.item())
                .then(Commands.argument("save", BoolArgumentType.bool())
                .executes((commandSource) -> removeEntry(commandSource.getSource(),
                ItemArgument.getItem(commandSource, "item"),
                BoolArgumentType.getBool(commandSource, "save")))));
    }

    private static int removeEntry(CommandSourceStack source, ItemInput itemInput, boolean save) {
        String entryName = Objects.requireNonNull(itemInput.getItem().getRegistryName()).toString();
        String itemName = itemInput.getItem().getDefaultInstance().getDisplayName().getString();
        if (save) {
            if (JsonHandler.removeEntry(entryName)) {
                JsonRemoval.addEntry(new CompostingRemoveEntry(entryName));
                source.sendSuccess(new TranslatableComponent("composting.info.remove.success", itemName), true);
            } else {
                source.sendSuccess(new TranslatableComponent("composting.info.remove.fail"), true);
            }
        } else {
            if (ComposterBlock.COMPOSTABLES.containsKey(itemInput.getItem())) {
                ComposterBlock.COMPOSTABLES.keySet().remove(itemInput.getItem());
                source.sendSuccess(new TranslatableComponent("composting.info.remove.success.temp", itemName), true);
            } else {
                source.sendSuccess(new TranslatableComponent("composting.info.remove.fail.temp"), true);
            }
        }

        return 0;
    }

}