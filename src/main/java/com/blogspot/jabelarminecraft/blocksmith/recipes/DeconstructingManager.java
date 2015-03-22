package com.blogspot.jabelarminecraft.blocksmith.recipes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

/**
 * Main part of the Deconstructing Table. The manager is used to parse the existing recipes and find the correct one depending on the given stack.
 * @author jglrxavpok
 */
public class DeconstructingManager 
{

	private static HashMap<Class<? extends IRecipe>, RecipeHandler>	deconstructingHandlers = new HashMap<Class<? extends IRecipe>, RecipeHandler>();

	public static List<Integer> getStackSizeNeeded(ItemStack item)
	{
		List<?> crafts = CraftingManager.getInstance().getRecipeList();
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0;i<crafts.size();i++)
		{
			IRecipe r = (IRecipe) crafts.get(i);
			if(r != null)
			{
				ItemStack s = r.getRecipeOutput();
				if(s!=null)
				{
					if(s.getItem() == item.getItem() && s.getItemDamage() == item.getItemDamage())
					{
						list.add(s.stackSize);
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
