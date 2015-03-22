package com.blogspot.jabelarminecraft.blocksmith.events;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author jglrxavpok
 *
 */
public class SuccessfulDeconstructingEvent extends Event
{

	/**
	 * Uncrafted item
	 */
	private final ItemStack	uncrafted;
	/**
	 * Output of the uncrafting
	 */
	private final ItemStack[]	out;
	/**
	 * Number of items required
	 */
	private final int	nbr;
	/**
	 * The player uncrafting the item
	 */
	private final EntityPlayer	p;
	/**
	 * When the event was fired (more like created, actually)
	 */
	private final long	when;

	public SuccessfulDeconstructingEvent(ItemStack stack, ItemStack[] output, int required, EntityPlayer player)
	{
		uncrafted = stack;
		out = output;
		nbr = required;
		p = player;
		when = System.currentTimeMillis();
	}
	
	/**
	 * When the event was fired (more like created, actually)
	 */
	public long getWhen()
	{
		return when;
	}
	
	/**
	 * The player uncrafting the item
	 */
	public EntityPlayer getPlayer()
	{
		return p;
	}
	
	/**
	 * The number of required items in order to do the uncrafting
	 * @return
	 */
	public int getRequiredNumber()
	{
		return nbr;
	}
	
	@Override
	public boolean isCancelable()
	{
		return true;
	}
	
	/**
	 * The uncrafted item 
	 */
	public ItemStack getUncrafted()
	{
		return uncrafted;
	}
	
	/**
	 * The output of the uncrafting
	 */
	public ItemStack[] getOutput()
	{
		return out;
	}
}
