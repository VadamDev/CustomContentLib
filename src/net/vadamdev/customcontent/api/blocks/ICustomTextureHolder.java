package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.annotations.Experimental;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * @author VadamDev
 * @since 05/07/2023
 */
@Experimental
public interface ICustomTextureHolder {
    String getTextureName();

    default int getBlockRotation(@Nullable Player player) {
        return 0;
    }
}
