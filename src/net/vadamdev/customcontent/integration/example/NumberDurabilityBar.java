package net.vadamdev.customcontent.integration.example;

import net.vadamdev.customcontent.api.items.IDurabilityBar;

/**
 * DONT USE THIS CLASS IT'S USED FOR DEMONSTRATION PURPOSES !
 *
 * An example of what a DurabilityBar can do.
 * @author VadamDev
 * @since 18/01/2022
 */
public class NumberDurabilityBar implements IDurabilityBar {
    private final String text, placeholder;

    public NumberDurabilityBar(String text, String placeholder) {
        this.text = text;
        this.placeholder = placeholder;
    }

    @Override
    public String createDurabilityBar(int durability, int maxDurability) {
        return text + durability;
    }

    @Override
    public String getPlaceholder() {
        return placeholder;
    }
}
