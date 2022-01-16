package net.vadamdev.customcontent.api.blocks;

import org.bukkit.Location;

public abstract class CustomBlock {
    private ItemBlock itemBlock;

    public CustomBlock(ItemBlock itemBlock) {
        this.itemBlock = itemBlock;
    }

    public abstract void onPlace(Location location);
    public abstract void onBreak();
}
