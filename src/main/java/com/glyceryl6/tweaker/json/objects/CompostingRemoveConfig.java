package com.glyceryl6.tweaker.json.objects;

import java.util.List;

public class CompostingRemoveConfig {

    protected List<CompostingRemoveEntry> compostRemoveConfig;

    public CompostingRemoveConfig(List<CompostingRemoveEntry> compostConfig) {
        this.compostRemoveConfig = compostConfig;
    }

    public List<CompostingRemoveEntry> getCompostRemoveConfig() {
        return compostRemoveConfig;
    }

}