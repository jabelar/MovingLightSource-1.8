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

package com.blogspot.jabelarminecraft.blocksmith.gui;

import java.io.IOException;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerGrinder;

/**
 * @author jabelar
 *
 */
public class GuiGrinder  extends GuiContainer
{
	private static final ResourceLocation grinderGuiTextures = new ResourceLocation(BlockSmith.MODID+":textures/gui/container/grinder.png");
    private final InventoryPlayer inventoryPlayer;
    private final IInventory tileGrinder;

    public GuiGrinder(InventoryPlayer parInventoryPlayer, IInventory parInventoryGrinder)
    {
        super(new ContainerGrinder(parInventoryPlayer, parInventoryGrinder));
        inventoryPlayer = parInventoryPlayer;
        tileGrinder = parInventoryGrinder;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = tileGrinder.getDisplayName().getUnformattedText();
        fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        fontRendererObj.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    @Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(grinderGuiTextures);
        int marginHorizontal = (width - xSize) / 2;
        int marginVertical = (height - ySize) / 2;
        drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);

//        // Draw fuel level indicator
//        if (TileEntityGrinder.func_174903_a(tileGrinder))
//        {
//            int fuelLevel = getFuelLevel(13);
//            drawTexturedModalRect(marginHorizontal + 56, marginVertical + 36 + 12 - fuelLevel, 176, 12 - fuelLevel, 14, fuelLevel + 1);
//        }

        // Draw progress indicator
        int progressLevel = getProgressLevel(24);
        drawTexturedModalRect(marginHorizontal + 79, marginVertical + 34, 176, 14, progressLevel + 1, 16);
    }

    private int getProgressLevel(int progressIndicatorPixelWidth)
    {
        int ticksGrindingItemSoFar = tileGrinder.getField(2); 
        int ticksPerItem = tileGrinder.getField(3);
        return ticksPerItem != 0 && ticksGrindingItemSoFar != 0 ? ticksGrindingItemSoFar * progressIndicatorPixelWidth / ticksPerItem : 0;
    }

    private int getFuelLevel(int fuelIndicatorPixelHeight)
    {
        int currentItemGrindTime = tileGrinder.getField(1); // this is currentItemGrindTime

        if (currentItemGrindTime == 0)
        {
            currentItemGrindTime = 200;
        }

        return tileGrinder.getField(0) * fuelIndicatorPixelHeight / currentItemGrindTime;
    }
    

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
    	// DEBUG
    	System.out.println("GuiGrinder mouseClicked() at "+mouseX+", "+mouseY);
    	
        super.mouseClicked(mouseX, mouseY, mouseButton);
//        boolean flag = mouseButton == mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
//        Slot slot = getSlotAtPosition(mouseX, mouseY);
//        long l = Minecraft.getSystemTime();
//        doubleClick = lastClickSlot == slot && l - lastClickTime < 250L && lastClickButton == mouseButton;
//        ignoreMouseUp = false;
//
//        if (mouseButton == 0 || mouseButton == 1 || flag)
//        {
//            int i1 = guiLeft;
//            int j1 = guiTop;
//            boolean flag1 = mouseX < i1 || mouseY < j1 || mouseX >= i1 + xSize || mouseY >= j1 + ySize;
//            int k1 = -1;
//
//            if (slot != null)
//            {
//                k1 = slot.slotNumber;
//            }
//
//            if (flag1)
//            {
//                k1 = -999;
//            }
//
//            if (mc.gameSettings.touchscreen && flag1 && mc.thePlayer.inventory.getItemStack() == null)
//            {
//                mc.displayGuiScreen((GuiScreen)null);
//                return;
//            }
//
//            if (k1 != -1)
//            {
//                if (mc.gameSettings.touchscreen)
//                {
//                    if (slot != null && slot.getHasStack())
//                    {
//                        clickedSlot = slot;
//                        draggedStack = null;
//                        isRightMouseClick = mouseButton == 1;
//                    }
//                    else
//                    {
//                        clickedSlot = null;
//                    }
//                }
//                else if (!dragSplitting)
//                {
//                    if (mc.thePlayer.inventory.getItemStack() == null)
//                    {
//                        if (mouseButton == mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
//                        {
//                            handleMouseClick(slot, k1, mouseButton, 3);
//                        }
//                        else
//                        {
//                            boolean flag2 = k1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
//                            byte b0 = 0;
//
//                            if (flag2)
//                            {
//                                shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack() : null;
//                                b0 = 1;
//                            }
//                            else if (k1 == -999)
//                            {
//                                b0 = 4;
//                            }
//
//                            handleMouseClick(slot, k1, mouseButton, b0);
//                        }
//
//                        ignoreMouseUp = true;
//                    }
//                    else
//                    {
//                        dragSplitting = true;
//                        dragSplittingButton = mouseButton;
//                        dragSplittingSlots.clear();
//
//                        if (mouseButton == 0)
//                        {
//                            dragSplittingLimit = 0;
//                        }
//                        else if (mouseButton == 1)
//                        {
//                            dragSplittingLimit = 1;
//                        }
//                        else if (mouseButton == mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
//                        {
//                            dragSplittingLimit = 2;
//                        }
//                    }
//                }
//            }
//        }
//
//        lastClickSlot = slot;
//        lastClickTime = l;
//        lastClickButton = mouseButton;
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    @Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
    	// DEBUG
    	System.out.println("GuiGrinder mouseClickMove()");
    	
    	super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
