package com.glyceryl6.tweaker.json.objects;

import java.util.List;

public class CompostingConfig {

    protected List<CompostingEntry> compostingConfig;

    public CompostingConfig(List<CompostingEntry> compostingConfig) {
        this.compostingConfig = compostingConfig;
    }

    public List<CompostingEntry> getCompostingConfig() {
        return compostingConfig;
    }

}