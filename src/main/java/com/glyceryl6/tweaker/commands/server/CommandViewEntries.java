package com.glyceryl6.tweaker.commands.server;

import com.glyceryl6.tweaker.json.JsonHandler;
import com.glyceryl6.tweaker.json.JsonRemoval;
import com.mojang.brigadier.builder.ArgumentBuilder;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class CommandViewEntries {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("view").requires((commandSource) -> commandSource.hasPermission(2))
                .executes((commandSource) -> viewEntries(commandSource.getSource()));
    }

    private static int viewEntries(CommandSourceStack source) {
        JsonHandler.reload();
        JsonHandler.addCompostable();
        JsonRemoval.removeCompostable();
        for (Object2FloatMap.Entry<ItemLike> itemLike : ComposterBlock.COMPOSTABLES.object2FloatEntrySet()) {
            String itemName = itemLike.getKey().asItem().getDefaultInstance().getDisplayName().getString();
            float chance = itemLike.getFloatValue() * 100.0F;
            source.sendSuccess(new TextComponent(ChatFormatting.GREEN + itemName + " " + ChatFormatting.GOLD + chance + "%"), true);
        }
        return 0;
    }

}