package net.vadamdev.customcontent.integration.test;

import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.viaapi.tools.builders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.function.Consumer;

public class TestFood extends CustomFood {
    public TestFood() {
        super(new ItemBuilder(Material.COOKED_BEEF).setName("Test Food Item").setLore("This is a test food item").setGlowing().toItemStack());
    }

    @Override
    public String getRegistryName() {
        return "test_food";
    }

    @Override
    public boolean isEdibleEvenWithFullHunger() {
        return true;
    }

    @Override
    public Consumer<PlayerItemConsumeEvent> getAction() {
        return event -> event.getPlayer().setHealth(0);
    }
}
