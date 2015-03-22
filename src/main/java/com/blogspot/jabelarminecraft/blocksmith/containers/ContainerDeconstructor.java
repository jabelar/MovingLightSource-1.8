package com.blogspot.jabelarminecraft.blocksmith.containers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.events.DeconstructingEvent;
import com.blogspot.jabelarminecraft.blocksmith.events.SuccessfulDeconstructingEvent;
import com.blogspot.jabelarminecraft.blocksmith.recipes.DeconstructingManager;

public class ContainerDeconstructor extends Container
{

    public static enum State
    {
        ERROR, READY
    }

    public InventoryCrafting deconstructIn = new InventoryCrafting(this, 1, 1);
    public InventoryDeconstructResult deconstructOut = new InventoryDeconstructResult();
    public InventoryCrafting calculInput = new InventoryCrafting(this, 1, 1);
    private final World worldObj;
    public InventoryPlayer playerInventory;
    public String result = I18n.format("deconstructing.result.ready");
    public State type = State.READY;
    public int xp = -BlockSmith.standardLevel;
    public int x = 0;
    public int y = 0;
    public int z = 0;
    private final int minLvl;
    private final int maxLvl;

    public ContainerDeconstructor(InventoryPlayer parPlayerInventory, World parWorld, int parX, int parY, int parZ, int parMinLevel, int parMaxLevel)
    {
        minLvl = parMinLevel;
        maxLvl = parMaxLevel;
        x = parX;
        y = parY;
        z = parZ;
        worldObj = parWorld;
        
        for(int outputSlotIndexX = 0; outputSlotIndexX < 3; ++outputSlotIndexX)
        {
            for(int outputSlotIndexY = 0; outputSlotIndexY < 3; ++outputSlotIndexY)
            {
                addSlotToContainer(new Slot(deconstructOut, outputSlotIndexY + outputSlotIndexX * 3, 112 + outputSlotIndexY * 18, 17 + outputSlotIndexX * 18));
            }
        }
        
        addSlotToContainer(new Slot(deconstructIn, 0, 30 + 15, 35));
        addSlotToContainer(new Slot(calculInput, 0, 15, 35));

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
    public void onCraftMatrixChanged(IInventory inventory)
    {
        if(inventory == calculInput)
        {
            if(calculInput.getStackInSlot(0) == null)
            {
                xp = 0;
                if(deconstructIn.getStackInSlot(0) == null)
                {
                    String r = I18n.format("deconstructing.result.ready");
                    result = r;
                    type = State.READY;
                    xp = -BlockSmith.standardLevel;
                }
                return;
            }
            else if(deconstructIn.getStackInSlot(0) == null)
            {
                List<ItemStack[]> list1 = DeconstructingManager.getDeconstructResults(calculInput.getStackInSlot(0));
                ItemStack[] output = null;
                if(list1.size() > 0)
                    output = list1.get(0);
                List<Integer> needs = DeconstructingManager.getStackSizeNeeded(calculInput.getStackInSlot(0));
                int required = 1;
                if(needs.size() > 0)
                {
                    required = needs.get(0);
                }
                DeconstructingEvent event = new DeconstructingEvent(calculInput.getStackInSlot(0), output, required, playerInventory.player);
                if(!MinecraftForge.EVENT_BUS.post(event))
                {
                    int nbrStacks = event.getRequiredNumber();
                    if(nbrStacks > calculInput.getStackInSlot(0).stackSize)
                    {

                        String r = I18n.format("deconstructing.result.needMoreStacks", (nbrStacks - calculInput.getStackInSlot(0).stackSize));
                        result = r;
                        type = State.ERROR;
                        xp = -minLvl;
                        return;
                    }
                    else if(event.getOutput() == null)
                    {
                        String r = I18n.format("deconstructing.result.impossible");
                        result = r;
                        type = State.ERROR;
                        xp = -minLvl;
                        return;
                    }
                    else
                    {
                        String r = I18n.format("deconstructing.result.ready");
                        result = r;
                        type = State.READY;
                    }
                    if(BlockSmith.deconstructMethod == 0)
                    {
                        xp = 0;
                    }
                    else if(BlockSmith.deconstructMethod == 1)
                    {
                        ItemStack s1 = calculInput.getStackInSlot(0);
                        int percent = (int) (((double) s1.getItemDamage() / (double) s1.getMaxDamage()) * 100);
                        xp = (maxLvl * percent) / 100;
                    }
                }
            }
            else
            {
                String r = I18n.format("deconstructing.result.impossible");
                result = r;
                type = State.ERROR;
                xp = -minLvl;
                return;
            }
        }
        else if(inventory == deconstructIn)
        {
            xp = 0;
            if(deconstructIn.getStackInSlot(0) == null)
            {
                result = I18n.format("deconstructing.result.ready");
                if(calculInput.getStackInSlot(0) == null)
                {
                    xp = 0;
                }
                type = State.READY;
                return;
            }
            List<ItemStack[]> list1 = DeconstructingManager.getDeconstructResults(deconstructIn.getStackInSlot(0));
            ItemStack[] output = null;
            if(list1.size() > 0)
                output = list1.get(0);
            List<Integer> needs = DeconstructingManager.getStackSizeNeeded(deconstructIn.getStackInSlot(0));
            int required = 1;
            if(needs.size() > 0)
            {
                required = needs.get(0);
            }
            DeconstructingEvent event = new DeconstructingEvent(deconstructIn.getStackInSlot(0), output, required, playerInventory.player);
            if(!MinecraftForge.EVENT_BUS.post(event))
            {
                int nbrStacks = event.getRequiredNumber();
                if(nbrStacks > deconstructIn.getStackInSlot(0).stackSize)
                {
                    String resultString = I18n.format("deconstructing.result.needMoreStacks", (nbrStacks - deconstructIn.getStackInSlot(0).stackSize));
                    result = resultString;
                    type = State.ERROR;
                    return;
                }
                while(deconstructIn.getStackInSlot(0) != null && nbrStacks <= deconstructIn.getStackInSlot(0).stackSize)
                {
                    EntityPlayer player = playerInventory.player;
                    int playerLevel = player.experienceLevel;
                    xp = 0;
                    if(!EnchantmentHelper.getEnchantments(deconstructIn.getStackInSlot(0)).isEmpty() && calculInput.getStackInSlot(0) != null && calculInput.getStackInSlot(0).getItem() == Items.book)
                    {
                        Map enchantsMap = EnchantmentHelper.getEnchantments(deconstructIn.getStackInSlot(0));
                        Iterator<?> i = enchantsMap.keySet().iterator();
                        Map<Integer, Integer> tmpMap = new LinkedHashMap<Integer, Integer>();
                        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
                        while(i.hasNext())
                        {
                            int id = (Integer) i.next();
                            tmpMap.put(id, (Integer) enchantsMap.get(id));
                            ItemStack stack = new ItemStack(Items.enchanted_book, 1);
                            EnchantmentHelper.setEnchantments(tmpMap, stack);
                            stacks.add(stack);
                            tmpMap.clear();
                        }
                        int nbr = calculInput.getStackInSlot(0).stackSize;
                        for(ItemStack s : stacks)
                        {
                            nbr-- ;
                            if(!playerInventory.addItemStackToInventory(s))
                            {
                                EntityItem e = playerInventory.player.entityDropItem(s, 0.5f);
                                e.posX = playerInventory.player.posX;
                                e.posY = playerInventory.player.posY;
                                e.posZ = playerInventory.player.posZ;
                            }
                            if(nbr <= 0)
                            {
                                break;
                            }
                        }
                        calculInput.setInventorySlotContents(0, null);
                    }
                    ItemStack[] items = event.getOutput();
                    if(items == null)
                    {
                        String r = I18n.format("deconstructing.result.impossible");
                        result = r;
                        type = State.ERROR;
                        return;
                    }
                    if(!playerInventory.player.capabilities.isCreativeMode && deconstructIn.getStackInSlot(0).getItem().getItemEnchantability() > 0)
                    {
                        if(BlockSmith.deconstructMethod == 0)
                        {
                            int count = 0;
                            ItemStack s1 = deconstructIn.getStackInSlot(0);

                            int percent = (int) (((double) s1.getItemDamage() / (double) s1.getMaxDamage()) * 100);
                            for(int i = 0; i < items.length; i++ )
                            {
                                if(items[i] != null)
                                    count++ ;
                            }
                            int toRemove = Math.round(percent * count / 100f);
                            if(toRemove > 0)
                                for(int i = 0; i < items.length; i++ )
                                {
                                    if(items[i] != null)
                                    {
                                        toRemove-- ;
                                        items[i] = null;
                                        if(toRemove <= 0)
                                        {
                                            break;
                                        }
                                    }
                                }
                        }
                        else if(BlockSmith.deconstructMethod == 1)
                        {
                            ItemStack inputStack = deconstructIn.getStackInSlot(0);
                            int percent = (int) (((double) inputStack.getItemDamage() / (double) inputStack.getMaxDamage()) * 100);
                            xp = (maxLvl * percent) / 100;
                        }
                    }
                    if(playerLevel < BlockSmith.standardLevel + xp && !player.capabilities.isCreativeMode)
                    {
                        String resultString = I18n.format("deconstructing.result.needMoreXP");
                        result = resultString;
                        type = State.ERROR;
                        return;
                    }
                    else if(playerLevel >= BlockSmith.standardLevel + xp && !player.capabilities.isCreativeMode)
                    {
                        player.experienceLevel -= BlockSmith.standardLevel + xp;
                    }
                    if(!deconstructOut.isEmpty())
                    {
                        for(int i = 0; i < deconstructOut.getSizeInventory(); i++ )
                        {
                            ItemStack item = deconstructOut.getStackInSlot(i);
                            if((item != null && items[i] != null && item.getItem() != items[i].getItem()))
                            {
                                if(!playerInventory.addItemStackToInventory(item))
                                {
                                    EntityItem entityItem = playerInventory.player.entityDropItem(item, 0.5f);
                                    entityItem.posX = playerInventory.player.posX;
                                    entityItem.posY = playerInventory.player.posY;
                                    entityItem.posZ = playerInventory.player.posZ;
                                }
                                deconstructOut.setInventorySlotContents(i, null);
                            }
                        }
                    }

                    for(int i = 0; i < items.length; i++ )
                    {
                        ItemStack s = items[i];
                        ItemStack currentStack = deconstructOut.getStackInSlot(i);
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
                            deconstructOut.setInventorySlotContents(i, newStack);
                        }
                        else
                        {
                            deconstructOut.setInventorySlotContents(i, null);
                        }
                    }
                    SuccessfulDeconstructingEvent sevent = new SuccessfulDeconstructingEvent(deconstructIn.getStackInSlot(0), items, event.getRequiredNumber(), playerInventory.player);
                    if(!MinecraftForge.EVENT_BUS.post(sevent))
                    {
                        event.getPlayer().addStat(BlockSmith.deconstructedItemsStat, event.getRequiredNumber());
                        event.getPlayer().triggerAchievement(BlockSmith.deconstructAny);
                    }
                    int i = deconstructIn.getStackInSlot(0).stackSize - event.getRequiredNumber();
                    ItemStack newStack = null;
                    if(i > 0)
                    {
                        newStack = new ItemStack(deconstructIn.getStackInSlot(0).getItem(), i, 0);
                    }
                    deconstructIn.setInventorySlotContents(0, newStack);
                    onCraftMatrixChanged(calculInput);
                }
            }
            else
            {
                String resultString = I18n.format("deconstructing.result.impossible");
                result = resultString;
                type = State.ERROR;
            }
        }
        else
        {
            String resultString = I18n.format("deconstructing.result.impossible");
            result = resultString;
            type = State.ERROR;
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
                if((((Slot) inventorySlots.get(parSlotId)).inventory == calculInput || ((Slot) inventorySlots.get(parSlotId)).inventory == playerInventory))
                    onCraftMatrixChanged(calculInput);
                else if(((Slot) inventorySlots.get(parSlotId)).inventory == deconstructIn)
                {
                    onCraftMatrixChanged(deconstructIn);
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
            ItemStack itemStack = deconstructIn.getStackInSlotOnClosing(0);
            if(itemStack != null)
            {
                parPlayer.entityDropItem(itemStack, 0.5f);
            }

            itemStack = calculInput.getStackInSlotOnClosing(0);
            if(itemStack != null)
            {
                parPlayer.entityDropItem(itemStack, 0.5f);
            }
            for(int i = 0; i < deconstructOut.getSizeInventory(); i++ )
            {
                itemStack = deconstructOut.getStackInSlotOnClosing(i);

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
        Slot slot = (Slot) inventorySlots.get(parSlotIndex);
        if(slot != null && slot.getHasStack())
            if(slot.inventory.equals(calculInput))
            {
                ItemStack itemstack1 = slot.getStack();
                slot.onPickupFromSlot(parPlayer, itemstack1);
                if(!playerInventory.addItemStackToInventory(itemstack1))
                {
                    return null;
                }
                slot.putStack(null);
            }
            else if(slot.inventory.equals(deconstructIn))
            {
                if(slot.getHasStack())
                {
                    if(!playerInventory.addItemStackToInventory(slot.getStack()))
                    {
                        return null;
                    }
                    slot.putStack(null);
                    slot.onSlotChanged();
                }
            }
            else if(slot.inventory.equals(playerInventory))
            {
                Slot calcInput = null;
                Slot deconstructSlot = null;
                for(Object s : inventorySlots)
                {
                    Slot s1 = (Slot) s;
                    if(s1.inventory.equals(calculInput))
                    {
                        calcInput = s1;
                    }
                    else if(s1.inventory.equals(deconstructIn))
                    {
                        deconstructSlot = s1;
                    }
                }
                if(calcInput != null)
                {
                    if(calcInput.getStack() == null)
                    {
                        calcInput.putStack(slot.getStack());
                        calcInput.onSlotChanged();
                        slot.putStack(null);
                    }
                    else
                    {
                        if(slot.getStack() != null)
                        {
                            ItemStack i = slot.getStack();
                            slot.onPickupFromSlot(parPlayer, slot.getStack());
                            slot.putStack(calcInput.getStack().copy());
                            calcInput.putStack(i.copy());
                            onCraftMatrixChanged(calculInput);
                            calcInput.onSlotChanged();
                        }
                        else
                        {
                            return null;
                        }
                    }
                }
            }
            else if(slot.inventory.equals(deconstructOut))
            {
                if(slot.getHasStack())
                {
                    if(!playerInventory.addItemStackToInventory(slot.getStack()))
                    {
                        return null;
                    }
                    slot.putStack(null);
                    slot.onSlotChanged();
                }
            }
        return null;
    }

    @Override
	public boolean func_94530_a(ItemStack parItemStack, Slot parSlot)
    {
        return !parSlot.inventory.equals(deconstructOut);
    }

    @Override
	public Slot getSlot(int parSlotIndex)
    {
        if(parSlotIndex >= inventorySlots.size())
            parSlotIndex = inventorySlots.size() - 1;
        return super.getSlot(parSlotIndex);
    }

}
