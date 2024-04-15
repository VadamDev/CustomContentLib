package net.vadamdev.customcontent.lib.durabilitybar;

import net.vadamdev.customcontent.api.items.IDurabilityBar;

/**
 * @author VadamDev
 * @since 19/07/2023
 */
public class TextDurabilityBar implements IDurabilityBar {
    private final String text, placeholder;

    public TextDurabilityBar(String text, String placeholder) {
        this.text = text;
        this.placeholder = placeholder;
    }

    @Override
    public String createDurabilityBar(int durability, int maxDurability) {
        return text.replace("%durability%", String.valueOf(durability)).replace("%max_durability%", String.valueOf(maxDurability));
    }

    @Override
    public String getPlaceholder() {
        return placeholder;
    }
}
