package com.blogspot.jabelarminecraft.blocksmith.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;


public class DeconstructingManager 
{

	private static HashMap<Class<? extends IRecipe>, RecipeHandler>	deconstructingHandlers = new HashMap<Class<? extends IRecipe>, RecipeHandler>();

	public static List<Integer> getStackSizeNeeded(ItemStack item)
	{
		List<?> crafts = CraftingManager.getInstance().getRecipeList();
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0;i<crafts.size();i++)
		{
			IRecipe recipe = (IRecipe) crafts.get(i);
			if(recipe != null)
			{
				ItemStack outputItemStack = recipe.getRecipeOutput();
				if(outputItemStack!=null)
				{
					if(outputItemStack.getItem() == item.getItem() && outputItemStack.getItemDamage() == item.getItemDamage())
					{
						list.add(outputItemStack.stackSize);
					}
				}
			}
		}
		return list;
	}
	
	public static List<ItemStack[]> getDeconstructResults(ItemStack parItemStack)
	{
		List<?> listAllRecipes = CraftingManager.getInstance().getRecipeList();
		List<ItemStack[]> list = new ArrayList<ItemStack[]>();
		
		// check all recipes for recipe for Itemstack
		for(int i = 0;i<listAllRecipes.size();i++)
		{
			IRecipe recipe = (IRecipe) listAllRecipes.get(i);
			if(recipe != null)
			{
				ItemStack recipeKeyItemStack = recipe.getRecipeOutput();
				if(recipeKeyItemStack!=null)
				{
					if(recipeKeyItemStack.getItem() == parItemStack.getItem() 
							&& recipeKeyItemStack.stackSize <= parItemStack.stackSize)
					{
						RecipeHandler recipeHandler = deconstructingHandlers.get(recipe.getClass());
						if(recipeHandler != null)
						{
							list.add(recipeHandler.getCraftingGrid(recipe));
						}
					}
				}
			}
		}
		return list;
	}
	
	public static void setRecipeHandler(Class<? extends IRecipe> recipe, RecipeHandler handler)
	{
		deconstructingHandlers.put(recipe, handler);
	}
	
}
