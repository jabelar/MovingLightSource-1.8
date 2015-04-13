/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockForge;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerForge;
import com.blogspot.jabelarminecraft.blocksmith.recipes.ForgeRecipes;
import com.blogspot.jabelarminecraft.blocksmith.slots.SlotForgeFuel;

/**
 * @author agilroy
 *
 */
public class TileEntityForge extends TileEntityLockable implements IUpdatePlayerListBox, ISidedInventory
{
    private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};
    /** The ItemStacks that hold the items currently being used in the forge */
    private ItemStack[] forgeItemStacks = new ItemStack[3];
    /** The number of ticks that the forge will keep burning */
    private int forgeBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the forge burning for */
    private int currentItemBurnTime;
    private int field_174906_k;
    private int field_174905_l;
    private String forgeCustomName;

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return forgeItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
	public ItemStack getStackInSlot(int index)
    {
        return forgeItemStacks[index];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        if (forgeItemStacks[index] != null)
        {
            ItemStack itemstack;

            if (forgeItemStacks[index].stackSize <= count)
            {
                itemstack = forgeItemStacks[index];
                forgeItemStacks[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = forgeItemStacks[index].splitStack(count);

                if (forgeItemStacks[index].stackSize == 0)
                {
                    forgeItemStacks[index] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
	public ItemStack getStackInSlotOnClosing(int index)
    {
        if (forgeItemStacks[index] != null)
        {
            ItemStack itemstack = forgeItemStacks[index];
            forgeItemStacks[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
        boolean flag = stack != null && stack.isItemEqual(forgeItemStacks[index]) && ItemStack.areItemStackTagsEqual(stack, forgeItemStacks[index]);
        forgeItemStacks[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }

        if (index == 0 && !flag)
        {
            field_174905_l = func_174904_a(stack);
            field_174906_k = 0;
            markDirty();
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
	public String getCommandSenderName()
    {
        return hasCustomName() ? forgeCustomName : "container.forge";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
	public boolean hasCustomName()
    {
        return forgeCustomName != null && forgeCustomName.length() > 0;
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        forgeCustomName = p_145951_1_;
    }

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        forgeItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < forgeItemStacks.length)
            {
                forgeItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        forgeBurnTime = compound.getShort("BurnTime");
        field_174906_k = compound.getShort("CookTime");
        field_174905_l = compound.getShort("CookTimeTotal");
        currentItemBurnTime = getItemBurnTime(forgeItemStacks[1]);

        if (compound.hasKey("CustomName", 8))
        {
            forgeCustomName = compound.getString("CustomName");
        }
    }

    @Override
	public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("BurnTime", (short)forgeBurnTime);
        compound.setShort("CookTime", (short)field_174906_k);
        compound.setShort("CookTimeTotal", (short)field_174905_l);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < forgeItemStacks.length; ++i)
        {
            if (forgeItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                forgeItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        compound.setTag("Items", nbttaglist);

        if (hasCustomName())
        {
            compound.setString("CustomName", forgeCustomName);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    @Override
	public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Forge isBurning
     */
    public boolean isBurning()
    {
        return forgeBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean func_174903_a(IInventory p_174903_0_)
    {
        return p_174903_0_.getField(0) > 0;
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
	public void update()
    {
        boolean flag = isBurning();
        boolean flag1 = false;

        if (isBurning())
        {
            --forgeBurnTime;
        }

        if (!worldObj.isRemote)
        {
            if (!isBurning() && (forgeItemStacks[1] == null || forgeItemStacks[0] == null))
            {
                if (!isBurning() && field_174906_k > 0)
                {
                    field_174906_k = MathHelper.clamp_int(field_174906_k - 2, 0, field_174905_l);
                }
            }
            else
            {
                if (!isBurning() && canSmelt())
                {
                    currentItemBurnTime = forgeBurnTime = getItemBurnTime(forgeItemStacks[1]);

                    if (isBurning())
                    {
                        flag1 = true;

                        if (forgeItemStacks[1] != null)
                        {
                            --forgeItemStacks[1].stackSize;

                            if (forgeItemStacks[1].stackSize == 0)
                            {
                                forgeItemStacks[1] = forgeItemStacks[1].getItem().getContainerItem(forgeItemStacks[1]);
                            }
                        }
                    }
                }

                if (isBurning() && canSmelt())
                {
                    ++field_174906_k;

                    if (field_174906_k == field_174905_l)
                    {
                        field_174906_k = 0;
                        field_174905_l = func_174904_a(forgeItemStacks[0]);
                        smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    field_174906_k = 0;
                }
            }

            if (flag != isBurning())
            {
                flag1 = true;
                BlockForge.changeBlockState(isBurning(), worldObj, pos);
            }
        }

        if (flag1)
        {
            markDirty();
        }
    }

    public int func_174904_a(ItemStack p_174904_1_)
    {
        return 200;
    }

    /**
     * Returns true if the forge can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (forgeItemStacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = ForgeRecipes.instance().getSmeltingResult(forgeItemStacks[0]);
            if (itemstack == null) return false;
            if (forgeItemStacks[2] == null) return true;
            if (!forgeItemStacks[2].isItemEqual(itemstack)) return false;
            int result = forgeItemStacks[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= forgeItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    /**
     * Turn one item from the forge source stack into the appropriate smelted item in the forge result stack
     */
    public void smeltItem()
    {
        if (canSmelt())
        {
            ItemStack itemstack = ForgeRecipes.instance().getSmeltingResult(forgeItemStacks[0]);

            if (forgeItemStacks[2] == null)
            {
                forgeItemStacks[2] = itemstack.copy();
            }
            else if (forgeItemStacks[2].getItem() == itemstack.getItem())
            {
                forgeItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            if (forgeItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge) && forgeItemStacks[0].getMetadata() == 1 && forgeItemStacks[1] != null && forgeItemStacks[1].getItem() == Items.bucket)
            {
                forgeItemStacks[1] = new ItemStack(Items.water_bucket);
            }

            --forgeItemStacks[0].stackSize;

            if (forgeItemStacks[0].stackSize <= 0)
            {
                forgeItemStacks[0] = null;
            }
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the forge burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack p_145952_0_)
    {
        if (p_145952_0_ == null)
        {
            return 0;
        }
        else
        {
            Item item = p_145952_0_.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }

                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return net.minecraftforge.fml.common.registry.GameRegistry.getFuelValue(p_145952_0_);
        }
    }

    public static boolean isItemFuel(ItemStack p_145954_0_)
    {
        /**
         * Returns the number of ticks that the supplied fuel item will keep the forge burning, or 0 if the item isn't
         * fuel
         */
        return getItemBurnTime(p_145954_0_) > 0;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
	public boolean isUseableByPlayer(EntityPlayer playerIn)
    {
        return worldObj.getTileEntity(pos) != this ? false : playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
	public void openInventory(EntityPlayer playerIn) {}

    @Override
	public void closeInventory(EntityPlayer playerIn) {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index == 2 ? false : (index != 1 ? true : isItemFuel(stack) || SlotForgeFuel.isItemBucket(stack));
    }

    @Override
	public int[] getSlotsForFace(EnumFacing side)
    {
        return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    @Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    @Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.water_bucket && item != Items.bucket)
            {
                return false;
            }
        }

        return true;
    }

    @Override
	public String getGuiID()
    {
        return "minecraft:forge";
    }

    @Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerForge(playerInventory, this);
    }

    @Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return forgeBurnTime;
            case 1:
                return currentItemBurnTime;
            case 2:
                return field_174906_k;
            case 3:
                return field_174905_l;
            default:
                return 0;
        }
    }

    @Override
	public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                forgeBurnTime = value;
                break;
            case 1:
                currentItemBurnTime = value;
                break;
            case 2:
                field_174906_k = value;
                break;
            case 3:
                field_174905_l = value;
        }
    }

    @Override
	public int getFieldCount()
    {
        return 4;
    }

    @Override
	public void clear()
    {
        for (int i = 0; i < forgeItemStacks.length; ++i)
        {
            forgeItemStacks[i] = null;
        }
    }
    
    /**
     * This controls whether the tile entity gets replaced whenever the block state is changed.
     * Normally only want this when block actually is replaced.
     */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
	    return (oldState.getBlock() != newSate.getBlock());
	}
}