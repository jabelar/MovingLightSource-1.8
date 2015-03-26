package com.blogspot.jabelarminecraft.blocksmith.recipes;
import java.util.ArrayList;
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

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;

public final class DeconstructingRecipeHandler
{
	public int divideByTwoCounter = 1;
	public int divideByThreeCounter = 2;
	public int divideByFourCounter = 3;
	
	public DeconstructingRecipeHandler()
	{
		
	}
	
	public ItemStack[] getDeconstructResults(ItemStack parItemStack)
	{
		// DEBUG
		System.out.println("Looking for deconstructing a recipe for "+parItemStack.getUnlocalizedName());
		
		// Allow recipes for some vanilla items that normally don't have recipes
		Item theItem = parItemStack.getItem();
		if (
				   theItem == Items.enchanted_book
				|| theItem == Items.iron_horse_armor // even though there is recipe, want to adjust wool color
				|| theItem == Items.golden_horse_armor // even though there is recipe, want to adjust wool color
				|| theItem == Items.diamond_horse_armor // even though there is recipe, want to adjust wool color
				)
		{
			return getCraftingGrid(theItem);
		}

		// check all recipes for recipe for Itemstack
		List<?> listAllRecipes = CraftingManager.getInstance().getRecipeList();
//		// DEBUG
//		System.out.println("DeconstructingManager getDeconstructResults() recipe list size = "+listAllRecipes.size());
				
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
						return getCraftingGrid(recipe);
					}
					else
					{
//						// DEBUG
//						System.out.println("Recipe doesn't match item type");
					}
				}
