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

package com.blogspot.jabelarminecraft.blocksmith.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityGrinder;

/**
 * @author jabelar
 *
 */
public class BlockGrinder extends BlockContainer
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private final boolean isGrinding;
    private boolean hasTileEntity;

    public BlockGrinder()
    {
        super(Material.rock);
        setUnlocalizedName("grinder");
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        isGrinding = true;
        setCreativeTab(CreativeTabs.tabDecorations);
        stepSound = soundTypeSnow;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        lightOpacity = 20; // cast a light shadow
        setTickRandomly(false);
        useNeighborBrightness = false;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(BlockSmith.blockGrinder);
    }

    @Override
	public void onBlockAdded(World parWorld, BlockPos parBlockPos, IBlockState parIBlockState)
    {
        if (!parWorld.isRemote)
        {
        	// Rotate block if the front side is blocked
            Block blockToNorth = parWorld.getBlockState(parBlockPos.north()).getBlock();
            Block blockToSouth = parWorld.getBlockState(parBlockPos.south()).getBlock();
            Block blockToWest = parWorld.getBlockState(parBlockPos.west()).getBlock();
            Block blockToEast = parWorld.getBlockState(parBlockPos.east()).getBlock();
            EnumFacing enumfacing = (EnumFacing)parIBlockState.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && blockToNorth.isFullBlock() && !blockToSouth.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && blockToSouth.isFullBlock() && !blockToNorth.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && blockToWest.isFullBlock() && !blockToEast.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && blockToEast.isFullBlock() && !blockToWest.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            parWorld.setBlockState(parBlockPos, parIBlockState.withProperty(FACING, enumfacing), 2);
        }
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World parWorld, BlockPos parBlockPos, IBlockState parIBlockState, Random parRand)
    {
//        if (isGrinding)
//        {
//            EnumFacing enumfacing = (EnumFacing)parIBlockState.getValue(FACING);
//            double d0 = parBlockPos.getX() + 0.5D;
//            double d1 = parBlockPos.getY() + parRand.nextDouble() * 6.0D / 16.0D;
//            double d2 = parBlockPos.getZ() + 0.5D;
//            double d3 = 0.52D;
//            double d4 = parRand.nextDouble() * 0.6D - 0.3D;
//
//            switch (BlockGrinder.SwitchEnumFacing.enumFacingArray[enumfacing.ordinal()])
//            {
//                case 1:
//                    parWorld.spawnParticle(EnumParticleTypes.CLOUD, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
//                    break;
//                case 2:
//                    parWorld.spawnParticle(EnumParticleTypes.CLOUD, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
//                    break;
//                case 3:
//                    parWorld.spawnParticle(EnumParticleTypes.CLOUD, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
//                    break;
//                case 4:
//                    parWorld.spawnParticle(EnumParticleTypes.CLOUD, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
//                    break;
//			default:
//				break;
//            }
//        }
    }

    @Override
	public boolean onBlockActivated(World parWorld, BlockPos parBlockPos, IBlockState parIBlockState, EntityPlayer parPlayer, EnumFacing parSide, float hitX, float hitY, float hitZ)
    {
        if (!parWorld.isRemote)
        {
        	// DEBUG
        	System.out.println("BlockGrinder onBlockActivated() on server side");
            parPlayer.openGui(BlockSmith.instance, BlockSmith.GUI_ENUM.GRINDER.ordinal(), parWorld, parBlockPos.getX(), parBlockPos.getY(), parBlockPos.getZ()); 
        }
        
        return true;
    }

    public static void changeBlockBasedOnGrindingStatus(boolean parIsGrinding, World parWorld, BlockPos parBlockPos)
    {
//        IBlockState iBlockState = parWorld.getBlockState(parBlockPos);
//        TileEntity tileentity = parWorld.getTileEntity(parBlockPos);
//        hasTileEntity = true;
//
//        if (parIsGrinding)
//        {
//            parWorld.setBlockState(parBlockPos, BlockSmith.blockActiveGrinder.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
//            parWorld.setBlockState(parBlockPos, BlockSmith.blockActiveGrinder.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
//        }
//        else
//        {
//            parWorld.setBlockState(parBlockPos, BlockSmith.blockGrinder.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
//            parWorld.setBlockState(parBlockPos, BlockSmith.blockGrinder.getDefaultState().withProperty(FACING, iBlockState.getValue(FACING)), 3);
//        }
//
//        hasTileEntity = false;
//
//        if (tileentity != null)
//        {
//            tileentity.validate();
//            parWorld.setTileEntity(parBlockPos, tileentity);
//        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
    	// DEBUG
    	System.out.println("BlockGrinder createNewTileEntity()");
        return new TileEntityGrinder();
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityGrinder)
            {
                ((TileEntityGrinder)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!hasTileEntity)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityGrinder)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityGrinder)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
	public boolean hasComparatorInputOverride()
    {
        return true;
    }

    @Override
	public int getComparatorInputOverride(World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
	@SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(BlockSmith.blockGrinder);
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
	public int getRenderType()
    {
        return 3;
    }

    /**
     * Possibly modify the given BlockState before rendering it on an Entity (Minecarts, Endermen, ...)
     */
    @Override
	@SideOnly(Side.CLIENT)
    public IBlockState getStateForEntityRender(IBlockState state)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING});
    }

    @SideOnly(Side.CLIENT)
    static final class SwitchEnumFacing
    {
        static final int[] enumFacingArray = new int[EnumFacing.values().length];

        static
        {
            try
            {
                enumFacingArray[EnumFacing.WEST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                enumFacingArray[EnumFacing.EAST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                enumFacingArray[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                enumFacingArray[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}