package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.IRegistrable;
import net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VadamDev
 * @since 01/09/2022
 */
public abstract class CustomBlock implements IRegistrable {
    protected ItemStack itemStack;

    public CustomBlock(ItemStack itemStack) {
        if(!itemStack.getType().isBlock())
            throw new UnsupportedOperationException("Provided itemstack is not a block");

        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());
    }

    public boolean onInteract(Block block, BlockPos blockPos, Player player) {
        return false;
    }

    public boolean onPlace(Block block, BlockPos blockPos, Player player) {
        getDataSerializer().write(blockPos);

        if(this instanceof ITileEntityProvider)
            CustomContentLib.instance.getTileEntityHandler().addTileEntity(blockPos, this, ((ITileEntityProvider) this).createTileEntity(blockPos));

        return false;
    }

    public boolean onBreak(BlockPos blockPos, @Nullable CustomTileEntity tileEntity, @Nullable ItemStack itemStack) {
        if(getDataSerializer().contains(blockPos))
            getDataSerializer().remove(blockPos);

        if(tileEntity != null)
            CustomContentLib.instance.getTileEntityHandler().removeTileEntity(blockPos);

        return false;
    }

    @Nonnull
    public abstract IDataSerializer getDataSerializer();

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
