/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.slots;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityForge;

/**
 * @author jabelar
 *
 */
public class SlotForgeFuel extends Slot
{
    public SlotForgeFuel(IInventory p_i45795_1_, int p_i45795_2_, int p_i45795_3_, int p_i45795_4_)
    {
        super(p_i45795_1_, p_i45795_2_, p_i45795_3_, p_i45795_4_);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
	public boolean isItemValid(ItemStack stack)
    {
        return TileEntityForge.isItemFuel(stack) || func_178173_c_(stack);
    }

    @Override
	public int func_178170_b(ItemStack p_178170_1_)
    {
        return func_178173_c_(p_178170_1_) ? 1 : super.func_178170_b(p_178170_1_);
    }

    public static boolean func_178173_c_(ItemStack p_178173_0_)
    {
        return p_178173_0_ != null && p_178173_0_.getItem() != null && p_178173_0_.getItem() == Items.bucket;
    }
}