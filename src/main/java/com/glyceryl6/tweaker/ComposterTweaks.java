package com.glyceryl6.tweaker;

import com.glyceryl6.tweaker.commands.CommandCenter;
import com.glyceryl6.tweaker.event.PlayerWithWorld;
import com.glyceryl6.tweaker.json.JsonHandler;
import com.glyceryl6.tweaker.json.JsonRemoval;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(ComposterTweaks.MOD_ID)
public class ComposterTweaks {

    public static final String MOD_ID = "composter_tweaks";

    public ComposterTweaks() {
        JsonHandler.setup();
        JsonRemoval.setup();
        MinecraftForge.EVENT_BUS.register(new PlayerWithWorld());
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    public void registerCommands(RegisterCommandsEvent event) {
        new CommandCenter(event.getDispatcher());
    }

}