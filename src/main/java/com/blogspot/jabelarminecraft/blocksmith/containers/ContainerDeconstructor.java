package com.blogspot.jabelarminecraft.blocksmith.containers;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.recipes.DeconstructingRecipeHandler;

public class ContainerDeconstructor extends Container
{

    public static enum State
    {
        ERROR, READY
    }

    public InventoryCrafting inputInventory = new InventoryCrafting(this, 1, 1);
    public int inputSlotNumber;
    public InventoryDeconstructResult outputInventory = new InventoryDeconstructResult();
    private final World worldObj;
    public InventoryPlayer playerInventory;
    public String resultString = I18n.format("deconstructing.result.ready");
    public State deconstructingState = State.READY;
    public int x = 0;
    public int y = 0;
    public int z = 0;

    public ContainerDeconstructor(InventoryPlayer parPlayerInventory, World parWorld, int parX, int parY, int parZ)
    {
        x = parX;
        y = parY;
        z = parZ;
        worldObj = parWorld;
        
        for(int outputSlotIndexX = 0; outputSlotIndexX < 3; ++outputSlotIndexX)
        {
            for(int outputSlotIndexY = 0; outputSlotIndexY < 3; ++outputSlotIndexY)
            {
                addSlotToContainer(new Slot(outputInventory, outputSlotIndexY + outputSlotIndexX * 3, 112 + outputSlotIndexY * 18, 17 + outputSlotIndexX * 18));
            }
        }
        
        inputSlotNumber = addSlotToContainer(new Slot(inputInventory, 0, 30 + 15, 35)).slotNumber;

        for(int playerSlotIndexY = 0; playerSlotIndexY < 3; ++playerSlotIndexY)
        {
            for(int playerSlotIndexX = 0; playerSlotIndexX < 9; ++playerSlotIndexX)
            {
                addSlotToContainer(new Slot(parPlayerInventory, playerSlotIndexX + playerSlotIndexY * 9 + 9, 8 + playerSlotIndexX * 18, 84 + playerSlotIndexY * 18));
            }
        }
        
        for(int hotbarSlotIndex = 0; hotbarSlotIndex < 9; ++hotbarSlotIndex)
        {
            addSlotToContainer(new Slot(parPlayerInventory, hotbarSlotIndex, 8 + hotbarSlotIndex * 18, 142));
        }     
        
        playerInventory = parPlayerInventory;
    }

