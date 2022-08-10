package com.glyceryl6.tweaker.event;

import com.glyceryl6.tweaker.json.JsonHandler;
import com.glyceryl6.tweaker.json.JsonRemoval;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerWithWorld {

    @SubscribeEvent
    public void onPlayerJoinWorld(WorldEvent.Load event) {
        JsonHandler.addCompostable();
        JsonRemoval.removeCompostable();
    }

    @SubscribeEvent
    public void onPlayerExitWorld(WorldEvent.Unload event) {

    }

}