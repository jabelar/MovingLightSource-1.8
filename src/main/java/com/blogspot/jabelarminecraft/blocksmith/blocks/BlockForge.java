/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.blocks;

import java.util.Random;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityForge;

/**
 * @author agilroy
 *
 */
public class BlockForge extends BlockFurnace
{

	public BlockForge(boolean parLit) 
	{
		super(parLit);
		if (parLit)
		{
			setUnlocalizedName("forge_lit");
		}
		else
		{
			setUnlocalizedName("forge");
			setCreativeTab(CreativeTabs.tabDecorations);
		}
	}
	
    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState parIBlockState, Random rand, int fortune)
    {
        return Item.getItemFromBlock(BlockSmith.blockForge);
    }

    @Override
	public boolean onBlockActivated(World parWorld, BlockPos parBlockPos, IBlockState parIBlockState, EntityPlayer parPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (parWorld.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = parWorld.getTileEntity(parBlockPos);

            if (tileentity instanceof TileEntityFurnace)
            {
                parPlayer.displayGUIChest((TileEntityFurnace)tileentity);
            }

            return true;
        }
    }

    public static void changeBlockState(boolean parLit, World parWorld, BlockPos parBlockPos)
    {
        IBlockState iblockstate = parWorld.getBlockState(parBlockPos);
        TileEntity tileentity = parWorld.getTileEntity(parBlockPos);

        if (parLit)
        {
            parWorld.setBlockState(parBlockPos, BlockSmith.blockForgeLit.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            parWorld.setBlockState(parBlockPos, BlockSmith.blockForgeLit.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }
        else
        {
            parWorld.setBlockState(parBlockPos, BlockSmith.blockForge.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            parWorld.setBlockState(parBlockPos, BlockSmith.blockForge.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        if (tileentity != null)
        {
            tileentity.validate();
            parWorld.setTileEntity(parBlockPos, tileentity);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityFurnace();
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.func_174811_aO().getOpposite());
    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.func_174811_aO().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityFurnace)
            {
                ((TileEntityFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
           TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityForge)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFurnace)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
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
        return Item.getItemFromBlock(Blocks.furnace);
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
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
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

        return this.getDefaultState().withProperty(FACING, enumfacing);
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
            static final int[] field_180356_a = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00002111";

            static
            {
                try
                {
                    field_180356_a[EnumFacing.WEST.ordinal()] = 1;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_180356_a[EnumFacing.EAST.ordinal()] = 2;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_180356_a[EnumFacing.NORTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_180356_a[EnumFacing.SOUTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}
