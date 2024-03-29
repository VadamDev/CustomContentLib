package net.vadamdev.customcontent.api;

import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viapi.tools.enums.EnumDirection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * The core of CustomContentLib. All parts of CCL can be accessed from here.
 * @see Provider
 *
 * @author VadamDev
 * @since 08/07/2023
 */
public interface CustomContentAPI {
    ContentRegistry getContentRegistry();
    RecipeRegistry getRecipeRegistry();

    boolean placeCustomBlock(BlockPos blockPos, CustomBlock customBlock, boolean checkValidity, @Nullable Player player);
    boolean breakCustomBlock(BlockPos blockPos, boolean checkValidity, boolean drop, @Nullable Entity entity);

    Optional<CustomTileEntity> getTileEntityAt(BlockPos blockPos);
    <T extends CustomTileEntity> Optional<T> findTileEntity(BlockPos blockPos, Class<T> clazz);

    @Nullable
    ItemStack getCustomTexture(BlockPos blockPos);
    void updateCustomTexture(BlockPos blockPos, ItemStack itemStack, @Nullable EnumDirection direction);

    @Nullable
    ItemStack getCustomItemAsItemStack(String registryName);

    boolean isCustomItem(ItemStack itemStack, String registryName);

    Map<String, ItemStack> getCustomItemstacks();

    final class Provider {
        private static CustomContentAPI api;

        private Provider() {}

        @Nonnull
        public static CustomContentAPI get() {
            return api;
        }

        public static void set(@Nonnull CustomContentAPI api) {
            Provider.api = api;
        }
    }
}
