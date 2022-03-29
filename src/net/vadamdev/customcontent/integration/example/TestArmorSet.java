package net.vadamdev.customcontent.integration.example;

import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import org.bukkit.Bukkit;

/**
 * DONT USE THIS CLASS IT'S USED FOR DEMONSTRATION PURPOSES !
 *
 * @author VadamDev
 * @since 28/03/2022
 */
public class TestArmorSet extends ArmorSet {
    public TestArmorSet() {
        super(new CustomArmorPart[] {new TestArmorPart(), null, null, null});
    }

    @Override
    public void tick() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(hasFullSet(player)) {
                //Do things...
            }
        });

        //Or
        Bukkit.getOnlinePlayers().stream().filter(this::hasFullSet).forEach(player -> {
            //Do things...
        });
    }

    @Override
    public int getInterval() {
        return 20;
    }

    //Optional
    @Override
    public boolean isTickAsync() {
        return false;
    }
}
