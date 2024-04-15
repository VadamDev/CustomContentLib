package net.vadamdev.customcontent.api.resourcepack;

/**
 * Represents a model attached to a {@link net.vadamdev.customcontent.api.common.IRegistrable IRegistrable} entity
 *
 * @author VadamDev
 * @since 12/03/2024
 */
public interface RegistrableModel extends Model {
    String getRegistryName();
}
