package net.vadamdev.customcontent.internal.registry;

import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.EnumCreatureType;
import net.vadamdev.customcontent.api.entities.CustomEntityContainer;
import net.vadamdev.customcontent.api.items.CustomSpawnEgg;
import net.vadamdev.customcontent.internal.CustomContentPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 26/08/2023
 */
public class EntitiesRegistry {
    private final Logger logger;

    private final Map<Class<? extends Entity>, Integer> minecraftIds;
    private final Set<CustomEntityContainer<?>> containers;

    private boolean completed;

    public EntitiesRegistry() {
        this.logger = CustomContentPlugin.instance.getLogger();

        this.minecraftIds = retrieveMinecraftEntityIds();
        this.containers = new HashSet<>();

        this.completed = false;
    }

    public void registerCustomEntity(CustomEntityContainer<?> container) {
        if(!completed)
            containers.add(container);
        else {
            try {
                registerEntity(container, retrieveEntityDataMaps(), retrieveRegisterMethod(), retrieveBiomeBases());

                logger.warning("A custom entity was registered after CCL loading ! You should increase \"postWorldLoadTime\" in the config.yml to avoid problems.");
            }catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public int getEntityId(Class<? extends Entity> entityClass) {
        return minecraftIds.getOrDefault(entityClass, -1);
    }

    public void complete(Logger logger) {
        try {
            final List<Map<?, ?>> dataMaps = retrieveEntityDataMaps();
            final Method registerMethod = retrieveRegisterMethod();
            final Map<BiomeBase, Map<EnumCreatureType, List<BiomeBase.BiomeMeta>>> biomeBases = retrieveBiomeBases();

            int i = 0;
            for (CustomEntityContainer<?> container : containers) {
                registerEntity(container, dataMaps, registerMethod, biomeBases);
                i++;
            }

            if(i != 0)
                logger.info("-> Registered " + i + " of " + containers.size() + " custom entities");
        }catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        completed = true;
    }

    private void registerEntity(CustomEntityContainer<? extends Entity> container, List<Map<?, ?>> dataMaps, Method registerMethod, Map<BiomeBase, Map<EnumCreatureType, List<BiomeBase.BiomeMeta>>> biomeBases) throws InvocationTargetException, IllegalAccessException {
        final String name = container.getName();
        final int id = minecraftIds.get(container.getNmsClass());

        if(dataMaps.get(2).containsKey(id)) {
            dataMaps.get(0).remove(name);
            dataMaps.get(2).remove(id);
        }

        registerMethod.invoke(null, container.getCustomEntityClass(), name, id);

        container.getSpawnEntries().forEach((biomeBase, tuple) -> biomeBases.get(biomeBase).get(tuple.a()).add(tuple.b()));

        final CustomSpawnEgg spawnEgg = container.getSpawnEgg();
        if(spawnEgg != null)
            CustomContentPlugin.instance.getItemsRegistry().registerCustomItem(spawnEgg);
    }

    private Map<BiomeBase, Map<EnumCreatureType, List<BiomeBase.BiomeMeta>>> retrieveBiomeBases() throws IllegalAccessException {
        final Map<BiomeBase, Map<EnumCreatureType, List<BiomeBase.BiomeMeta>>> biomeBases = new HashMap<>();

        for (Field biomeField : BiomeBase.class.getDeclaredFields()) {
            if(!biomeField.getType().isAssignableFrom(BiomeBase.class))
                continue;

            final BiomeBase biome = (BiomeBase) biomeField.get(null);

            final Map<EnumCreatureType, List<BiomeBase.BiomeMeta>> dataMap = new HashMap<>();
            for (Field listField : BiomeBase.class.getDeclaredFields()) {
                if(!listField.getType().isAssignableFrom(List.class))
                    continue;

                listField.setAccessible(true);

                dataMap.put(getCreatureTypeByFieldName(listField.getName()), (List<BiomeBase.BiomeMeta>) listField.get(biome));
            }

            biomeBases.put(biome, dataMap);
        }

        return biomeBases;
    }

    private List<Map<?, ?>> retrieveEntityDataMaps() throws IllegalAccessException {
        final List<Map<?, ?>> dataMaps = new ArrayList<>();

        for (Field field : EntityTypes.class.getDeclaredFields()) {
            if(!field.getType().isAssignableFrom(Map.class))
                continue;

            field.setAccessible(true);
            dataMaps.add((Map<?, ?>) field.get(null));
        }

        return dataMaps;
    }

    private Method retrieveRegisterMethod() throws NoSuchMethodException {
        final Method registerMethod = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
        registerMethod.setAccessible(true);

        return registerMethod;
    }

    private Map<Class<? extends Entity>, Integer> retrieveMinecraftEntityIds() {
        try {
            final Field field = EntityTypes.class.getDeclaredField("f");
            field.setAccessible(true);

            return (Map<Class<? extends Entity>, Integer>) field.get(null);
        }catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private EnumCreatureType getCreatureTypeByFieldName(String fieldName) {
        switch(fieldName) {
            case "at":
                return EnumCreatureType.MONSTER;
            case "au":
                return EnumCreatureType.CREATURE;
            case "av":
                return EnumCreatureType.WATER_CREATURE;
            case "aw":
                return EnumCreatureType.AMBIENT;
            default:
                return null;
        }
    }
}
