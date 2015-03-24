package com.blogspot.jabelarminecraft.blocksmith.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public abstract class RecipeHandler
{
	private final Class<? extends IRecipe>	recipe;

	public RecipeHandler(Class<? extends IRecipe> parRecipe)
	{
		recipe = parRecipe;
	}
	
	public Class<? extends IRecipe> getType()
	{
		return recipe;
	}
	
	/**
	 * Returns the "crafting grid" depending on the given Recipe
	 */
	public abstract ItemStack[] getCraftingGrid(IRecipe parRecipe);
}
