package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.api.blocks.serialization.SerializableDataCompound;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.World;

import javax.annotation.Nonnull;

/**
 * @author VadamDev
 * @since 01/09/2022
 */
public class CustomTileEntity {
    protected final BlockPos blockPos;
    protected final World world;

    public CustomTileEntity(BlockPos blockPos) {
        this.blockPos = blockPos;
        this.world = this.blockPos.getWorld();
    }

    @Nonnull
    public SerializableDataCompound save(SerializableDataCompound compound) {
        return compound;
    }

    public void load(SerializableDataCompound compound) {

    }
}