    @Override
    public void onCraftMatrixChanged(IInventory parInventory)
    {
    	if(parInventory == inputInventory)
        {
            if(inputInventory.getStackInSlot(0) == null)
            {
                resultString = I18n.format("deconstructing.result.ready");
                deconstructingState = State.READY;
                return;
            }
            ItemStack[] outputItemStackArray = DeconstructingRecipeHandler.getDeconstructResults(inputInventory.getStackInSlot(0));
            int amountRequired = DeconstructingRecipeHandler.getStackSizeNeeded(inputInventory.getStackInSlot(0));
            // DEBUG
            System.out.println("Amount required = "+amountRequired);
            if(amountRequired > inputInventory.getStackInSlot(0).stackSize)
            {
                resultString = I18n.format("deconstructing.result.needMoreStacks", (amountRequired - inputInventory.getStackInSlot(0).stackSize));
                deconstructingState = State.ERROR;
                return;
            }
            while(inputInventory.getStackInSlot(0) != null && amountRequired <= inputInventory.getStackInSlot(0).stackSize)
            {              
                if(outputItemStackArray == null)
                {
                    String r = I18n.format("deconstructing.result.impossible");
                    resultString = r;
                    deconstructingState = State.ERROR;
                    return;
                }
                if(!playerInventory.player.capabilities.isCreativeMode && inputInventory.getStackInSlot(0).getItem().getItemEnchantability() > 0)
                {
                    int count = 0;
                    ItemStack s1 = inputInventory.getStackInSlot(0);

                    int percent = (int) (((double) s1.getItemDamage() / (double) s1.getMaxDamage()) * 100);
                    for(int i = 0; i < outputItemStackArray.length; i++ )
                    {
                        if(outputItemStackArray[i] != null)
                            count++ ;
                    }
                    int toRemove = Math.round(percent * count / 100f);
                    if(toRemove > 0)
                    {
                        for(int i = 0; i < outputItemStackArray.length; i++ )
                        {
                            if(outputItemStackArray[i] != null)
                            {
                                toRemove-- ;
                                outputItemStackArray[i] = null;
                                if(toRemove <= 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }
                
                if(!outputInventory.isEmpty())
                {
                    for(int i = 0; i < outputInventory.getSizeInventory(); i++ )
                    {
                        ItemStack itemStackInOutputSlot = outputInventory.getStackInSlot(i);
                        if((itemStackInOutputSlot != null && outputItemStackArray[i] != null && itemStackInOutputSlot.getItem() != outputItemStackArray[i].getItem()))
                        {
                            if(!playerInventory.addItemStackToInventory(itemStackInOutputSlot))
                            {
                                EntityItem entityItem = playerInventory.player.entityDropItem(itemStackInOutputSlot, 0.5f);
                                entityItem.posX = playerInventory.player.posX;
                                entityItem.posY = playerInventory.player.posY;
                                entityItem.posZ = playerInventory.player.posZ;
                            }
                            outputInventory.setInventorySlotContents(i, null);
                        }
                    }
                }

                for(int i = 0; i < outputItemStackArray.length; i++ )
                {
                    ItemStack s = outputItemStackArray[i];
                    ItemStack currentStack = outputInventory.getStackInSlot(i);
                    if(s != null)
                    {
                        int metadata = s.getItemDamage();
                        if(metadata == 32767)
                        {
                            metadata = 0;
                        }
                        ItemStack newStack = null;
                        if(currentStack != null && 1 + currentStack.stackSize <= s.getMaxStackSize())
                        {
                            newStack = new ItemStack(s.getItem(), 1 + currentStack.stackSize, metadata);
                        }
                        else
                        {
                            if(currentStack != null && !playerInventory.addItemStackToInventory(currentStack))
                            {
                                EntityItem entityItem = playerInventory.player.entityDropItem(currentStack, 0.5f);
                                entityItem.posX = playerInventory.player.posX;
                                entityItem.posY = playerInventory.player.posY;
                                entityItem.posZ = playerInventory.player.posZ;
                            }
                            newStack = new ItemStack(s.getItem(), 1, metadata);
                        }
                        outputInventory.setInventorySlotContents(i, newStack);
                    }
                    else
                    {
                        outputInventory.setInventorySlotContents(i, null);
                    }
                }
                playerInventory.player.addStat(BlockSmith.deconstructedItemsStat, amountRequired);
                playerInventory.player.triggerAchievement(BlockSmith.deconstructAny);
                
                int i = inputInventory.getStackInSlot(0).stackSize - amountRequired;
                ItemStack newStack = null;
                if(i > 0)
                {
                    newStack = new ItemStack(inputInventory.getStackInSlot(0).getItem(), i, 0);
                }
                inputInventory.setInventorySlotContents(0, newStack);
            }
        }
        else
        {
            resultString = I18n.format("deconstructing.result.impossible");
            deconstructingState = State.ERROR;
        }
    }

    @Override
	public ItemStack slotClick(int parSlotId, int parMouseButtonId, int parClickMode, EntityPlayer parPlayer)
    {
        ItemStack clickItemStack = super.slotClick(parSlotId, parMouseButtonId, parClickMode, parPlayer);
        if(inventorySlots.size() > parSlotId && parSlotId >= 0)
        {
            if(inventorySlots.get(parSlotId) != null)
            {
            	if(((Slot) inventorySlots.get(parSlotId)).inventory == inputInventory)
                {
                    onCraftMatrixChanged(inputInventory);
                }
            }
        }
        return clickItemStack;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
	public void onContainerClosed(EntityPlayer parPlayer)
    {
        if(playerInventory.getItemStack() != null)
        {
            parPlayer.entityDropItem(playerInventory.getItemStack(), 0.5f);
        }
        if(!worldObj.isRemote)
        {
            ItemStack itemStack = inputInventory.getStackInSlotOnClosing(0);
            if(itemStack != null)
            {
                parPlayer.entityDropItem(itemStack, 0.5f);
            }

            for(int i = 0; i < outputInventory.getSizeInventory(); i++ )
            {
                itemStack = outputInventory.getStackInSlotOnClosing(i);

                if(itemStack != null)
                {
                    parPlayer.entityDropItem(itemStack, 0.5f);
                }
            }
        }
    }

    @Override
	public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    /**
     * Called when a player shift-clicks on a slot.
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer parPlayer, int parSlotIndex)
    {
    	// DEBUG
    	System.out.println("Shift-clicked on a slot");
        Slot slot = (Slot) inventorySlots.get(parSlotIndex);
        // If there is something in the stack to pick up
        if (slot != null && slot.getHasStack())
        {
        	// If the slot is one of the custom slots
        	if (slot.inventory.equals(inputInventory) || slot.inventory.equals(outputInventory))
            {
        		// try to move to player inventory
                if (!playerInventory.addItemStackToInventory(slot.getStack()))
                {
                    return null;
                }
                slot.putStack(null);
                slot.onSlotChanged();
            }
         	// if the slot is a player inventory slot
            else if(slot.inventory.equals(playerInventory))
            {
            	// DEBUG
            	System.out.println("Shift-clicked on player inventory slot");
            	// Try to transfer to input slot
            	if (!((Slot)inventorySlots.get(inputSlotNumber)).getHasStack())
            	{
            		((Slot)inventorySlots.get(inputSlotNumber)).putStack(slot.getStack());
                    slot.putStack(null);
                    slot.onSlotChanged();
            	}
            	else
            	{
            		// DEBUG
            		System.out.println("There is already something in the input slot");
            	}
            }
        }
        return null;
    }

    @Override
	public boolean func_94530_a(ItemStack parItemStack, Slot parSlot)
    {
        return !parSlot.inventory.equals(outputInventory);
    }

    @Override
	public Slot getSlot(int parSlotIndex)
    {
        if(parSlotIndex >= inventorySlots.size())
            parSlotIndex = inventorySlots.size() - 1;
        return super.getSlot(parSlotIndex);
    }

}
