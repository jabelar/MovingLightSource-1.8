/**
    Copyright (C) 2014 by jabelar

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

package com.blogspot.jabelarminecraft.blocksmith.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityTanningRack;

/**
 * @author jabelar
 *
 */
public class BlockTanningRack extends BlockContainer
{
	// create a property to allow animation of the block model
    public static final PropertyBool TANNING_COMPLETE = PropertyBool.create("tanning_complete");

    public BlockTanningRack()
    {
        super(BlockSmith.materialTanningRack);
        // DEBUG
        System.out.println("BlockTanningRack constructor");
        // override default values of Block, where appropriate
        setUnlocalizedName("tanningrack");
        setCreativeTab(CreativeTabs.tabBlock);
        stepSound = soundTypeSnow;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        lightOpacity = 20; // cast a light shadow
        setTickRandomly(false);
        useNeighborBrightness = false;
    }

    public static void changeBlockBasedOnTanningStatus(boolean parTanningComplete, World parWorld, BlockPos parBlockPos)
    {
        parWorld.setBlockState(parBlockPos, BlockSmith.blockTanningRack.getDefaultState().withProperty(TANNING_COMPLETE, parTanningComplete));
    }
    
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
    	// DEBUG
    	System.out.println("BlockTanningRack createNewTileEntity()");
        return new TileEntityTanningRack();
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(TANNING_COMPLETE, false);
    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(TANNING_COMPLETE, false));

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityTanningRack)
            {
                ((TileEntityTanningRack)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityTanningRack)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityTanningRack)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    // Ensure that you can see through the translucent block properly, i.e. render inside sides.
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess parWorld, BlockPos parPos, EnumFacing parSide)
    {
    	return true;
    }

    /**
     * Returns true if the given side of this block type should be rendered (if it's solid or not), if the adjacent
     * block is at the given coordinates. Args: blockAccess, x, y, z, side
     */
    @Override
	public boolean isBlockSolid(IBlockAccess parWorld, BlockPos parPos, EnumFacing parSide)
    {
        return getMaterial().isSolid();
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
	public boolean isOpaqueCube()
    {
        return getMaterial().isOpaque();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return 0xFFFFFF; // white
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    @Override
	public int getMobilityFlag()
    {
        return getMaterial().getMaterialMobility();
    }

    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param side The side to check
     * @return True if the block is solid on the specified side.
     */
    @Override
	public boolean isSideSolid(IBlockAccess world, BlockPos parPos, EnumFacing parSide)
    {
        return true; 
    }
    
    @Override
	public boolean isNormalCube()
    {
    	return true;
    }
    
    @Override
	public boolean isNormalCube(IBlockAccess parWorld, BlockPos parPos)
    {
    	return true;
    }
    

    /**
     * Determines if a new block can be replace the space occupied by this one,
     * Used in the player's placement code to make the block act like water, and lava.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is replaceable by another block
     */
    @Override
	public boolean isReplaceable(World world, BlockPos parPos)
    {
        return getMaterial().isReplaceable();
    }

    /**
     * Chance that fire will spread and consume this block.
     * 300 being a 100% chance, 0, being a 0% chance.
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param face The face that the fire is coming from
     * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
     */
    @Override
	public int getFlammability(IBlockAccess world, BlockPos parPos, EnumFacing parSide)
    {
        return 0;
    }

    /**
     * Currently only called by fire when it is on top of this block.
     * Returning true will prevent the fire from naturally dying during updating.
     * Also prevents firing from dying from rain.
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param side The face that the fire is coming from
     * @return True if this block sustains fire, meaning it will never go out.
     */
    @Override
	public boolean isFireSource(World parWorld, BlockPos parPos, EnumFacing parSide)
    {
        return false;
    }

    /**
     * Metadata and fortune sensitive version, this replaces the old (int meta, Random rand)
     * version in 1.1.
     *
     * @param meta Blocks Metadata
     * @param fortune Current item fortune level
     * @param random Random number generator
     * @return The number of items to drop
     */
    @Override
	public int quantityDropped(IBlockState parState, int parFortune, Random parRandom)
    {
        /**
         * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
         */
        return 0;
    }

    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    @Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos parPos, IBlockState parState, int parFortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        return ret;
    }

    @Override
	public boolean canSilkHarvest(World parWorld, BlockPos parPos, IBlockState parState, EntityPlayer parPlayer)
    {
    	return false;
    }

    @Override
	public boolean canCreatureSpawn(IBlockAccess parWorld, BlockPos parPos, SpawnPlacementType parType)
    {
    	// TODO
    	// probably want to limit by creature type
        return true;
    }

    @Override
	public boolean shouldCheckWeakPower(IBlockAccess parWorld, BlockPos parPos, EnumFacing parSide)
    {
        return false;
    }

    /**
     * Checks if the specified tool type is efficient on this block, 
     * meaning that it digs at full speed.
     * 
     * @param type
     * @param metadata
     * @return
     */
    @Override
	public boolean isToolEffective(String parType, IBlockState parState)
    {
        return false;
    }
    
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
    	if (meta == 0)
    	{
    		return getDefaultState().withProperty(TANNING_COMPLETE, false);
    	}
    	else
    	{
    		return getDefaultState().withProperty(TANNING_COMPLETE, true);
    	}
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
    	if ((Boolean)state.getValue(TANNING_COMPLETE))
		{
    		return 1;
		}
    	else
    	{
    		return 0;
    	}
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {TANNING_COMPLETE});
    }
}
