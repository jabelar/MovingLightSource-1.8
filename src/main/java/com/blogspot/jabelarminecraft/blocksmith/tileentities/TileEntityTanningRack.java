/**
    Copyright (C) 2015 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.blocksmith.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockTanningRack;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerTanningRack;
import com.blogspot.jabelarminecraft.blocksmith.recipes.TanningRackRecipes;

/**
 * @author jabelar
 *
 */
public class TileEntityTanningRack extends TileEntityLockable implements IUpdatePlayerListBox, ISidedInventory
{
    // enumerate the slots
    public enum slotEnum 
    {
        INPUT_SLOT, OUTPUT_SLOT
    }
    private static final int[] slotsTop = new int[] {slotEnum.INPUT_SLOT.ordinal()};
    private static final int[] slotsBottom = new int[] {slotEnum.OUTPUT_SLOT.ordinal()};
    private static final int[] slotsSides = new int[] {};
    /** The ItemStacks that hold the items currently being used in the tanningRack */
    private ItemStack[] tanningRackItemStackArray = new ItemStack[2];
    /** The number of ticks that the tanningRack will keep tanning */
    private int timeCanGrind;
    /** The number of ticks that a fresh copy of the currently-tanning item would keep the tanningRack tanning for */
    private int currentItemGrindTime;
    private int ticksTanningItemSoFar;
    private int ticksPerItem;
    private String tanningRackCustomName;

