package net.vadamdev.customcontent.integration.example;

import net.vadamdev.customcontent.api.items.IDurabilityBar;
import net.vadamdev.customcontent.api.items.armor.ArmorPart;
import net.vadamdev.customcontent.api.items.armor.ArmorType;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;

/**
 * DONT USE THIS CLASS IT'S USED FOR DEMONSTRATION PURPOSES !
 *
 * @author VadamDev
 */
public class TestArmorPart extends CustomArmorPart {
    public TestArmorPart() {
        super(ArmorType.CHAINMAIL, ArmorPart.HELMET, "Test Armor Part", "Test armor part lore", "%bar%");
    }

    @Override
    public String getRegistryName() {
        return "testarmorpart";
    }

    @Override
    public int getMaxDurability() {
        return 10;
    }

    //Optional
    @Override
    public IDurabilityBar getDurabilityBar() {
        return new NumberDurabilityBar("Durability : ", "%bar%");
    }
}
