package com.glyceryl6.tweaker.json.objects;

public class CompostingEntry {

    private String item;
    private float chance;

    public CompostingEntry() {}

    public CompostingEntry(String item, float chance) {
        this.item = item;
        this.chance = chance;
    }

    public String getItem() {
        return item;
    }

    public float getChance() {
        return chance;
    }

    @Override
    public String toString() {
        return "CompostingEntry{" + "item='" + item + '\'' + ", chance=" + chance + '}';
    }

}