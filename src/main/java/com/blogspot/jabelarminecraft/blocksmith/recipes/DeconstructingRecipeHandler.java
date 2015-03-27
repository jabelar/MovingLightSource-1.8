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

public final class DeconstructingRecipeHandler
{
	// Counters to allow fractional deconstruction
	public int divideByTwoCounter = 1;
	public int divideByThreeCounter = 2;
	public int divideByFourCounter = 3;
	public int divideByEightCounter = 7;
	
	// The item and meta data input to deconstructor
	public Item theItem = null;
	public int theMetadata = 0;
	
	public DeconstructingRecipeHandler()
	{
		
	}
	
	public ItemStack[] getDeconstructResults(ItemStack parItemStack)
	{
		// DEBUG
		System.out.println("Looking for deconstructing a recipe for "+parItemStack.getUnlocalizedName());
		
		// Allow recipes for some vanilla items that normally don't have recipes
		theItem = parItemStack.getItem();
		if (DeconstructingAddedRecipes.shouldAddRecipe(theItem))
		{
			return DeconstructingAddedRecipes.getCraftingGrid(theItem);
		}

		// check all recipes for recipe for Itemstack
		List<?> listAllRecipes = CraftingManager.getInstance().getRecipeList();
				
		for(int i = 0;i<listAllRecipes.size();i++)
		{
			IRecipe recipe = (IRecipe) listAllRecipes.get(i);
			if(recipe != null)
			{
				ItemStack recipeKeyItemStack = recipe.getRecipeOutput();
				if(recipeKeyItemStack!=null)
				{
					if (recipeKeyItemStack.getUnlocalizedName().equals(parItemStack.getUnlocalizedName()))
					{
						// DEBUG
						System.out.println("Recipe matches item type = "+parItemStack.getUnlocalizedName()+" with item has damage value = "+parItemStack.getItemDamage());
						System.out.println("Recipe class is "+recipe.getClass().toString()+", adding crafting grid to list");
						return getCraftingGrid(recipe);
					}
				}
			}
		}
		return null;
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
		theItem = parInputItemStack.getItem();
		theMetadata = theItem.getMetadata(parInputItemStack);
		if (theItem == Items.oak_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1),
					null, null, null, null, null, null, null
			};
		}
		if (theItem == Items.spruce_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 1),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 1),
					null, null, null, null, null, null, null
			};
		}
		else if (theItem == Items.birch_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 2),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 2),
					null, null, null, null, null, null, null
			};
		}
		else if (theItem == Items.jungle_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 3),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 3),
					null, null, null, null, null, null, null
			};
		}
		else if (theItem == Items.acacia_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 4),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 4),
					null, null, null, null, null, null, null
			};
		}
		else if (theItem == Items.dark_oak_door)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 5),
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 5),
					null, null, null, null, null, null, null
			};
		}
		else if (theItem == Items.iron_door)
		{
			return new ItemStack[] {
					new ItemStack(Items.iron_ingot, 1, 0),
					new ItemStack(Items.iron_ingot, 1, 0),
					null, null, null, null, null, null, null
			};
		}
		else if (theItem == Items.paper)
		{
			return new ItemStack[] {
					new ItemStack(Items.reeds, 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theItem == Items.stick)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theItem == Item.getItemFromBlock(Blocks.ladder))
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
		else if (theItem == Item.getItemFromBlock(Blocks.oak_fence))
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
		else if (theItem == Item.getItemFromBlock(Blocks.spruce_fence))
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
		else if (theItem == Item.getItemFromBlock(Blocks.birch_fence))
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
		else if (theItem == Item.getItemFromBlock(Blocks.jungle_fence))
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
		else if (theItem == Item.getItemFromBlock(Blocks.acacia_fence))
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
		else if (theItem == Item.getItemFromBlock(Blocks.dark_oak_fence))
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
		else if (theItem == Items.enchanted_book)
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
		else if (theItem == Item.getItemFromBlock(Blocks.nether_brick_fence))
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.nether_brick), 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theItem == Item.getItemFromBlock(Blocks.wooden_slab))
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theItem == Item.getItemFromBlock(Blocks.stone_slab))
		{
			// Need to handle all the various subtypes
			// Also need to handle upper and lower slabs (this is why I do bitwise mask with 7)
			if ((theMetadata&7) == 0)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.stone), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theMetadata&7) == 1)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.sandstone), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theMetadata&7) == 2) // this is supposed to be "(stone) wooden slab" which I don't know what that is
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.stone), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theMetadata&7) == 3)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.cobblestone), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theMetadata&7) == 4)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.brick_block), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theMetadata&7) == 5)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.stonebrick), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theMetadata&7) == 6)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.nether_brick), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
			else if ((theMetadata&7) == 7)
			{
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.quartz_block), 1, 0),
						null, null, null, null, null, null, null, null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.stone_slab2)) // this is red sandstone slab
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.red_sandstone), 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theItem == Items.sign)
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
		else if (theItem == Items.glass_bottle)
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.glass), 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theItem == Item.getItemFromBlock(Blocks.rail))
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
		else if (theItem == Item.getItemFromBlock(Blocks.golden_rail))
		{
			// DEBUG
			System.out.println("Divide by two counter = "+divideByTwoCounter);
			if (divideByTwoCounter == 1)
			{
				decrementDivideByTwoCounter();
				return new ItemStack[] {
						new ItemStack(Items.gold_ingot, 1, 0), null, null,
						new ItemStack(Items.gold_ingot, 1, 0), new ItemStack(Items.stick, 1, 0), null,
						new ItemStack(Items.gold_ingot, 1, 0), null, null
				};
			}
			else if (divideByTwoCounter == 0)
			{
				decrementDivideByTwoCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Items.gold_ingot, 1, 0),
						null, null, new ItemStack(Items.gold_ingot, 1, 0),
						null, new ItemStack(Items.redstone), new ItemStack(Items.gold_ingot, 1, 0)
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.activator_rail))
		{
			// DEBUG
			System.out.println("Divide by two counter = "+divideByTwoCounter);
			if (divideByTwoCounter == 1)
			{
				decrementDivideByTwoCounter();
				return new ItemStack[] {
						new ItemStack(Items.iron_ingot, 1, 0), new ItemStack(Items.stick, 1, 0), null,
						new ItemStack(Items.iron_ingot, 1, 0), null, null,
						new ItemStack(Items.iron_ingot, 1, 0), null, null
				};
			}
			else if (divideByTwoCounter == 0)
			{
				decrementDivideByTwoCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Items.iron_ingot, 1, 0),
						null, new ItemStack(Item.getItemFromBlock(Blocks.redstone_torch), 1, 0), new ItemStack(Items.iron_ingot, 1, 0),
						null, new ItemStack(Items.stick, 1, 0), new ItemStack(Items.iron_ingot, 1, 0)
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.detector_rail))
		{
			// DEBUG
			System.out.println("Divide by two counter = "+divideByTwoCounter);
			if (divideByTwoCounter == 1)
			{
				decrementDivideByTwoCounter();
				return new ItemStack[] {
						new ItemStack(Items.iron_ingot, 1, 0), null, null,
						new ItemStack(Items.iron_ingot, 1, 0), new ItemStack(Item.getItemFromBlock(Blocks.stone_pressure_plate), 1, 0), null,
						new ItemStack(Items.iron_ingot, 1, 0), null, null
				};
			}
			else if (divideByTwoCounter == 0)
			{
				decrementDivideByTwoCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Items.iron_ingot, 1, 0),
						null, null, new ItemStack(Items.iron_ingot, 1, 0),
						null, new ItemStack(Items.redstone), new ItemStack(Items.iron_ingot, 1, 0)
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.glass_pane))
		{
			return new ItemStack[] {
					null, null, null, null, null, null,
					new ItemStack(Item.getItemFromBlock(Blocks.glass), 1, 0), 
					new ItemStack(Item.getItemFromBlock(Blocks.glass), 1, 0), 
					new ItemStack(Item.getItemFromBlock(Blocks.glass), 1, 0)
			};
		}
		else if (theItem == Item.getItemFromBlock(Blocks.stained_glass_pane))
		{
			return new ItemStack[] {
					null, null, null, null, null, null,
					new ItemStack(Item.getItemFromBlock(Blocks.stained_glass), 1, theMetadata), 
					new ItemStack(Item.getItemFromBlock(Blocks.stained_glass), 1, theMetadata), 
					new ItemStack(Item.getItemFromBlock(Blocks.stained_glass), 1, theMetadata)
			};
		}
		else if (theItem == Item.getItemFromBlock(Blocks.cobblestone_wall)) 
		{
			return new ItemStack[] {
					new ItemStack(Item.getItemFromBlock(Blocks.cobblestone), 1, 0),
					null, null, null, null, null, null, null, null
			};
		}
		else if (theItem == Item.getItemFromBlock(Blocks.quartz_block)) 
		{
			if (theMetadata == 0) // regular quartz block
			{
				return new ItemStack[] {
						null, null, null,
						new ItemStack(Items.quartz, 1, 0), new ItemStack(Items.quartz, 1, 0), null,
						new ItemStack(Items.quartz, 1, 0), new ItemStack(Items.quartz, 1, 0), null
				};
			}
			else if (theMetadata == 1) // chizeled quartz block
			{
				return new ItemStack[] {
						null, null, null,
						null, new ItemStack(Item.getItemFromBlock(Blocks.stone_slab), 1, 7), null,
						null, new ItemStack(Item.getItemFromBlock(Blocks.stone_slab), 1, 7), null
				};
			}
			else if (theMetadata == 2 || theMetadata == 3 || theMetadata == 4) // pillar quartz block, any orientation
			{
				if (divideByTwoCounter == 1)
				{
					decrementDivideByTwoCounter();
					return new ItemStack[] {
							null, null, null,
							null, null, null,
							null, new ItemStack(Item.getItemFromBlock(Blocks.quartz_block), 1, 0), null
					};
				}
				else if (divideByTwoCounter == 0)
				{
					decrementDivideByTwoCounter();
					return new ItemStack[] {
							null, null, null,
							null, new ItemStack(Item.getItemFromBlock(Blocks.quartz_block), 1, 0), null,
							null, null, null
					};
				}
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.stained_hardened_clay))
		{
			if (divideByEightCounter != 3) 
			{
				decrementDivideByEightCounter();
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.clay), 1, 0), null, null,
						null, null, null,
						null, null, null
				};
			}
			else 
			{
				// DEBUG
				System.out.println("Should output a dye");
				decrementDivideByEightCounter();
				return new ItemStack[] {
						new ItemStack(Item.getItemFromBlock(Blocks.clay), 1, 0), new ItemStack(Items.dye, 1, convertClayMetaToDyeMeta(theMetadata)), null,
						null, null, null,
						null, null, null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.oak_stairs))
		{
			theMetadata = 0;
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata)
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.spruce_stairs))
		{
			theMetadata = 1;
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata)
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.birch_stairs))
		{
			theMetadata = 2;
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata)
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.jungle_stairs))
		{
			theMetadata = 3;
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata)
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.acacia_stairs))
		{
			theMetadata = 4;
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata)
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.dark_oak_stairs))
		{
			theMetadata = 5;
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata),
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata)
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, theMetadata), null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.sandstone_stairs))
		{
			ItemStack theItemStack = new ItemStack(Item.getItemFromBlock(Blocks.sandstone));
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, theItemStack,
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, theItemStack, theItemStack,
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, theItemStack
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						theItemStack, theItemStack, null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.stone_stairs))
		{
			ItemStack theItemStack = new ItemStack(Item.getItemFromBlock(Blocks.cobblestone));
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, theItemStack,
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, theItemStack, theItemStack,
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, theItemStack
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						theItemStack, theItemStack, null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.nether_brick_stairs))
		{
			ItemStack theItemStack = new ItemStack(Item.getItemFromBlock(Blocks.nether_brick));
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, theItemStack,
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, theItemStack, theItemStack,
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, theItemStack
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						theItemStack, theItemStack, null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.red_sandstone_stairs))
		{
			ItemStack theItemStack = new ItemStack(Item.getItemFromBlock(Blocks.red_sandstone));
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, theItemStack,
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, theItemStack, theItemStack,
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, theItemStack
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						theItemStack, theItemStack, null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.stone_brick_stairs))
		{
			ItemStack theItemStack = new ItemStack(Item.getItemFromBlock(Blocks.stonebrick));
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, theItemStack,
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, theItemStack, theItemStack,
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, theItemStack
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						theItemStack, theItemStack, null
				};
			}
		}
		else if (theItem == Item.getItemFromBlock(Blocks.quartz_stairs))
		{
			ItemStack theItemStack = new ItemStack(Item.getItemFromBlock(Blocks.quartz_block));
			if (divideByFourCounter == 0) 
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, theItemStack,
						null, null, null,
						null, null, null
				};
			}
			else if (divideByFourCounter == 1)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, theItemStack, theItemStack,
						null, null, null
				};
			}
			else if (divideByFourCounter == 2)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						null, null, theItemStack
				};
			}
			else if (divideByFourCounter == 3)
			{
				decrementDivideByFourCounter();
				return new ItemStack[] {
						null, null, null,
						null, null, null,
						theItemStack, theItemStack, null
				};
			}
		}

		// else no adjustments needed
		return parOutputItemStackArray ;
	}
	
	private int convertClayMetaToDyeMeta(int parClayMeta)
	{
		// for some reason dye and clay have reversed sequence of meta data values
		return 15-parClayMeta;
	}
	
	private void decrementDivideByTwoCounter()
	{
		divideByTwoCounter--;
		if (divideByTwoCounter<0)
		{
			divideByTwoCounter=1;
		}				
	}
	
	private void decrementDivideByThreeCounter()
	{
		divideByThreeCounter--;
		if (divideByThreeCounter<0)
		{
			divideByThreeCounter=2;
		}				
	}
	
	private void decrementDivideByFourCounter()
	{
		divideByFourCounter--;
		if (divideByFourCounter<0)
		{
			divideByFourCounter=3;
		}				
	}
	
	public void decrementDivideByEightCounter()
	{
		divideByEightCounter--;
		if (divideByEightCounter<0)
		{
			divideByEightCounter=7;
		}				
	}
	
	public int getStackSizeNeeded(ItemStack parItemStack)
	{		
		return DeconstructingQuantity.getStackSizeNeeded(parItemStack);
	}
	
}
