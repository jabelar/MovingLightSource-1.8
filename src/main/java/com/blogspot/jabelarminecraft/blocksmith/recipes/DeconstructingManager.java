package com.blogspot.jabelarminecraft.blocksmith.recipes;

import java.util.HashMap;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;


public class DeconstructingManager 
{

	private static HashMap<Class<? extends IRecipe>, RecipeHandler>	deconstructingHandlers = new HashMap<Class<? extends IRecipe>, RecipeHandler>();

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
						if (outputItemStack.getItem() == Items.oak_door)
						{
							// DEBUG
							System.out.println("DeconstructingManager getStackSizeNeeded() needs stack size of "+outputItemStack.stackSize/3);
							return outputItemStack.stackSize / 3 ;
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
	
	public static ItemStack[] getDeconstructResults(ItemStack parItemStack)
	{
		List<?> listAllRecipes = CraftingManager.getInstance().getRecipeList();
		// DEBUG
		System.out.println("DeconstructingManager getDeconstructResults() recipe list size = "+listAllRecipes.size());
				
		// check all recipes for recipe for Itemstack
		for(int i = 0;i<listAllRecipes.size();i++)
		{
//			// DEBUG
//			System.out.println("Checking recipe number = "+i);
			
			IRecipe recipe = (IRecipe) listAllRecipes.get(i);
			if(recipe != null)
			{
				ItemStack recipeKeyItemStack = recipe.getRecipeOutput();
				if(recipeKeyItemStack!=null)
				{
					// DEBUG
					System.out.println("Comparing with a recipe for "+recipeKeyItemStack.getUnlocalizedName()+" with input item = "+parItemStack.getUnlocalizedName());
					if (recipeKeyItemStack.getUnlocalizedName().equals(parItemStack.getUnlocalizedName()))
					{
						// DEBUG
						System.out.println("Recipe matches item type");
						RecipeHandler recipeHandler = deconstructingHandlers.get(recipe.getClass());
						if(recipeHandler != null)
						{
							// DEBUG
							System.out.println("The item has damage value = "+parItemStack.getItemDamage());
							System.out.println("Recipe handler found for class "+recipe.getClass().toString()+", adding crafting grid to list");
							return recipeHandler.getCraftingGrid(recipe);
						}
						else
						{
							// DEBUG
							System.out.println("Recipe handler is null");
						}
					}
					else
					{
//						// DEBUG
//						System.out.println("Recipe doesn't match item type");
					}
				}
				else
				{
					// DEBUG
					System.out.println("DeconstructingManager getDeconstructingResults() no recipe found for input item");
				}
			}
		}
		return null;
	}
	
	public static void setRecipeHandler(Class<? extends IRecipe> recipe, RecipeHandler handler)
	{
		deconstructingHandlers.put(recipe, handler);
	}
	
}
