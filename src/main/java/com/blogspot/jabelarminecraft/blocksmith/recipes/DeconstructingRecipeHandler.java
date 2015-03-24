package com.blogspot.jabelarminecraft.blocksmith.recipes;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class DeconstructingRecipeHandler
{	
	public static ItemStack[] getCraftingGrid(IRecipe parRecipe)
	{
		ItemStack[] itemStackArray = new ItemStack[9];
		
		if (parRecipe instanceof ShapedRecipes)
		{
			// DEBUG
			System.out.println("getCraftingGrid for shaped recipe");
			ShapedRecipes shaped = (ShapedRecipes)parRecipe;
			for(int j = 0;j<shaped.recipeItems.length;j++)
			{
				itemStackArray[j] = shaped.recipeItems[j];
			}
		}
		
		if (parRecipe instanceof ShapelessRecipes)
		{
			// DEBUG
			System.out.println("getCraftingGrid for shapeless recipe");
			ShapelessRecipes shapeless = (ShapelessRecipes)parRecipe;
			for(int j = 0;j<shapeless.recipeItems.size();j++)
			{
				itemStackArray[j] = (ItemStack) shapeless.recipeItems.get(j);
			}
		}
		
		if (parRecipe instanceof ShapedOreRecipe)
		{
			// DEBUG
			System.out.println("getCraftingGrid for shaped ore recipe");
			ShapedOreRecipe shaped = (ShapedOreRecipe)parRecipe;
			for(int j = 0;j<shaped.getInput().length;j++)
			{
				if(shaped.getInput()[j] instanceof ItemStack)
				{
					itemStackArray[j] = (ItemStack) shaped.getInput()[j];
				}
				else if(shaped.getInput()[j] instanceof List)
				{
					Object o = ((List) shaped.getInput()[j]).get(0);
					if(o instanceof ItemStack)
					{
						itemStackArray[j] = (ItemStack)o;
					}
				}
			}
		}
		
		if (parRecipe instanceof ShapelessOreRecipe)
		{
			// DEBUG
			System.out.println("getCraftingGrid for shapeless ore recipe");
			ShapelessOreRecipe shapeless = (ShapelessOreRecipe)parRecipe;
			for(int j = 0;j<shapeless.getInput().size();j++)
			{
				if(shapeless.getInput().get(j) instanceof ItemStack)
				{
					itemStackArray[j] = (ItemStack) shapeless.getInput().get(j);
				}
				else if(shapeless.getInput().get(j) instanceof List)
				{
					Object o = ((List)shapeless.getInput().get(j)).get(0);
					if(o instanceof ItemStack)
					{
						itemStackArray[j] = (ItemStack)o;
					}
				}
			}
		}

		return adjustOutputQuantities(itemStackArray, parRecipe.getRecipeOutput().getItem());
	}

	/**
	 * Adjust those cases where the recipe can be divided down (e.g. one door gives back two blocks)
	 * @param parItemStackArray should hold the regular recipe output item stack array
	 * @param parItem 
	 */
	private static ItemStack[] adjustOutputQuantities(ItemStack[] parItemStackArray, Item parItem) 
	{
		if (parItem == Items.oak_door)
		{
			// DEBUG
			System.out.println("Dividing down the door recipe");
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1),
					null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.spruce_door)
		{
			// DEBUG
			System.out.println("Dividing down the door recipe");
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 1),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 1),
					null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.birch_door)
		{
			// DEBUG
			System.out.println("Dividing down the door recipe");
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 2),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 2),
					null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.jungle_door)
		{
			// DEBUG
			System.out.println("Dividing down the door recipe");
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 3),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 3),
					null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.acacia_door)
		{
			// DEBUG
			System.out.println("Dividing down the door recipe");
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 4),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 4),
					null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.dark_oak_door)
		{
			// DEBUG
			System.out.println("Dividing down the door recipe");
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 5),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 5),
					null, null, null, null, null, null, null
			};
		}
		// else no adjustments needed
		return parItemStackArray ;
	}
}
