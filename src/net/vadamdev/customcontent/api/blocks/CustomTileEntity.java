package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.api.blocks.serialization.SerializableDataCompound;
import net.vadamdev.customcontent.lib.BlockPos;

import javax.annotation.Nonnull;

/**
 * Represents a CCL's TileEntity
 * @see <a href="https://minecraft.fandom.com/wiki/Block_entity">https://minecraft.fandom.com/wiki/Block_entity</a>
 *
 * @author VadamDev
 * @since 01/09/2022
 */
public class CustomTileEntity {
    public final BlockPos blockPos;

    public CustomTileEntity(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    /**
     * Called when the {@link CustomTileEntity} is unloaded. It likely happens when the server is stopped.
     *
     * @param compound Data given by the associated {@link CustomBlock}. Likely a empty {@link SerializableDataCompound}
     * @return The data of the {@link CustomTileEntity}
     */
    @Nonnull
    public SerializableDataCompound save(SerializableDataCompound compound) {
        return compound;
    }


    /**
     * Called when the {@link CustomTileEntity} is loaded. It likely happens once the server finished starting.
     *
     * @param compound Data given by the {@link net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer IDataSerializer}
     */
    public void load(SerializableDataCompound compound) {

    }
}
