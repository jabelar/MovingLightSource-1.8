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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Maps;

/**
 * @author jabelar
 *
 */
public class GrinderRecipes 
{
    private static final GrinderRecipes grindingBase = new GrinderRecipes();
    /** The list of grinding results. */
    private final Map grindingList = Maps.newHashMap();
    private final Map experienceList = Maps.newHashMap();

    public static GrinderRecipes instance()
    {
        return grindingBase;
    }

    private GrinderRecipes()
    {
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.stonebrick), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.gravel)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.stone_slab), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.gravel)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.stone_slab2), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.gravel)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.stone_stairs), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.gravel)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.stone), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.gravel)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.gravel), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.sand)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.sandstone), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.sand)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.cobblestone), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.sand)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.glass), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.sand)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.brick_block), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.gravel)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 32767), new ItemStack(Items.paper, 10), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.log), 1, 32767), new ItemStack(Items.paper), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.log2), 1, 32767), new ItemStack(Items.paper), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.log2), 1, 32767), new ItemStack(Items.paper), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.nether_brick), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.netherrack)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.nether_brick_stairs), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.netherrack)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.nether_brick_fence), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.netherrack)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.netherrack), 1, 32767), new ItemStack(Item.getItemFromBlock(Blocks.soul_sand)), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.soul_sand), 1, 32767), new ItemStack(Items.gunpowder, 4), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.slime_block), 1, 32767), new ItemStack(Items.slime_ball, 9), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.obsidian), 1, 32767), new ItemStack(Items.flint, 10), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.prismarine), 1, 32767), new ItemStack(Items.prismarine_shard, 10), 0.7F);
        addGrindingRecipe(new ItemStack(Item.getItemFromBlock(Blocks.sea_lantern), 1, 32767), new ItemStack(Items.prismarine_crystals, 9), 0.7F);
    }

    public void addGrindingRecipe(ItemStack parItemStackIn, ItemStack parItemStackOut, float parExperience)
    {
        grindingList.put(parItemStackIn, parItemStackOut);
        experienceList.put(parItemStackOut, Float.valueOf(parExperience));
    }

    /**
     * Returns the grinding result of an item.
     */
    public ItemStack getGrindingResult(ItemStack parItemStack)
    {
        Iterator iterator = grindingList.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entry = (Entry)iterator.next();
        }
        while (!areItemStacksEqual(parItemStack, (ItemStack)entry.getKey()));

        return (ItemStack)entry.getValue();
    }

    private boolean areItemStacksEqual(ItemStack parItemStack1, ItemStack parItemStack2)
    {
        return parItemStack2.getItem() == parItemStack1.getItem() && (parItemStack2.getMetadata() == 32767 || parItemStack2.getMetadata() == parItemStack1.getMetadata());
    }

    public Map getGrindingList()
    {
        return grindingList;
    }

    public float getGrindingExperience(ItemStack parItemStack)
    {
        Iterator iterator = experienceList.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return 0.0F;
            }

            entry = (Entry)iterator.next();
        }
        while (!areItemStacksEqual(parItemStack, (ItemStack)entry.getKey()));

        return ((Float)entry.getValue()).floatValue();
    }
}