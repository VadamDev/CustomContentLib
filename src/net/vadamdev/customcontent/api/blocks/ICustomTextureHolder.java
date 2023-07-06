package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.annotations.Experimental;
import org.bukkit.entity.Player;

/**
 * @author VadamDev
 * @since 05/07/2023
 */
@Experimental
public interface ICustomTextureHolder {
    String getTextureName();

    default int getBlockRotation(Player player) {
        return 0;
    }
}
