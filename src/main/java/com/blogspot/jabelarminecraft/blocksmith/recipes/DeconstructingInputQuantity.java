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

package com.blogspot.jabelarminecraft.blocksmith.recipes;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;

/**
 * @author jabelar
 *
 */
public class DeconstructingInputQuantity 
{
    public static int getStackSizeNeeded(ItemStack parItemStack)
    {
        Item theItem = parItemStack.getItem();
        // Create recipes for some things that don't normally have them
        if (theItem == Items.enchanted_book)
        {
            if (BlockSmith.allowDeconstructEnchantedBooks)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        List<?> crafts = CraftingManager.getInstance().getRecipeList();
        for (int i = 0;i<crafts.size();i++)
        {
            IRecipe recipe = (IRecipe) crafts.get(i);
            if (recipe != null)
            {
                ItemStack outputItemStack = recipe.getRecipeOutput();
                // if found matching recipe
                if (outputItemStack != null)
                {                    
                    if (outputItemStack.getUnlocalizedName().equals(parItemStack.getUnlocalizedName()))
                    {
                        // DEBUG
                        System.out.println("getStackSizeNeeded() found matching recipe");
                        return adjustQuantity(theItem, outputItemStack.stackSize);
                    }
                }
            }
        }
        // DEBUG
        System.out.println("No matching recipe found!");
        return 0; // no recipe found
    }

    public static int adjustQuantity(Item theItem, int parDefaultQuantity)
    {
        // prevent some deconstructions that aren't realistic (like paper into reeds)
        if (!BlockSmith.allowDeconstructUnrealistic)
        {
            if (     theItem == Items.paper 
                  || theItem == Items.melon_seeds
                  || theItem == Items.pumpkin_seeds
                  || theItem == Items.bread
                  || theItem == Items.cake
                )
            {
                // DEBUG
                System.out.println("Triying to deconstruct unrealistic item when not allowed");
                return 0;
            }
        }
        if (!BlockSmith.allowHorseArmorCrafting && 
                (    theItem == Items.saddle
                  || theItem == Items.iron_horse_armor
                  || theItem == Items.golden_horse_armor
                  || theItem == Items.diamond_horse_armor
                )
            )
        {
            // DEBUG
            System.out.println("Triying to deconstruct horse armor or saddle item when not allowed");
            return 0;
        }
        if (!BlockSmith.allowPartialDeconstructing)
        {
            // DEBUG
            System.out.println("Don't look for partial deconstruct recipe when not allowed");
            return parDefaultQuantity;
        }
        if (       theItem == Items.oak_door
                || theItem == Items.spruce_door
                || theItem == Items.birch_door
                || theItem == Items.jungle_door
                || theItem == Items.acacia_door
                || theItem == Items.dark_oak_door
                || theItem == Items.iron_door
                || theItem == Items.paper
                || theItem == Items.stick
                || theItem == Item.getItemFromBlock(Blocks.ladder)
                || theItem == Items.enchanted_book                    
                || theItem == Item.getItemFromBlock(Blocks.oak_fence)
                || theItem == Item.getItemFromBlock(Blocks.spruce_fence)
                || theItem == Item.getItemFromBlock(Blocks.birch_fence)
                || theItem == Item.getItemFromBlock(Blocks.jungle_fence)
                || theItem == Item.getItemFromBlock(Blocks.acacia_fence)
                || theItem == Item.getItemFromBlock(Blocks.dark_oak_fence)
                || theItem == Item.getItemFromBlock(Blocks.nether_brick_fence)
                || theItem == Items.sign
                || theItem == Items.glass_bottle
                || theItem == Item.getItemFromBlock(Blocks.cobblestone_wall)
                || theItem == Item.getItemFromBlock(Blocks.quartz_block)
                || theItem == Item.getItemFromBlock(Blocks.stained_hardened_clay)
                || theItem == Item.getItemFromBlock(Blocks.oak_stairs)
                || theItem == Item.getItemFromBlock(Blocks.spruce_stairs)
                || theItem == Item.getItemFromBlock(Blocks.birch_stairs)
                || theItem == Item.getItemFromBlock(Blocks.jungle_stairs)
                || theItem == Item.getItemFromBlock(Blocks.acacia_stairs)
                || theItem == Item.getItemFromBlock(Blocks.dark_oak_stairs)
                || theItem == Item.getItemFromBlock(Blocks.stone_stairs)
                || theItem == Item.getItemFromBlock(Blocks.sandstone_stairs)
                || theItem == Item.getItemFromBlock(Blocks.nether_brick_stairs)
                || theItem == Item.getItemFromBlock(Blocks.red_sandstone_stairs)
                || theItem == Item.getItemFromBlock(Blocks.quartz_stairs)
                || theItem == Item.getItemFromBlock(Blocks.stone_brick_stairs)
                || theItem == Item.getItemFromBlock(Blocks.brick_stairs)
                )
        {
            return 1;
        }
        if (theItem == Items.paper
                || theItem == Item.getItemFromBlock(Blocks.wooden_slab)
                || theItem == Item.getItemFromBlock(Blocks.stone_slab)
                || theItem == Item.getItemFromBlock(Blocks.stone_slab2)
                )
        {
            return 2;
        }
        if (       theItem == Item.getItemFromBlock(Blocks.iron_bars)
                || theItem == Item.getItemFromBlock(Blocks.rail)
                || theItem == Item.getItemFromBlock(Blocks.golden_rail)
                || theItem == Item.getItemFromBlock(Blocks.activator_rail)
                || theItem == Item.getItemFromBlock(Blocks.detector_rail)
                || theItem == Item.getItemFromBlock(Blocks.glass_pane)
                || theItem == Item.getItemFromBlock(Blocks.stained_glass_pane)
                )
        {
            return 8;
        }
        return parDefaultQuantity;
    }
}
