package net.vadamdev.customcontent.api.entities;

import net.minecraft.server.v1_8_R3.*;
import net.vadamdev.customcontent.api.CustomContentAPI;
import net.vadamdev.customcontent.api.items.CustomSpawnEgg;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 25/08/2023
 */
public class CustomEntityContainer<T extends Entity> {
    private final String name;
    private final Class<? extends T> customEntityClass;
    private final Class<?> nmsClass;

    private final Map<BiomeBase, Tuple<EnumCreatureType, BiomeBase.BiomeMeta>> spawnEntries;

    private Constructor<? extends T> constructor;

    public CustomEntityContainer(String name, Class<? extends T> customEntityClass) {
        this.name = name;
        this.customEntityClass = customEntityClass;
        this.nmsClass = findOriginalNMS();

        this.spawnEntries = new HashMap<>();

        try {
            constructor = customEntityClass.getConstructor(World.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    protected void onSpawn(T entity) {}

    @Nullable
    public CustomSpawnEgg getSpawnEgg() {
        return null;
    }

    protected void addSpawnEntry(BiomeBase[] biomes, EnumCreatureType creatureType, int weight, int a, int b) {
        /*if(!nmsClass.isAssignableFrom(EntityInsentient.class))
            throw new UnsupportedOperationException("You cannot add a spawn entry if the custom entity is not an instanceof EntityInsentient");*/

        final BiomeBase.BiomeMeta meta = new BiomeBase.BiomeMeta((Class<? extends EntityInsentient>) customEntityClass, weight, a, b);
        for(BiomeBase biome : biomes)
            spawnEntries.put(biome, new Tuple<>(creatureType, meta));
    }

    @Nullable
    public T spawn(Location location, CreatureSpawnEvent.SpawnReason spawnReason) {
        try {
            final WorldServer ws = ((CraftWorld) location.getWorld()).getHandle();
            final T entity = constructor.newInstance(ws);

            if(entity instanceof EntityInsentient)
                ((EntityInsentient) entity).prepare(ws.E(new BlockPosition(entity)), null);

            entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

            onSpawn(entity);

            ws.addEntity(entity, spawnReason);

            return entity;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public Class<? extends T> getCustomEntityClass() {
        return customEntityClass;
    }

    public Class<?> getNmsClass() {
        return nmsClass;
    }

    public Map<BiomeBase, Tuple<EnumCreatureType, BiomeBase.BiomeMeta>> getSpawnEntries() {
        return spawnEntries;
    }

    public int getNMSEntityId() {
        return CustomContentAPI.get().getContentRegistry().getEntityId((Class<? extends Entity>) nmsClass);
    }

    private Class<?> findOriginalNMS() {
        Class<?> superClass = customEntityClass;
        do {
            if(superClass.getPackage().getName().equals("net.minecraft.server.v1_8_R3"))
                return superClass;
        }while((superClass = superClass.getSuperclass()) != null);

        return null;
    }
}
