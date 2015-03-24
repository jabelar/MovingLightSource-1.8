package com.blogspot.jabelarminecraft.blocksmith.recipes;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;


public class DeconstructingManager 
{	
	
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
//					// DEBUG
//					System.out.println("Comparing with a recipe for "+recipeKeyItemStack.getUnlocalizedName()+" with input item = "+parItemStack.getUnlocalizedName());
					if (recipeKeyItemStack.getUnlocalizedName().equals(parItemStack.getUnlocalizedName()))
					{
						// DEBUG
						System.out.println("Recipe matches item type = "+parItemStack.getUnlocalizedName()+" with item has damage value = "+parItemStack.getItemDamage());
						System.out.println("Recipe class is "+recipe.getClass().toString()+", adding crafting grid to list");
						return DeconstructingRecipeHandler.getCraftingGrid(recipe);
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
}
