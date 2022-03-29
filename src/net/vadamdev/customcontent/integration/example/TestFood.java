package net.vadamdev.customcontent.integration.example;

import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.viaapi.tools.builders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.function.Consumer;

/**
 * DONT USE THIS CLASS IT'S USED FOR DEMONSTRATION PURPOSES !
 *
 * @author VadamDev
 * @since 29/03/2022
 */
public class TestFood extends CustomFood {
    public TestFood() {
        super(new ItemBuilder(Material.COOKED_BEEF).setName("Test Food Item").toItemStack());
    }

    @Override
    public String getRegistryName() {
        return "test_food";
    }

    @Override
    public Consumer<PlayerItemConsumeEvent> getAction() {
        return event -> event.getPlayer().setHealth(0);
    }

    //Optional
    @Override
    public boolean isEdibleEvenWithFullHunger() {
        return true;
    }
}
