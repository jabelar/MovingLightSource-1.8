package com.blogspot.jabelarminecraft.blocksmith.recipes;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class DefaultsRecipeHandlers 
{
	/**
	 * Default Recipe Handlers
	 */
	public static final RecipeHandler defaultShapelessRecipeHandler = new ShapelessRecipeHandler(ShapelessRecipes.class);
	public static final RecipeHandler defaultShapedRecipeHandler = new ShapedRecipeHandler(ShapedRecipes.class);
	public static final RecipeHandler defaultShapelessOreRecipeHandler = new ShapelessOreRecipeHandler(ShapelessOreRecipe.class);
	public static final RecipeHandler defaultShapedOreRecipeHandler = new ShapedOreRecipeHandler(ShapedOreRecipe.class);

	private static class ShapedRecipeHandler extends RecipeHandler 
	{
		public ShapedRecipeHandler(Class<? extends IRecipe> parRecipe)
		{
			super(parRecipe);
		}

		@Override
		public ItemStack[] getCraftingGrid(IRecipe parRecipe)
		{
			ItemStack[] stacks = new ItemStack[9];
			ShapedRecipes shaped = (ShapedRecipes)parRecipe;
			for(int j = 0;j<shaped.recipeItems.length;j++)
			{
				stacks[j] = shaped.recipeItems[j];
			}
			return stacks;
		}
	}
	
	private static class ShapelessOreRecipeHandler extends RecipeHandler
	{
		public ShapelessOreRecipeHandler(Class<? extends IRecipe> recipe)
		{
			super(recipe);
		}

		@Override
		public ItemStack[] getCraftingGrid(IRecipe parRecipe)
		{
			ItemStack[] stacks = new ItemStack[9];
			ShapelessOreRecipe shapeless = (ShapelessOreRecipe)parRecipe;
			for(int j = 0;j<shapeless.getInput().size();j++)
			{
				if(shapeless.getInput().get(j) instanceof ItemStack)
				{
					stacks[j] = (ItemStack) shapeless.getInput().get(j);
				}
				else if(shapeless.getInput().get(j) instanceof ArrayList)
				{
					Object o = ((ArrayList<?>)shapeless.getInput().get(j)).get(0);
					if(o instanceof ItemStack)
					{
						stacks[j] = (ItemStack)o;
					}
				}
			}
			return stacks;
		}
	}
	
	private static class ShapedOreRecipeHandler extends RecipeHandler
	{
		public ShapedOreRecipeHandler(Class<? extends IRecipe> parRecipe)
		{
			super(parRecipe);
		}

		@Override
		public ItemStack[] getCraftingGrid(IRecipe parRecipe)
		{
			ItemStack[] stacks = new ItemStack[9];
			ShapedOreRecipe shaped = (ShapedOreRecipe)parRecipe;
			for(int j = 0;j<shaped.getInput().length;j++)
			{
				if(shaped.getInput()[j] instanceof ItemStack)
				{
					stacks[j] = (ItemStack) shaped.getInput()[j];
				}
				else if(shaped.getInput()[j] instanceof ArrayList)
				{
					Object o = ((ArrayList<?>)shaped.getInput()[j]).get(0);
					if(o instanceof ItemStack)
					{
						stacks[j] = (ItemStack)o;
					}
				}
			}
			return stacks;
		}
	}
	
	private static class ShapelessRecipeHandler extends RecipeHandler
	{
		public ShapelessRecipeHandler(Class<? extends IRecipe> parRecipe)
		{
			super(parRecipe);
		}

		@Override
		public ItemStack[] getCraftingGrid(IRecipe parRecipe)
		{
			ItemStack[] stacks = new ItemStack[9];
			ShapelessRecipes shaped = (ShapelessRecipes)parRecipe;
			for(int j = 0;j<shaped.recipeItems.size();j++)
			{
				stacks[j] = (ItemStack) shaped.recipeItems.get(j);
			}
			return stacks;
		}
	}
	
	/**
	 * Set the default Recipe Handlers
	 */
	public static void load()
	{
		DeconstructingManager.setRecipeHandler(ShapelessRecipes.class, defaultShapelessRecipeHandler);
		DeconstructingManager.setRecipeHandler(ShapedRecipes.class, defaultShapedRecipeHandler);
		DeconstructingManager.setRecipeHandler(ShapelessOreRecipe.class, defaultShapelessOreRecipeHandler);
		DeconstructingManager.setRecipeHandler(ShapedOreRecipe.class, defaultShapedOreRecipeHandler);
	}
}