//				else
//				{
//					// DEBUG
//					System.out.println("DeconstructingManager getDeconstructingResults() no recipe found for input item");
//				}
			}
		}
		return null;
	}	
	
	public ItemStack[] getCraftingGrid(Item parItem)
	{
		// Initialize the result array
		ItemStack[] resultItemStackArray = new ItemStack[9];
		for(int j = 0;j<resultItemStackArray.length;j++)
		{
			resultItemStackArray[j] = null;
		}
		
		// Create deconstructing recipes for things that don't have crafting recipes
		if (parItem == Items.enchanted_book)
		{
			resultItemStackArray = new ItemStack[] {
					null, new ItemStack(Items.leather, 1, 0), null,
					new ItemStack(Items.paper, 1, 0), new ItemStack(Items.paper, 1, 0),	new ItemStack(Items.paper, 1, 0),
					null, null, null
			};
		}
		// Even though horse armor has recipe, need to adjust the wool color when deconstructed
		else if (parItem == Items.iron_horse_armor)
		{
			return new ItemStack[] {
					null,
					null,
					new ItemStack(Items.iron_ingot, 1, 0),
					new ItemStack(Items.iron_ingot, 1, 0),
					new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15),
					new ItemStack(Items.iron_ingot, 1, 0),
					new ItemStack(Items.iron_ingot, 1, 0),
					new ItemStack(Items.iron_ingot, 1, 0),
					new ItemStack(Items.iron_ingot, 1, 0)
			};
		}
		else if (parItem == Items.golden_horse_armor)
		{
			return new ItemStack[] {
					null,
					null,
					new ItemStack(Items.gold_ingot, 1, 0),
					new ItemStack(Items.gold_ingot, 1, 0),
					new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12),
					new ItemStack(Items.gold_ingot, 1, 0),
					new ItemStack(Items.gold_ingot, 1, 0),
					new ItemStack(Items.gold_ingot, 1, 0),
					new ItemStack(Items.gold_ingot, 1, 0)
			};
		}
		else if (parItem == Items.diamond_horse_armor)
		{
			return new ItemStack[] {
					null,
					null,
					new ItemStack(Items.diamond, 1, 0),
					new ItemStack(Items.diamond, 1, 0),
					new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11),
					new ItemStack(Items.diamond, 1, 0),
					new ItemStack(Items.diamond, 1, 0),
					new ItemStack(Items.diamond, 1, 0),
					new ItemStack(Items.diamond, 1, 0)
			};
		}
		return resultItemStackArray;
	}
	
	public ItemStack[] getCraftingGrid(IRecipe parRecipe)
	{
		// Initialize the result array
		ItemStack[] resultItemStackArray = new ItemStack[9];
		for(int j = 0;j<resultItemStackArray.length;j++)
		{
			resultItemStackArray[j] = null;
		}
		
		if (parRecipe instanceof ShapedRecipes)
		{
			// DEBUG
			System.out.println("getCraftingGrid for shaped recipe");
			ShapedRecipes shaped = (ShapedRecipes)parRecipe;
			for(int j = 0;j<shaped.recipeItems.length;j++)
			{
				resultItemStackArray[j] = shaped.recipeItems[j];
			}
		}
		
		if (parRecipe instanceof ShapelessRecipes)
		{
			// DEBUG
			System.out.println("getCraftingGrid for shapeless recipe");
			ShapelessRecipes shapeless = (ShapelessRecipes)parRecipe;
			for(int j = 0;j<shapeless.recipeItems.size();j++)
			{
				resultItemStackArray[j] = (ItemStack) shapeless.recipeItems.get(j);
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
					resultItemStackArray[j] = (ItemStack) shaped.getInput()[j];
				}
				else if(shaped.getInput()[j] instanceof List)
				{
					Object o = ((List) shaped.getInput()[j]).get(0);
					if(o instanceof ItemStack)
					{
						resultItemStackArray[j] = (ItemStack)o;
					}
				}
			}
		}
		
		if (parRecipe instanceof ShapelessOreRecipe)
		{
			ArrayList shapelessArray = ((ShapelessOreRecipe)parRecipe).getInput();
			// DEBUG
			System.out.println("getCraftingGrid for shapeless ore recipe with input array size = "+shapelessArray.size());
			for(int j = 0; j<shapelessArray.size(); j++)
			{
				if(shapelessArray.get(j) instanceof ItemStack)
				{
//					// DEBUG
//					System.out.println("Ingredient in shapeless array slot "+j+" is an ItemStack");
					resultItemStackArray[j] = (ItemStack) shapelessArray.get(j);
				}
				else if(shapelessArray.get(j) instanceof List)
				{
//					// DEBUG
//					System.out.println("Ingredient in shapeless array slot "+j+" is a List");
					Object o = ((List)shapelessArray.get(j)).get(0);
					if(o instanceof ItemStack)
					{
						resultItemStackArray[j] = (ItemStack)o;
					}
					else
					{
						// DEBUG
						System.out.println("But list element is not an ItemStack");
					}
				}
			}
		}

		return adjustOutputQuantities(resultItemStackArray, parRecipe.getRecipeOutput());
	}

	/**
	 * Adjust those cases where the recipe can be divided down (e.g. one door gives back two blocks)
	 * @param parOutputItemStackArray should hold the regular recipe output item stack array
	 * @param theItem 
	 */
	private ItemStack[] adjustOutputQuantities(ItemStack[] parOutputItemStackArray, ItemStack parInputItemStack) 
	{
		Item theInputItem = parInputItemStack.getItem();
		if (theInputItem == Items.oak_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1),
					null, null, null, null, null, null, null
			};
		}
		if (theInputItem == Items.spruce_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 1),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 1),
					null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Items.birch_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 2),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 2),
					null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Items.jungle_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 3),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 3),
					null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Items.acacia_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 4),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 4),
					null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Items.dark_oak_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 5),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 5),
					null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Items.iron_door)
		{
			return new ItemStack[] {
					new ItemStack(Items.iron_ingot, 1, 0),
					new ItemStack(Items.iron_ingot, 1, 0),
					null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Items.paper)
		{
			return new ItemStack[] {
					new ItemStack(Items.reeds, 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Items.stick)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.ladder))
		{
			if (divideByThreeCounter <= 0)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null,
						new ItemStack(Items.stick, 1, 0),
						new ItemStack(Items.stick, 1, 0), 
						new ItemStack(Items.stick, 1, 0), 
						null, null, null
				};
			}
			else if (divideByThreeCounter == 1)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						new ItemStack(Items.stick, 1, 0),
						null,
						new ItemStack(Items.stick, 1, 0), 
						null, null, null, null, null, null
				};
			}
			else if (divideByThreeCounter == 2)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null, null,
						new ItemStack(Items.stick, 1, 0),
						null,
						new ItemStack(Items.stick, 1, 0), 
				};
			}
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.oak_fence))
		{
			ItemStack planksItemStack = new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 0);
			if (divideByThreeCounter == 2)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null,
						planksItemStack,
						new ItemStack(Items.stick, 1, 0), 
						null, null, null, null
				};
			}
			else if (divideByThreeCounter == 1)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null, null, null,
						new ItemStack(Items.stick, 1, 0), 
						planksItemStack
				};
			}
			else if (divideByThreeCounter == 0)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null,
						planksItemStack,
						planksItemStack,
						null, null
				};
			}
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.spruce_fence))
		{
			ItemStack planksItemStack = new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 1);
			if (divideByThreeCounter == 2)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null,
						planksItemStack,
						new ItemStack(Items.stick, 1, 0), 
						null, null, null, null
				};
			}
			else if (divideByThreeCounter == 1)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null, null, null,
						new ItemStack(Items.stick, 1, 0), 
						planksItemStack
				};
			}
			else if (divideByThreeCounter == 0)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null,
						planksItemStack,
						planksItemStack,
						null, null
				};
			}
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.birch_fence))
		{
			ItemStack planksItemStack = new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 2);
			if (divideByThreeCounter == 2)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null,
						planksItemStack,
						new ItemStack(Items.stick, 1, 0), 
						null, null, null, null
				};
			}
			else if (divideByThreeCounter == 1)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null, null, null,
						new ItemStack(Items.stick, 1, 0), 
						planksItemStack
				};
			}
			else if (divideByThreeCounter == 0)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null,
						planksItemStack,
						planksItemStack,
						null, null
				};
			}
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.jungle_fence))
		{
			ItemStack planksItemStack = new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 3);
			if (divideByThreeCounter == 2)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null,
						planksItemStack,
						new ItemStack(Items.stick, 1, 0), 
						null, null, null, null
				};
			}
			else if (divideByThreeCounter == 1)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null, null, null,
						new ItemStack(Items.stick, 1, 0), 
						planksItemStack
				};
			}
			else if (divideByThreeCounter == 0)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null,
						planksItemStack,
						planksItemStack,
						null, null
				};
			}
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.acacia_fence))
		{
			ItemStack planksItemStack = new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 4);
			if (divideByThreeCounter == 2)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null,
						planksItemStack,
						new ItemStack(Items.stick, 1, 0), 
						null, null, null, null
				};
			}
			else if (divideByThreeCounter == 1)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null, null, null,
						new ItemStack(Items.stick, 1, 0), 
						planksItemStack
				};
			}
			else if (divideByThreeCounter == 0)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null,
						planksItemStack,
						planksItemStack,
						null, null
				};
			}
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.dark_oak_fence))
		{
			ItemStack planksItemStack = new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 5);
			if (divideByThreeCounter == 2)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null,
						planksItemStack,
						new ItemStack(Items.stick, 1, 0), 
						null, null, null, null
				};
			}
			else if (divideByThreeCounter == 1)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null, null, null,
						new ItemStack(Items.stick, 1, 0), 
						planksItemStack
				};
			}
			else if (divideByThreeCounter == 0)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, null, null, null,
						planksItemStack,
						planksItemStack,
						null, null
				};
			}
		}
		else if (theInputItem == Items.enchanted_book)
		{
			return new ItemStack[] {
					null, 
					new ItemStack(Items.reeds, 1, 0),
					null,
					null, 
					new ItemStack(Items.reeds, 1, 0),
					null,
					null, 
					new ItemStack(Items.reeds, 1, 0),
					null
			};
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.nether_brick_fence))
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.nether_brick), 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.wooden_slab))
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theInputItem.getMetadata(parInputItemStack)),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.stone_slab))
		{
			// Need to handle all the various subtypes
			// Also need to handle upper and lower slabs (this is why I do bitwise mask with 7)
			if ((theInputItem.getMetadata(parInputItemStack)&7) == 0)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.stone), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theInputItem.getMetadata(parInputItemStack)&7) == 1)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.sandstone), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theInputItem.getMetadata(parInputItemStack)&7) == 2) // this is supposed to be "(stone) wooden slab" which I don't know what that is
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.stone), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theInputItem.getMetadata(parInputItemStack)&7) == 3)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.cobblestone), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theInputItem.getMetadata(parInputItemStack)&7) == 4)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.brick_block), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theInputItem.getMetadata(parInputItemStack)&7) == 5)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.stonebrick), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theInputItem.getMetadata(parInputItemStack)&7) == 6)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.nether_brick), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theInputItem.getMetadata(parInputItemStack)&7) == 7)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.quartz_block), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.stone_slab2)) // this is red sandstone slab
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.red_sandstone), 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Items.sign)
		{
			ItemStack planksItemStack = new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 0);
			if (divideByThreeCounter == 2)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						planksItemStack, null, null,
						planksItemStack, null, null, 
						null, null, null
				};
			}
			else if (divideByThreeCounter == 0)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, planksItemStack, null, 
						null, planksItemStack, null, 
						null, new ItemStack(Items.stick, 1, 0), null
				};
			}
			else if (divideByThreeCounter == 1)
			{
				decrementDivideByThreeCounter();
				return new ItemStack[] {
						null, null, planksItemStack, 
						null, null, planksItemStack,
						null, null, null
				};
			}
		}
		else if (theInputItem == Items.glass_bottle)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.glass), 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theInputItem == Item.getItemFromBlock(Blocks.rail))
		{
			// DEBUG
			System.out.println("Divide by two counter = "+divideByTwoCounter);
			if (divideByTwoCounter == 1)
			{
				decrementDivideByTwoCounter();
				return new ItemStack[] {
						new ItemStack(Items.iron_ingot, 1, 0), null, null,
						new ItemStack(Items.iron_ingot, 1, 0), null, null,
						new ItemStack(Items.iron_ingot, 1, 0), null, null
				};
			}
			else if (divideByTwoCounter == 0)
			{
				decrementDivideByTwoCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Items.iron_ingot, 1, 0),
						null, new ItemStack(Items.stick, 1, 0), new ItemStack(Items.iron_ingot, 1, 0),
						null, null, new ItemStack(Items.iron_ingot, 1, 0)
				};
			}
		}

		// else no adjustments needed
		return parOutputItemStackArray ;
	}
	
	public void decrementDivideByTwoCounter()
	{
		// DEBUG
		System.out.println("Decrementing divide by two counter with counter starting at "+divideByTwoCounter);
		divideByTwoCounter--;
		if (divideByTwoCounter<0)
		{
			divideByTwoCounter=1;
		}				
		// DEBUG
		System.out.println("Decrementing divide by two counter with counter resulting in "+divideByTwoCounter);
	}
	
	public void decrementDivideByThreeCounter()
	{
		divideByThreeCounter--;
		if (divideByThreeCounter<0)
		{
			divideByThreeCounter=2;
		}				
	}
	
	public int getStackSizeNeeded(ItemStack parItemStack)
	{		
		Item theItem = parItemStack.getItem();
		// Create recipes for some things that don't normally have them
		if (
				theItem == Items.enchanted_book
				)
		{
			return 1;
		}
		List<?> crafts = CraftingManager.getInstance().getRecipeList();
		for(int i = 0;i<crafts.size();i++)
		{
			IRecipe recipe = (IRecipe) crafts.get(i);
			if(recipe != null)
			{
				ItemStack outputItemStack = recipe.getRecipeOutput();
				// if found matching recipe
				if (outputItemStack != null)
				{					
//					// DEBUG
//					System.out.println("Checking if recipes match: "+outputItemStack.getUnlocalizedName()+" with "+parItemStack.getUnlocalizedName());
					if (outputItemStack.getUnlocalizedName().equals(parItemStack.getUnlocalizedName()))
					{
						// DEBUG
						System.out.println("getStackSizeNeeded() found matching recipe");
						// prevent some deconstructions that aren't realistic (like paper into reeds)
						if (!BlockSmith.allowDeconstructAllCraftable)
						{
							if (theItem == Items.paper
							|| theItem == Items.melon_seeds
							|| theItem == Items.bread
							|| theItem == Items.cake
							|| (!BlockSmith.allowHorseArmorCrafting
							&& (   theItem == Items.saddle
								|| theItem == Items.iron_horse_armor
								|| theItem == Items.golden_horse_armor
								|| theItem == Items.diamond_horse_armor
								))
							)
							{
								return 0;
							}
						}
						if (       theItem == Items.oak_door
								|| theItem == Items.spruce_door
								|| theItem == Items.birch_door
								|| theItem == Items.jungle_door
								|| theItem == Items.acacia_door
								|| theItem == Items.dark_oak_door
								|| theItem == Items.iron_door
								|| theItem == Items.paper
								|| theItem == Items.stick
								|| theItem == Item.getItemFromBlock(Blocks.ladder)
								|| theItem == Items.enchanted_book					
								|| theItem == Item.getItemFromBlock(Blocks.oak_fence)
								|| theItem == Item.getItemFromBlock(Blocks.spruce_fence)
								|| theItem == Item.getItemFromBlock(Blocks.birch_fence)
								|| theItem == Item.getItemFromBlock(Blocks.jungle_fence)
								|| theItem == Item.getItemFromBlock(Blocks.acacia_fence)
								|| theItem == Item.getItemFromBlock(Blocks.dark_oak_fence)
								|| theItem == Item.getItemFromBlock(Blocks.nether_brick_fence)
								|| theItem == Items.sign
								|| theItem == Items.glass_bottle
								)
						{
							return 1;
						}
						if (theItem == Items.paper
								|| theItem == Item.getItemFromBlock(Blocks.wooden_slab)
								|| theItem == Item.getItemFromBlock(Blocks.stone_slab)
								|| theItem == Item.getItemFromBlock(Blocks.stone_slab2)
								)
						{
							return 2;
						}
						if (       theItem == Item.getItemFromBlock(Blocks.iron_bars)
								|| theItem == Item.getItemFromBlock(Blocks.rail)
								|| theItem == Item.getItemFromBlock(Blocks.golden_rail)
								|| theItem == Item.getItemFromBlock(Blocks.activator_rail)
								|| theItem == Item.getItemFromBlock(Blocks.detector_rail)
								)
						{
							return 8;
						}
						// DEBUG
						System.out.println("No adjustment needed to recipe amount");
						return outputItemStack.stackSize;
					}
				}
//				else
//				{
//					// DEBUG
//					System.out.println("Recipe output stack is null!");
//				}
			}
		}
		// DEBUG
		System.out.println("No matching recipe found!");
		return 0; // no recipe found
	}
	
}
