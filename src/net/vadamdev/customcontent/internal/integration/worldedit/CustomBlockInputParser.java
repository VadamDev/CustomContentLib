package net.vadamdev.customcontent.internal.integration.worldedit;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.*;
import com.sk89q.worldedit.blocks.metadata.MobType;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.input.DisallowedUsageException;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.NoMatchException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.registry.InputParser;
import com.sk89q.worldedit.world.World;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is based on com.sk89q.worldedit.extension.factory.DefaultBlockParser
 * All credits goes to the WorldEdit team.
 */
public class CustomBlockInputParser extends InputParser<BaseBlock> {
    private final BlocksRegistry blocksRegistry;

    public CustomBlockInputParser(WorldEdit worldEdit, BlocksRegistry blocksRegistry) {
        super(worldEdit);

        this.blocksRegistry = blocksRegistry;
    }

    @Override
    public BaseBlock parseFromInput(String input, ParserContext context) throws InputParseException {
        //CCL CHANGES START HERE
        for(CustomBlock customBlock : blocksRegistry.getCustomBlocks()) {
            final String registryName = customBlock.getRegistryName();

            if(registryName.equalsIgnoreCase(input)) {
                final Map<String, Tag> data = new HashMap<>();
                data.put("CCL-RegistryName", new StringTag(registryName));

                return new BaseBlock(1, 0, new CompoundTag(data));
            }
        }
        //CCL CHANGES END HERE

        String originalInput = input;
        input = input.replace("_", " ");
        input = input.replace(";", "|");
        Exception suppressed = null;

        try {
            BaseBlock modified = this.parseLogic(input, context);
            if (modified != null) {
                return modified;
            }
        } catch (Exception var6) {
            suppressed = var6;
        }

        try {
            return this.parseLogic(originalInput, context);
        } catch (Exception var7) {
            if (suppressed != null) {
                var7.addSuppressed(suppressed);
            }

            throw var7;
        }
    }