//        Slot slot = getSlotAtPosition(mouseX, mouseY);
//        ItemStack itemstack = mc.thePlayer.inventory.getItemStack();
//
//        if (clickedSlot != null && mc.gameSettings.touchscreen)
//        {
//            if (clickedMouseButton == 0 || clickedMouseButton == 1)
//            {
//                if (draggedStack == null)
//                {
//                    if (slot != clickedSlot)
//                    {
//                        draggedStack = clickedSlot.getStack().copy();
//                    }
//                }
//                else if (draggedStack.stackSize > 1 && slot != null && Container.canAddItemToSlot(slot, draggedStack, false))
//                {
//                    long i1 = Minecraft.getSystemTime();
//
//                    if (currentDragTargetSlot == slot)
//                    {
//                        if (i1 - dragItemDropDelay > 500L)
//                        {
//                            handleMouseClick(clickedSlot, clickedSlot.slotNumber, 0, 0);
//                            handleMouseClick(slot, slot.slotNumber, 1, 0);
//                            handleMouseClick(clickedSlot, clickedSlot.slotNumber, 0, 0);
//                            dragItemDropDelay = i1 + 750L;
//                            --draggedStack.stackSize;
//                        }
//                    }
//                    else
//                    {
//                        currentDragTargetSlot = slot;
//                        dragItemDropDelay = i1;
//                    }
//                }
//            }
//        }
//        else if (dragSplitting && slot != null && itemstack != null && itemstack.stackSize > dragSplittingSlots.size() && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && inventorySlots.canDragIntoSlot(slot))
//        {
//            dragSplittingSlots.add(slot);
//            updateDragSplitting();
//        }
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    @Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
    {
    	// DEBUG
    	System.out.println("GuiGrinder mouseReleased()");
    	
        super.mouseReleased(mouseX, mouseY, state); //Forge, Call parent to release buttons
//        Slot slot = getSlotAtPosition(mouseX, mouseY);
//        int l = guiLeft;
//        int i1 = guiTop;
//        boolean flag = mouseX < l || mouseY < i1 || mouseX >= l + xSize || mouseY >= i1 + ySize;
//        int j1 = -1;
//
//        if (slot != null)
//        {
//            j1 = slot.slotNumber;
//        }
//
//        if (flag)
//        {
//            j1 = -999;
//        }
//
//        Slot slot1;
//        Iterator iterator;
//
//        if (doubleClick && slot != null && state == 0 && inventorySlots.func_94530_a((ItemStack)null, slot))
//        {
//            if (isShiftKeyDown())
//            {
//                if (slot != null && slot.inventory != null && shiftClickedSlot != null)
//                {
//                    iterator = inventorySlots.inventorySlots.iterator();
//
//                    while (iterator.hasNext())
//                    {
//                        slot1 = (Slot)iterator.next();
//
//                        if (slot1 != null && slot1.canTakeStack(mc.thePlayer) && slot1.getHasStack() && slot1.inventory == slot.inventory && Container.canAddItemToSlot(slot1, shiftClickedSlot, true))
//                        {
//                            handleMouseClick(slot1, slot1.slotNumber, state, 1);
//                        }
//                    }
//                }
//            }
//            else
//            {
//                handleMouseClick(slot, j1, state, 6);
//            }
//
//            doubleClick = false;
//            lastClickTime = 0L;
//        }
//        else
//        {
//            if (dragSplitting && dragSplittingButton != state)
//            {
//                dragSplitting = false;
//                dragSplittingSlots.clear();
//                ignoreMouseUp = true;
//                return;
//            }
//
//            if (ignoreMouseUp)
//            {
//                ignoreMouseUp = false;
//                return;
//            }
//
//            boolean flag1;
//
//            if (clickedSlot != null && mc.gameSettings.touchscreen)
//            {
//                if (state == 0 || state == 1)
//                {
//                    if (draggedStack == null && slot != clickedSlot)
//                    {
//                        draggedStack = clickedSlot.getStack();
//                    }
//
//                    flag1 = Container.canAddItemToSlot(slot, draggedStack, false);
//
//                    if (j1 != -1 && draggedStack != null && flag1)
//                    {
//                        handleMouseClick(clickedSlot, clickedSlot.slotNumber, state, 0);
//                        handleMouseClick(slot, j1, 0, 0);
//
//                        if (mc.thePlayer.inventory.getItemStack() != null)
//                        {
//                            handleMouseClick(clickedSlot, clickedSlot.slotNumber, state, 0);
//                            touchUpX = mouseX - l;
//                            touchUpY = mouseY - i1;
//                            returningStackDestSlot = clickedSlot;
//                            returningStack = draggedStack;
//                            returningStackTime = Minecraft.getSystemTime();
//                        }
//                        else
//                        {
//                            returningStack = null;
//                        }
//                    }
//                    else if (draggedStack != null)
//                    {
//                        touchUpX = mouseX - l;
//                        touchUpY = mouseY - i1;
//                        returningStackDestSlot = clickedSlot;
//                        returningStack = draggedStack;
//                        returningStackTime = Minecraft.getSystemTime();
//                    }
//
//                    draggedStack = null;
//                    clickedSlot = null;
//                }
//            }
//            else if (dragSplitting && !dragSplittingSlots.isEmpty())
//            {
//                handleMouseClick((Slot)null, -999, Container.func_94534_d(0, dragSplittingLimit), 5);
//                iterator = dragSplittingSlots.iterator();
//
//                while (iterator.hasNext())
//                {
//                    slot1 = (Slot)iterator.next();
//                    handleMouseClick(slot1, slot1.slotNumber, Container.func_94534_d(1, dragSplittingLimit), 5);
//                }
//
//                handleMouseClick((Slot)null, -999, Container.func_94534_d(2, dragSplittingLimit), 5);
//            }
//            else if (mc.thePlayer.inventory.getItemStack() != null)
//            {
//                if (state == mc.gameSettings.keyBindPickBlock.getKeyCode() + 100)
//                {
//                    handleMouseClick(slot, j1, state, 3);
//                }
//                else
//                {
//                    flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
//
//                    if (flag1)
//                    {
//                        shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack() : null;
//                    }
//
//                    handleMouseClick(slot, j1, state, flag1 ? 1 : 0);
//                }
//            }
//        }
//
//        if (mc.thePlayer.inventory.getItemStack() == null)
//        {
//            lastClickTime = 0L;
//        }
//
//        dragSplitting = false;
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    @Override
	protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType)
    {
    	// DEBUG
    	System.out.println("GuiGrinder handleMouseClick() for slot "+slotId);
        if (slotIn != null)
        {
            slotId = slotIn.slotNumber;
        }

        mc.playerController.windowClick(inventorySlots.windowId, slotId, clickedButton, clickType, mc.thePlayer);
    }
}