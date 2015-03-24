package com.blogspot.jabelarminecraft.blocksmith.recipes;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
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
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 2),
					null, null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.spruce_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 2, 1),
					null, null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.birch_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 2, 2),
					null, null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.jungle_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 2, 3),
					null, null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.acacia_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 2, 4),
					null, null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.dark_oak_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 2, 5),
					null, null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.iron_door)
		{
			return new ItemStack[] {
					new ItemStack(Items.iron_ingot, 2, 0),
					null, null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.stick)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 2),
					null, null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.paper)
		{
			return new ItemStack[] {
					new ItemStack(Items.reeds, 1),
					null, null, null, null, null, null, null, null
			};
		}
		if (parItem == Items.glass_bottle)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.glass), 1),
					null, null, null, null, null, null, null, null
			};
		}
		// else no adjustments needed
		return parItemStackArray ;
	}
	
	public static int getStackSizeNeeded(ItemStack item)
	{
		List<?> crafts = CraftingManager.getInstance().getRecipeList();
		for(int i = 0;i<crafts.size();i++)
		{
			IRecipe recipe = (IRecipe) crafts.get(i);
			if(recipe != null)
			{
				ItemStack outputItemStack = recipe.getRecipeOutput();
				if(outputItemStack!=null)
				{
					if(outputItemStack.getUnlocalizedName().equals(item.getItem().getUnlocalizedName()))
					{
						if (       outputItemStack.getItem() == Items.oak_door
								|| outputItemStack.getItem() == Items.spruce_door
								|| outputItemStack.getItem() == Items.birch_door
								|| outputItemStack.getItem() == Items.jungle_door
								|| outputItemStack.getItem() == Items.acacia_door
								|| outputItemStack.getItem() == Items.dark_oak_door
								|| outputItemStack.getItem() == Items.iron_door
								|| outputItemStack.getItem() == Items.stick
								|| outputItemStack.getItem() == Items.paper
								|| outputItemStack.getItem() == Items.glass_bottle
								)
						{
							return 1;
						}
						// DEBUG
						System.out.println("DeconstructingManager getStackSizeNeeded() needs stack size of "+outputItemStack.stackSize);
						return outputItemStack.stackSize;
					}
				}
				else
				{
//					// DEBUG
//					System.out.println("DeconstructingManager getStackSizeNeeded can't find recipe matching input item");
				}
			}
		}
		return 1;
	}
}
