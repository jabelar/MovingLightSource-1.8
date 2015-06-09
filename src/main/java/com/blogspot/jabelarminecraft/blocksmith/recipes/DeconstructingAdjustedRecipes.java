/**
    Copyright (C) 2015 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.blocksmith.recipes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author jabelar
 *
 */
public class DeconstructingAdjustedRecipes 
{
    // Counters to allow fractional deconstruction
    public int divideByTwoCounter = 1;
    public int divideByThreeCounter = 2;
    public int divideByFourCounter = 3;
    public int divideByEightCounter = 7;
    
    // The item and meta data input to deconstructor
    public Item theItem = null;
    public int theMetadata = 0;
    
    public DeconstructingAdjustedRecipes()
    {
        
    }

    /**
     * Adjust those cases where the recipe can be divided down (e.g. one door gives back two blocks)
     * @param parOutputItemStackArray should hold the regular recipe output item stack array
     * @param theItem 
     */
    public ItemStack[] adjustOutputQuantities(ItemStack[] parOutputItemStackArray, ItemStack parInputItemStack) 
    {
        theItem = parInputItemStack.getItem();
        theMetadata = theItem.getMetadata(parInputItemStack);
        if (theItem == Items.oak_door) return outputForWoodenDoor(0);
        if (theItem == Items.spruce_door) return outputForWoodenDoor(1);
        else if (theItem == Items.birch_door) return outputForWoodenDoor(2);
        else if (theItem == Items.jungle_door) return outputForWoodenDoor(3);
        else if (theItem == Items.acacia_door) return outputForWoodenDoor(4);
        else if (theItem == Items.dark_oak_door) return outputForWoodenDoor(5);
        else if (theItem == Items.iron_door)
        {
            return new ItemStack[] {
                    new ItemStack(Items.iron_ingot, 1, 0),
                    new ItemStack(Items.iron_ingot, 1, 0),
                    null, null, null, null, null, null, null
            };
        }
        else if (theItem == Items.paper) return outputSingle(Items.reeds);
        else if (theItem == Items.stick) return outputSingle(Blocks.planks);
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
        else if (theItem == Item.getItemFromBlock(Blocks.oak_fence)) return outputForWoodenFence(0);
        else if (theItem == Item.getItemFromBlock(Blocks.spruce_fence)) return outputForWoodenFence(1);
        else if (theItem == Item.getItemFromBlock(Blocks.birch_fence)) return outputForWoodenFence(2);
        else if (theItem == Item.getItemFromBlock(Blocks.jungle_fence)) return outputForWoodenFence(3);
        else if (theItem == Item.getItemFromBlock(Blocks.acacia_fence)) return outputForWoodenFence(4);
        else if (theItem == Item.getItemFromBlock(Blocks.dark_oak_fence)) return outputForWoodenFence(5);
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
        else if (theItem == Item.getItemFromBlock(Blocks.nether_brick_fence)) return outputSingle(Blocks.nether_brick);
        else if (theItem == Item.getItemFromBlock(Blocks.wooden_slab)) return outputSingle(Blocks.planks, theMetadata);
        else if (theItem == Item.getItemFromBlock(Blocks.stone_slab)) return outputForStoneSlab();
        else if (theItem == Item.getItemFromBlock(Blocks.stone_slab2)) return outputSingle(Blocks.red_sandstone);
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
        else if (theItem == Items.glass_bottle) return outputSingle(Blocks.glass);
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
        else if (theItem == Item.getItemFromBlock(Blocks.cobblestone_wall)) return outputSingle(Blocks.cobblestone);
        else if (theItem == Item.getItemFromBlock(Blocks.quartz_block)) return outputForQuartz();
        else if (theItem == Item.getItemFromBlock(Blocks.stained_hardened_clay)) return outputForHardenedClay();
        // Wooden stairs
        else if (theItem == Item.getItemFromBlock(Blocks.oak_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 0));
        else if (theItem == Item.getItemFromBlock(Blocks.spruce_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 1));
        else if (theItem == Item.getItemFromBlock(Blocks.birch_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 2));
        else if (theItem == Item.getItemFromBlock(Blocks.jungle_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 3));
        else if (theItem == Item.getItemFromBlock(Blocks.acacia_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 4));
        else if (theItem == Item.getItemFromBlock(Blocks.dark_oak_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, 5));
        // Stone stairs
        else if (theItem == Item.getItemFromBlock(Blocks.sandstone_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.sandstone)));
        else if (theItem == Item.getItemFromBlock(Blocks.stone_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.cobblestone)));
        else if (theItem == Item.getItemFromBlock(Blocks.brick_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.brick_block)));
        else if (theItem == Item.getItemFromBlock(Blocks.nether_brick_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.nether_brick)));
        else if (theItem == Item.getItemFromBlock(Blocks.red_sandstone_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.red_sandstone)));
        else if (theItem == Item.getItemFromBlock(Blocks.stone_brick_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.stonebrick)));
        else if (theItem == Item.getItemFromBlock(Blocks.quartz_stairs)) return outputForStairs(new ItemStack(Item.getItemFromBlock(Blocks.quartz_block)));

        // else no adjustments needed
        return parOutputItemStackArray ;
    }
    
    private ItemStack[] outputSingle(Block parBlock)
    {
        return new ItemStack[] {
                new ItemStack(Item.getItemFromBlock(parBlock)),
                null, null, null, null, null, null, null, null
        };
    }

    private ItemStack[] outputSingle(Item parItem)
    {
        return new ItemStack[] {
                new ItemStack(parItem),
                null, null, null, null, null, null, null, null
        };
    }
        
    private ItemStack[] outputSingle(Block parBlock, int parMetadata)
    {
        return new ItemStack[] {
                new ItemStack(Item.getItemFromBlock(parBlock), 1, parMetadata),
                null, null, null, null, null, null, null, null
        };
    }

    private ItemStack[] outputSingle(Item parItem, int parMetadata)
    {
        return new ItemStack[] {
                new ItemStack(parItem, 1, parMetadata),
                null, null, null, null, null, null, null, null
        };
    }

    private ItemStack[] outputForWoodenDoor(int parMetadata)
    {
        return new ItemStack[] {
                new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, parMetadata),
                new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, parMetadata),
                null, null, null, null, null, null, null
        };
    }

    private ItemStack[] outputForWoodenFence(int parMetadata)
    {
        ItemStack[] resultItemStackArray = initItemStackArray();
        ItemStack planksItemStack = new ItemStack(Item.getItemFromBlock(Blocks.planks), 1, parMetadata);
        if (divideByThreeCounter == 2)
        {
            decrementDivideByThreeCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    planksItemStack,
                    new ItemStack(Items.stick, 1, 0), 
                    null, null, null, null
            };
        }
        else if (divideByThreeCounter == 1)
        {
            decrementDivideByThreeCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null, null, null, null, null,
                    new ItemStack(Items.stick, 1, 0), 
                    planksItemStack
            };
        }
        else if (divideByThreeCounter == 0)
        {
            decrementDivideByThreeCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null, null, null,
                    planksItemStack,
                    planksItemStack,
                    null, null
            };
        }
        return resultItemStackArray;
    }

    
    private ItemStack[] outputForStoneSlab()
    {
        ItemStack[] resultItemStackArray = initItemStackArray();
        // Need to handle all the various subtypes
        // Also need to handle upper and lower slabs (this is why I do bitwise mask with 7)
        if ((theMetadata&7) == 0)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.stone), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 1)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.sandstone), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 2) // this is supposed to be "(stone) wooden slab" which I don't know what that is
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.stone), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 3)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.cobblestone), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 4)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.brick_block), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 5)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.stonebrick), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 6)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.nether_brick), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        else if ((theMetadata&7) == 7)
        {
            resultItemStackArray = new ItemStack[] {
                    new ItemStack(Item.getItemFromBlock(Blocks.quartz_block), 1, 0),
                    null, null, null, null, null, null, null, null
            };
        }
        return resultItemStackArray;
    }

    private ItemStack[] outputForQuartz()
    {
        ItemStack[] resultItemStackArray = initItemStackArray();
        if (theMetadata == 0) // regular quartz block
        {
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    new ItemStack(Items.quartz, 1, 0), new ItemStack(Items.quartz, 1, 0), null,
                    new ItemStack(Items.quartz, 1, 0), new ItemStack(Items.quartz, 1, 0), null
            };
        }
        else if (theMetadata == 1) // chizeled quartz block
        {
            resultItemStackArray = new ItemStack[] {
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
                resultItemStackArray = new ItemStack[] {
                        null, null, null,
                        null, null, null,
                        null, new ItemStack(Item.getItemFromBlock(Blocks.quartz_block), 1, 0), null
                };
            }
            else if (divideByTwoCounter == 0)
            {
                decrementDivideByTwoCounter();
                resultItemStackArray = new ItemStack[] {
                        null, null, null,
                        null, new ItemStack(Item.getItemFromBlock(Blocks.quartz_block), 1, 0), null,
                        null, null, null
                };
            }
        }
        return resultItemStackArray;
    }

    private ItemStack[] outputForHardenedClay()
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
    
    private ItemStack[] outputForStairs(ItemStack parOutputItemStack)
    { 
        ItemStack[] resultItemStackArray = initItemStackArray();
        if (divideByFourCounter == 0) 
        {
            decrementDivideByFourCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, parOutputItemStack,
                    null, null, null,
                    null, null, null
            };
        }
        else if (divideByFourCounter == 1)
        {
            decrementDivideByFourCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    null, parOutputItemStack, parOutputItemStack,
                    null, null, null
            };
        }
        else if (divideByFourCounter == 2)
        {
            decrementDivideByFourCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    null, null, null,
                    null, null, parOutputItemStack
            };
        }
        else if (divideByFourCounter == 3)
        {
            decrementDivideByFourCounter();
            resultItemStackArray = new ItemStack[] {
                    null, null, null,
                    null, null, null,
                    parOutputItemStack, parOutputItemStack, null
            };
        }
    
        return resultItemStackArray;
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
    
    private void decrementDivideByEightCounter()
    {
        divideByEightCounter--;
        if (divideByEightCounter<0)
        {
            divideByEightCounter=7;
        }                
    }
    
    private ItemStack[] initItemStackArray()
    {
        ItemStack[] resultItemStackArray = new ItemStack[9];
        for(int j = 0;j<resultItemStackArray.length;j++)
        {
            resultItemStackArray[j] = null;
        }
        return resultItemStackArray;
    }
}
