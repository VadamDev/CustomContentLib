package net.vadamdev.customcontent.integration.test;

import net.vadamdev.customcontent.api.items.armor.ArmorPart;
import net.vadamdev.customcontent.api.items.armor.ArmorType;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;

public class TestArmorPart extends CustomArmorPart {
    public TestArmorPart() {
        super(ArmorType.CHAINMAIL, ArmorPart.HELMET, "Test Armor Part", "Test armor part lore");
    }

    @Override
    public String getRegistryName() {
        return "testarmorpart";
    }

    @Override
    public int getMaxDurability() {
        return 25;
    }
}
