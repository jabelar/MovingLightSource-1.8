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
				else
				{
//					// DEBUG
//					System.out.println("DeconstructingManager getStackSizeNeeded can't find recipe matching input item");
				}
			}
		}
		return list;
	}
	
	public static List<ItemStack[]> getDeconstructResults(ItemStack parItemStack)
	{
		List<?> listAllRecipes = CraftingManager.getInstance().getRecipeList();
		// DEBUG
		System.out.println("DeconstructingManager getDeconstructResults() recipe list size = "+listAllRecipes.size());
		
		List<ItemStack[]> list = new ArrayList<ItemStack[]>();
		
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
//					System.out.println("Found a recipe for the item = "+recipeKeyItemStack.getUnlocalizedName()+" with stacksize = "+recipeKeyItemStack.stackSize);
					if (recipeKeyItemStack.getItem() == parItemStack.getItem())
					{
						// DEBUG
						System.out.println("Recipe matches item type");
						if(recipeKeyItemStack.stackSize <= parItemStack.stackSize)
						{
							// DEBUG
							System.out.println("There is enough of the item to deconstruct");
							RecipeHandler recipeHandler = deconstructingHandlers.get(recipe.getClass());
							if(recipeHandler != null)
							{
								// DEBUG
								System.out.println("Recipe handler found for class "+recipe.getClass().toString()+", adding crafting grid to list");
								list.add(recipeHandler.getCraftingGrid(recipe));
							}
							else
							{
								// DEBUG
								System.out.println("Recipe handler is null");
							}
						}
						else
						{
							// DEBUG
							System.out.println("There is not enough of the item to deconstruct");
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
		return list;
	}
	
	public static void setRecipeHandler(Class<? extends IRecipe> recipe, RecipeHandler handler)
	{
		deconstructingHandlers.put(recipe, handler);
	}
	
}