    /**
     * This controls whether the tile entity gets replaced whenever the block state is changed.
     * Normally only want this when block actually is replaced.
     */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
	    return (oldState.getBlock() != newSate.getBlock());
	}
    
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return tanningRackItemStackArray.length;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
	public ItemStack getStackInSlot(int index)
    {
        return tanningRackItemStackArray[index];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        if (tanningRackItemStackArray[index] != null)
        {
            ItemStack itemstack;

            if (tanningRackItemStackArray[index].stackSize <= count)
            {
                itemstack = tanningRackItemStackArray[index];
                tanningRackItemStackArray[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = tanningRackItemStackArray[index].splitStack(count);

                if (tanningRackItemStackArray[index].stackSize == 0)
                {
                    tanningRackItemStackArray[index] = null;
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
        if (tanningRackItemStackArray[index] != null)
        {
            ItemStack itemstack = tanningRackItemStackArray[index];
            tanningRackItemStackArray[index] = null;
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
    	// DEBUG
    	System.out.println("TileEntityTanningRack setInventorySlotContents()");
    	
        boolean isSameItemStackAlreadyInSlot = stack != null && stack.isItemEqual(tanningRackItemStackArray[index]) && ItemStack.areItemStackTagsEqual(stack, tanningRackItemStackArray[index]);
        tanningRackItemStackArray[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }

        // if input slot, reset the tanning timers
        if (index == slotEnum.INPUT_SLOT.ordinal() && !isSameItemStackAlreadyInSlot)
        {
            ticksPerItem = timeToGrindOneItem(stack);
            ticksTanningItemSoFar = 0;
            markDirty();
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
	public String getCommandSenderName()
    {
        return hasCustomName() ? tanningRackCustomName : "container.tanningRack";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
	public boolean hasCustomName()
    {
        return tanningRackCustomName != null && tanningRackCustomName.length() > 0;
    }

    public void setCustomInventoryName(String parCustomName)
    {
        tanningRackCustomName = parCustomName;
    }

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        tanningRackItemStackArray = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbtTagCompound.getByte("Slot");

            if (b0 >= 0 && b0 < tanningRackItemStackArray.length)
            {
                tanningRackItemStackArray[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
            }
        }

        timeCanGrind = compound.getShort("GrindTime");
        ticksTanningItemSoFar = compound.getShort("CookTime");
        ticksPerItem = compound.getShort("CookTimeTotal");

        if (compound.hasKey("CustomName", 8))
        {
            tanningRackCustomName = compound.getString("CustomName");
        }
    }

    @Override
	public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("GrindTime", (short)timeCanGrind);
        compound.setShort("CookTime", (short)ticksTanningItemSoFar);
        compound.setShort("CookTimeTotal", (short)ticksPerItem);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < tanningRackItemStackArray.length; ++i)
        {
            if (tanningRackItemStackArray[i] != null)
            {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte)i);
                tanningRackItemStackArray[i].writeToNBT(nbtTagCompound);
                nbttaglist.appendTag(nbtTagCompound);
            }
        }

        compound.setTag("Items", nbttaglist);

        if (hasCustomName())
        {
            compound.setString("CustomName", tanningRackCustomName);
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
     * TanningRack is tanning
     */
    public boolean tanningSomething()
    {
        return true;
    }

    // this function indicates whether container texture should be drawn
    @SideOnly(Side.CLIENT)
    public static boolean func_174903_a(IInventory parIInventory)
    {
        return true ; // parIInventory.getField(0) > 0;
    }

    @Override
	public void update()
    {
        boolean hasBeenTanning = tanningSomething();
        boolean changedTanningState = false;

        if (tanningSomething())
        {
            --timeCanGrind;
        }

        if (!worldObj.isRemote)
        {
        	// if something in input slot
            if (tanningRackItemStackArray[slotEnum.INPUT_SLOT.ordinal()] != null)
            {            	
             	// start tanning
                if (!tanningSomething() && canGrind())
                {
	            	// DEBUG
	            	System.out.println("TileEntityTanningRack update() started tanning");
	            	
	                timeCanGrind = 150;
	
	                 if (tanningSomething())
	                 {
	                     changedTanningState = true;
	                 }
                }

                // continue tanning
                if (tanningSomething() && canGrind())
                {
//	            	// DEBUG
//	            	System.out.println("TileEntityTanningRack update() continuing tanning");
	            	
                    ++ticksTanningItemSoFar;
                    
                    // check if completed tanning an item
                    if (ticksTanningItemSoFar == ticksPerItem)
                    {
                    	// DEBUG
                    	System.out.println("Tanning completed another output cycle");
                    	
                        ticksTanningItemSoFar = 0;
                        ticksPerItem = timeToGrindOneItem(tanningRackItemStackArray[0]);
                        tanItem();
                        changedTanningState = true;
                    }
                }
                else
                {
                    ticksTanningItemSoFar = 0;
                }
            }

            // started or stopped tanning, update block to change to active or inactive model
            if (hasBeenTanning != tanningSomething()) // the isTanning() value may have changed due to call to tanItem() earlier
            {
            	// DEBUG
            	System.out.println("Changed tanning state");
                changedTanningState = true;
            }
            
            // if leather result is in output slot display it
            if (tanningRackItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()] != null)
            {
                 BlockTanningRack.changeBlockBasedOnTanningStatus(6, worldObj, pos);
            }
            else // display what is in input slot
            {
            	if (tanningRackItemStackArray[slotEnum.INPUT_SLOT.ordinal()] != null)
	            {
            		Item inputItem = tanningRackItemStackArray[slotEnum.INPUT_SLOT.ordinal()].getItem();
            		
	            	if (inputItem == BlockSmith.cowHide)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(1, worldObj, pos);
	            	}
	            	else if (inputItem == BlockSmith.sheepSkin)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(2, worldObj, pos);
	            	}
	            	else if (inputItem == BlockSmith.pigSkin)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(3, worldObj, pos);
	            	}
	            	else if (inputItem == BlockSmith.horseHide)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(4, worldObj, pos);
	            	}
	            	else if (inputItem == Items.rabbit_hide)
	            	{
	                	BlockTanningRack.changeBlockBasedOnTanningStatus(5, worldObj, pos);
	            	}
            	}
            	else
            	{
                	BlockTanningRack.changeBlockBasedOnTanningStatus(0, worldObj, pos);
            	}
            }
        }

        if (changedTanningState)
        {
            markDirty();
        }
    }

    public int timeToGrindOneItem(ItemStack parItemStack)
    {
        return 200;
    }

    /**
     * Returns true if the tanningRack can tan an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canGrind()
    {
    	// if nothing in input slot
        if (tanningRackItemStackArray[slotEnum.INPUT_SLOT.ordinal()] == null)
        {
            return false;
        }
        else // check if it has a tanning recipe
        {
            ItemStack itemStackToOutput = TanningRackRecipes.instance().getTanningResult(tanningRackItemStackArray[slotEnum.INPUT_SLOT.ordinal()]);
            if (itemStackToOutput == null) return false; // no valid recipe for tanning this item
            if (tanningRackItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()] == null) return true; // output slot is empty
            if (!tanningRackItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()].isItemEqual(itemStackToOutput)) return false; // output slot has different item occupying it
            // check if output slot is full
            int result = tanningRackItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()].stackSize + itemStackToOutput.stackSize;
            return result <= getInventoryStackLimit() && result <= tanningRackItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()].getMaxStackSize();
        }
    }

    /**
     * Turn one item from the tanningRack source stack into the appropriate taned item in the tanningRack result stack
     */
    public void tanItem()
    {
        if (canGrind())
        {
            ItemStack itemstack = TanningRackRecipes.instance().getTanningResult(tanningRackItemStackArray[slotEnum.INPUT_SLOT.ordinal()]);

            // check if output slot is empty
            if (tanningRackItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()] == null)
            {
                tanningRackItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()] = itemstack.copy();
            }
            else if (tanningRackItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()].getItem() == itemstack.getItem())
            {
                tanningRackItemStackArray[slotEnum.OUTPUT_SLOT.ordinal()].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --tanningRackItemStackArray[slotEnum.INPUT_SLOT.ordinal()].stackSize;

            if (tanningRackItemStackArray[slotEnum.INPUT_SLOT.ordinal()].stackSize <= 0)
            {
                tanningRackItemStackArray[slotEnum.INPUT_SLOT.ordinal()] = null;
            }
        }
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

    @Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index == slotEnum.INPUT_SLOT.ordinal() ? true : false; // can always put things in input (may not tan though) and can't put anything in output
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
	public boolean canExtractItem(int parSlotIndex, ItemStack parStack, EnumFacing parFacing)
    {
        return true;
    }

    @Override
	public String getGuiID()
    {
        return "blocksmith:tanningRack";
    }

    @Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
    	// DEBUG
    	System.out.println("TileEntityTanningRack createContainer()");
        return new ContainerTanningRack(playerInventory, this);
    }

    @Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return timeCanGrind;
            case 1:
                return currentItemGrindTime;
            case 2:
                return ticksTanningItemSoFar;
            case 3:
                return ticksPerItem;
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
                timeCanGrind = value;
                break;
            case 1:
                currentItemGrindTime = value;
                break;
            case 2:
                ticksTanningItemSoFar = value;
                break;
            case 3:
                ticksPerItem = value;
                break;
		default:
			break;
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
        for (int i = 0; i < tanningRackItemStackArray.length; ++i)
        {
            tanningRackItemStackArray[i] = null;
        }
    }
}