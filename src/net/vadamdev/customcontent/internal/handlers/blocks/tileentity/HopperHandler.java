package net.vadamdev.customcontent.internal.handlers.blocks.tileentity;

import net.vadamdev.customcontent.api.blocks.container.Container;
import net.vadamdev.customcontent.api.blocks.container.WorldyContainer;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author VadamDev
 * @since 13/05/2024
 */
public class HopperHandler {
    private static final BlockFace[] INPUT_FACES = new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP };
    private static final BlockFace[] OUTPUT_FACES = new BlockFace[] { BlockFace.DOWN };

    private final Map<BlockPos, WorldyContainer> containers;

    public HopperHandler() {
        this.containers = new ConcurrentHashMap<>();

        Bukkit.getScheduler().runTaskTimerAsynchronously(CustomContentPlugin.instance, this::updateContainers, 0, 8);
    }

    /*
       Register / Remove
     */

    public void register(BlockPos blockPos, WorldyContainer container) {
        if(!container.isHopperCompatible())
            return;

        containers.put(blockPos, container);
    }

    public void remove(BlockPos blockPos) {
        if(!containers.containsKey(blockPos))
            return;

        containers.remove(blockPos);
    }

    /*
       Updater
     */

    private void updateContainers() {
        for(Map.Entry<BlockPos, WorldyContainer> entry : containers.entrySet()) {
            if(!entry.getKey().isChunkLoaded())
                continue;

            final Block block = entry.getKey().getBlock();
            final WorldyContainer container = entry.getValue();

            checkInputFaces(block, container);
            checkOutputFaces(block, container);
        }
    }

    /*
       Hopper logic
     */

    private void checkInputFaces(Block block, WorldyContainer container) {
        for(BlockFace face : INPUT_FACES) {
            final Block hopperBlock = block.getRelative(face);
            if(hopperBlock.isBlockPowered())
                continue;

            final BlockState state = hopperBlock.getState();
            if(!(state instanceof Hopper))
                continue;

            if(!getHopperFace(state).getOppositeFace().equals(face))
                continue;

            final Inventory hopperInventory = ((Hopper) state).getInventory();

            final int slotToWorkWith = findFirstFullSlot(hopperInventory);
            if(slotToWorkWith == -1)
                continue;

            final ItemStack current = hopperInventory.getItem(slotToWorkWith);

            final int[] availableSlots = container.getSlotsForFace(face);
            if(availableSlots.length == 0) {
                for(int slot = 0; slot < container.getContainerSize(); slot++) {
                    if(container.canPlaceItemTroughFace(slot, current, face) && container.canPlaceItem(slot, current)) {
                        addItem(hopperInventory, container, slotToWorkWith, slot);
                        break;
                    }
                }
            }else {
                for(int slot : availableSlots) {
                    if(container.canPlaceItemTroughFace(slot, current, face) && container.canPlaceItem(slot, current)) {
                        addItem(hopperInventory, container, slotToWorkWith, slot);
                        break;
                    }
                }
            }
        }
    }

    private void checkOutputFaces(Block block, WorldyContainer container) {
        for(BlockFace face : OUTPUT_FACES) {
            final BlockState state = block.getRelative(face).getState();
            if(!(state instanceof Hopper))
                continue;

            final Inventory hopperInventory = ((Hopper) state).getInventory();

            final int[] availableSlots = container.getSlotsForFace(face);
            if(availableSlots.length == 0) {
                for(int slot = 0; slot < container.getContainerSize(); slot++) {
                    final ItemStack current = container.getItem(slot);
                    if(current == null || current.getType().equals(Material.AIR))
                        continue;

                    if(container.canTakeItemTroughFace(slot, current, face)) {
                        removeItem(hopperInventory, container, slot);
                        break;
                    }
                }
            }else {
                for(int slot : availableSlots) {
                    final ItemStack current = container.getItem(slot);
                    if(current == null || current.getType().equals(Material.AIR))
                        continue;

                    if(container.canTakeItemTroughFace(slot, current, face)) {
                        removeItem(hopperInventory, container, slot);
                        break;
                    }
                }
            }
        }
    }

    /*
       Transactions
     */

    private void addItem(Inventory hopperInventory, Container container, int hopperSlot, int destinationSlot) {
        final ItemStack itemToAdd = hopperInventory.getItem(hopperSlot);
        final ItemStack itemToAddClone = itemToAdd.clone();
        itemToAddClone.setAmount(1);

        final ItemStack remaining = container.addItem(destinationSlot, itemToAddClone);
        if(remaining != null)
            return;

        if(itemToAdd.getAmount() <= 1)
            hopperInventory.setItem(hopperSlot, null);
        else
            itemToAdd.setAmount(itemToAdd.getAmount() - 1);
    }

    private void removeItem(Inventory hopperInventory, Container container, int destinationSlot) {
        final ItemStack itemToRemoveClone = container.getItem(destinationSlot).clone();
        itemToRemoveClone.setAmount(1);

        final Collection<ItemStack> remaining = hopperInventory.addItem(itemToRemoveClone).values();
        if(!remaining.isEmpty())
            return;

        final boolean hasDecreased = container.decrItem(destinationSlot);
        if(!hasDecreased)
            CustomContentPlugin.instance.getLogger().warning("An item was extracted by an hopper but wasn't decreased !!! THIS IS A DUPE EXPLOIT THAT NEEDS TO BE FIXED");
    }

    /*
       Utility Methods
     */

    private int findFirstFullSlot(Inventory inventory) {
        final ItemStack[] contents = inventory.getContents();
        for(int i = 0; i < contents.length; i++) {
            final ItemStack itemStack = contents[i];

            if(itemStack == null || itemStack.getType().equals(Material.AIR))
                continue;

            return i;
        }

        return -1;
    }

    private BlockFace getHopperFace(BlockState state) {
        switch(state.getRawData()) {
            case 2: case 10:
                return BlockFace.NORTH;
            case 3: case 11:
                return BlockFace.SOUTH;
            case 4: case 12:
                return BlockFace.WEST;
            case 5: case 13:
                return BlockFace.EAST;
            default:
                return BlockFace.DOWN;
        }
    }
}