    private BaseBlock parseLogic(String input, ParserContext context) throws InputParseException {
        String[] blockAndExtraData = input.split("\\|");
        String[] blockLocator = blockAndExtraData[0].split(":", 3);
        String[] typeAndData;
        switch (blockLocator.length) {
            case 3:
                typeAndData = new String[]{blockLocator[0] + ":" + blockLocator[1], blockLocator[2]};
                break;
            default:
                typeAndData = blockLocator;
        }

        String testId = typeAndData[0];
        int blockId = -1;
        int data = -1;
        boolean parseDataValue = true;
        BlockType blockType;
        if ("hand".equalsIgnoreCase(testId)) {
            BaseBlock blockInHand = getBlockInHand(context.requireActor());
            if (blockInHand.getClass() != BaseBlock.class) {
                return blockInHand;
            }

            blockId = blockInHand.getId();
            blockType = BlockType.fromID(blockId);
            data = blockInHand.getData();
        } else if ("pos1".equalsIgnoreCase(testId)) {
            World world = context.requireWorld();

            BlockVector primaryPosition;
            try {
                primaryPosition = context.requireSession().getRegionSelector(world).getPrimaryPosition();
            } catch (IncompleteRegionException var19) {
                throw new InputParseException("Your selection is not complete.");
            }

            BaseBlock blockInHand = world.getBlock(primaryPosition);
            if (blockInHand.getClass() != BaseBlock.class) {
                return blockInHand;
            }

            blockId = blockInHand.getId();
            blockType = BlockType.fromID(blockId);
            data = blockInHand.getData();
        } else {
            try {
                blockId = Integer.parseInt(testId);
                blockType = BlockType.fromID(blockId);
            } catch (NumberFormatException var22) {
                blockType = BlockType.lookup(testId);
                if (blockType == null) {
                    int t = this.worldEdit.getServer().resolveItem(testId);
                    if (t >= 0) {
                        blockType = BlockType.fromID(t);
                        blockId = t;
                    } else if (blockLocator.length == 2) {
                        t = this.worldEdit.getServer().resolveItem(blockAndExtraData[0]);
                        if (t >= 0) {
                            blockType = BlockType.fromID(t);
                            blockId = t;
                            typeAndData = new String[]{blockAndExtraData[0]};
                            testId = blockAndExtraData[0];
                        }
                    }
                }
            }

            if (blockId == -1 && blockType == null) {
                ClothColor col = ClothColor.lookup(testId);
                if (col == null) {
                    throw new NoMatchException("Can't figure out what block '" + input + "' refers to");
                }

                blockType = BlockType.CLOTH;
                data = col.getID();
                parseDataValue = false;
            }

            if (blockId == -1) {
                blockId = blockType.getID();
            }

            if (!context.requireWorld().isValidBlockType(blockId)) {
                throw new NoMatchException("Does not match a valid block type: '" + input + "'");
            }
        }

        if (!context.isPreferringWildcard() && data == -1) {
            data = 0;
        }

        if (parseDataValue) {
            try {
                if (typeAndData.length > 1 && !typeAndData[1].isEmpty()) {
                    data = Integer.parseInt(typeAndData[1]);
                }

                if (data > 15) {
                    throw new NoMatchException("Invalid data value '" + typeAndData[1] + "'");
                }

                if (data < 0 && (context.isRestricted() || data != -1)) {
                    data = 0;
                }
            } catch (NumberFormatException var21) {
                if (blockType == null) {
                    throw new NoMatchException("Unknown data value '" + typeAndData[1] + "'");
                }

                label170:
                switch (blockType) {
                    case CLOTH:
                    case STAINED_CLAY:
                    case CARPET:
                        ClothColor col = ClothColor.lookup(typeAndData[1]);
                        if (col == null) {
                            throw new NoMatchException("Unknown wool color '" + typeAndData[1] + "'");
                        }

                        data = col.getID();
                        break;
                    case STEP:
                    case DOUBLE_STEP:
                        BlockType dataType = BlockType.lookup(typeAndData[1]);
                        if (dataType == null) {
                            throw new NoMatchException("Unknown step type '" + typeAndData[1] + "'");
                        }

                        switch (dataType) {
                            case STONE:
                                data = 0;
                                break label170;
                            case SANDSTONE:
                                data = 1;
                                break label170;
                            case WOOD:
                                data = 2;
                                break label170;
                            case COBBLESTONE:
                                data = 3;
                                break label170;
                            case BRICK:
                                data = 4;
                                break label170;
                            case STONE_BRICK:
                                data = 5;
                                break label170;
                            case NETHER_BRICK:
                                data = 6;
                                break label170;
                            case QUARTZ_BLOCK:
                                data = 7;
                                break label170;
                            default:
                                throw new NoMatchException("Invalid step type '" + typeAndData[1] + "'");
                        }
                    default:
                        throw new NoMatchException("Unknown data value '" + typeAndData[1] + "'");
                }
            }
        }

        Actor actor = context.requireActor();
        if (context.isRestricted() && actor != null && !actor.hasPermission("worldedit.anyblock") && this.worldEdit.getConfiguration().disallowedBlocks.contains(blockId)) {
            throw new DisallowedUsageException("You are not allowed to use '" + input + "'");
        } else if (blockType == null) {
            return new BaseBlock(blockId, data);
        } else {
            int skullType;
            switch (blockType) {
                case SIGN_POST:
                case WALL_SIGN:
                    String[] text = new String[]{blockAndExtraData.length > 1 ? blockAndExtraData[1] : "", blockAndExtraData.length > 2 ? blockAndExtraData[2] : "", blockAndExtraData.length > 3 ? blockAndExtraData[3] : "", blockAndExtraData.length > 4 ? blockAndExtraData[4] : ""};
                    return new SignBlock(blockType.getID(), data, text);
                case MOB_SPAWNER:
                    if (blockAndExtraData.length <= 1) {
                        return new MobSpawnerBlock(data, MobType.PIG.getName());
                    } else {
                        String mobName = blockAndExtraData[1];
                        MobType[] var32 = MobType.values();
                        int var33 = var32.length;

                        for(skullType = 0; skullType < var33; ++skullType) {
                            MobType mobType = var32[skullType];
                            if (mobType.getName().toLowerCase().equals(mobName.toLowerCase())) {
                                mobName = mobType.getName();
                                break;
                            }
                        }

                        if (!this.worldEdit.getServer().isValidMobType(mobName)) {
                            throw new NoMatchException("Unknown mob type '" + mobName + "'");
                        } else {
                            return new MobSpawnerBlock(data, mobName);
                        }
                    }
                case NOTE_BLOCK:
                    if (blockAndExtraData.length <= 1) {
                        return new NoteBlock(data, (byte)0);
                    } else {
                        byte note = Byte.parseByte(blockAndExtraData[1]);
                        if (note >= 0 && note <= 24) {
                            return new NoteBlock(data, note);
                        }

                        throw new InputParseException("Out of range note value: '" + blockAndExtraData[1] + "'");
                    }
                case HEAD:
                    if (blockAndExtraData.length <= 1) {
                        return new SkullBlock(data);
                    } else {
                        byte rot = 0;
                        String type = "";

                        try {
                            rot = Byte.parseByte(blockAndExtraData[1]);
                        } catch (NumberFormatException var20) {
                            type = blockAndExtraData[1];
                            if (blockAndExtraData.length > 2) {
                                try {
                                    rot = Byte.parseByte(blockAndExtraData[2]);
                                } catch (NumberFormatException var18) {
                                    throw new InputParseException("Second part of skull metadata should be a number.");
                                }
                            }
                        }

                        skullType = 0;
                        if (!type.isEmpty()) {
                            if (type.equalsIgnoreCase("skeleton")) {
                                skullType = 0;
                            } else if (type.equalsIgnoreCase("wither")) {
                                skullType = 1;
                            } else if (type.equalsIgnoreCase("zombie")) {
                                skullType = 2;
                            } else if (type.equalsIgnoreCase("creeper")) {
                                skullType = 4;
                            } else {
                                skullType = 3;
                            }
                        }

                        if (skullType == 3) {
                            return new SkullBlock(data, rot, type.replace(" ", "_"));
                        }

                        return new SkullBlock(data, (byte)skullType, rot);
                    }
                default:
                    return new BaseBlock(blockId, data);
            }
        }
    }

    private static BaseBlock getBlockInHand(Actor actor) throws InputParseException {
        if (actor instanceof Player) {
            try {
                return ((Player)actor).getBlockInHand();
            } catch (NotABlockException var2) {
                throw new InputParseException("You're not holding a block!");
            } catch (WorldEditException var3) {
                throw new InputParseException("Unknown error occurred: " + var3.getMessage(), var3);
            }
        } else {
            throw new InputParseException("The user is not a player!");
        }
    }
}
