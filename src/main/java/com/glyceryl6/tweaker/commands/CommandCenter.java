package com.glyceryl6.tweaker.commands;

import com.glyceryl6.tweaker.commands.server.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class CommandCenter {

    public CommandCenter(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder
                .<CommandSourceStack> literal("composting")
                        .then(CommandAddEntry.register())
                        .then(CommandReloadJSON.register())
                        .then(CommandRemoveEntry.register())
                        .then(CommandViewEntries.register())
                        .then(CommandCreateItemList.register())
                        .executes(ctx -> 0));
    }

}