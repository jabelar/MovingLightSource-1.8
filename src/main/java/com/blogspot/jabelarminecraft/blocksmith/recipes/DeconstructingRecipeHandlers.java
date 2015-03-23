package com.blogspot.jabelarminecraft.blocksmith.recipes;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class DeconstructingRecipeHandlers 
{
	/**
	 * Default Recipe Handlers
	 */
	public static final RecipeHandler defaultShapelessRecipeHandler = new ShapelessRecipeHandler(ShapelessRecipes.class);
	public static final RecipeHandler defaultShapedRecipeHandler = new ShapedRecipeHandler(ShapedRecipes.class);
	public static final RecipeHandler defaultShapelessOreRecipeHandler = new ShapelessOreRecipeHandler(ShapelessOreRecipe.class);
	public static final RecipeHandler defaultShapedOreRecipeHandler = new ShapedOreRecipeHandler(ShapedOreRecipe.class);

	private static class ShapelessRecipeHandler extends RecipeHandler
	{
		public ShapelessRecipeHandler(Class<? extends IRecipe> parRecipe)
		{
			super(parRecipe);
		}

		@Override
		public ItemStack[] getCraftingGrid(IRecipe parRecipe)
		{
			// DEBUG
			System.out.println("getCraftingGrid for shapeless recipe");
			ItemStack[] itemStackArray = new ItemStack[9];
			ShapelessRecipes shapeless = (ShapelessRecipes)parRecipe;
			for(int j = 0;j<shapeless.recipeItems.size();j++)
			{
				itemStackArray[j] = (ItemStack) shapeless.recipeItems.get(j);
				// DEBUG
				System.out.println("Crafting grid slot "+j+" has "+itemStackArray[j].getUnlocalizedName());
			}
			return itemStackArray;
		}
	}
	
	private static class ShapedRecipeHandler extends RecipeHandler 
	{
		
		public ShapedRecipeHandler(Class<? extends IRecipe> parRecipe)
		{
			super(parRecipe);
		}

		@Override
		public ItemStack[] getCraftingGrid(IRecipe parRecipe)
		{
			// DEBUG
			System.out.println("getCraftingGrid for shaped recipe");
			ItemStack[] itemStackArray = new ItemStack[9];
			ShapedRecipes shaped = (ShapedRecipes)parRecipe;
			for(int j = 0;j<shaped.recipeItems.length;j++)
			{
				itemStackArray[j] = shaped.recipeItems[j];
			}
			return itemStackArray;
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
			// DEBUG
			System.out.println("getCraftingGrid for shapeless ore recipe");
			ItemStack[] itemStackArray = new ItemStack[9];
			ShapelessOreRecipe shapeless = (ShapelessOreRecipe)parRecipe;
			for(int j = 0;j<shapeless.getInput().size();j++)
			{
				// DEBUG
				System.out.println("Shapeless ore recipe slot "+j);
				
				if(shapeless.getInput().get(j) instanceof ItemStack)
				{
					itemStackArray[j] = (ItemStack) shapeless.getInput().get(j);
					// DEBUG
					System.out.println("Is an ItemStack ingredient = "+itemStackArray[j].getUnlocalizedName());
				}
				else if(shapeless.getInput().get(j) instanceof List)
				{
					Object o = ((List)shapeless.getInput().get(j)).get(0);
					if(o instanceof ItemStack)
					{
						itemStackArray[j] = (ItemStack)o;
						// DEBUG
						System.out.println("Is an List ingredient = "+itemStackArray[j].getUnlocalizedName());
					}
					else
					{
						// DEBUG
						System.out.println("Is an List ingredient but not Itemstack inside");
					}
				}
				else
				{
					// DEBUG
					System.out.println("Isn't an ItemStack or List, possibly null");
				}
			}
			return itemStackArray;
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
			// DEBUG
			System.out.println("getCraftingGrid for shaped ore recipe");
			ItemStack[] itemStackArray = new ItemStack[9];
			ShapedOreRecipe shaped = (ShapedOreRecipe)parRecipe;
			for(int j = 0;j<shaped.getInput().length;j++)
			{
				// DEBUG
				System.out.println("Shaped ore recipe slot "+j);
				if(shaped.getInput()[j] instanceof ItemStack)
				{
					itemStackArray[j] = (ItemStack) shaped.getInput()[j];
					// DEBUG
					System.out.println("Is an ItemStack ingredient = "+itemStackArray[j].getUnlocalizedName());
				}
				else if(shaped.getInput()[j] instanceof List)
				{
					Object o = ((List) shaped.getInput()[j]).get(0);
					if(o instanceof ItemStack)
					{
						itemStackArray[j] = (ItemStack)o;
						// DEBUG
						System.out.println("Is an List ingredient = "+itemStackArray[j].getUnlocalizedName());
					}
					else
					{
						// DEBUG
						System.out.println("Is an List ingredient but not Itemstack inside");
					}
				}
				else
				{
					// DEBUG
					System.out.println("Isn't an ItemStack or List, possibly null");
				}
			}
			return itemStackArray;
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
