/**
 * 
 */
package com.blogspot.jabelarminecraft.blocksmith.items;

import net.minecraft.item.ItemSword;

/**
 * @author jabelar
 *
 */
public class ItemSwordExtended extends ItemSword implements IExtendedReach
{
	public ItemSwordExtended(ToolMaterial parMaterial) 
	{
		super(parMaterial);
		setUnlocalizedName("swordExtended");
	}

	@Override
	public float getReach() 
	{
		return 30.0F;
	}

}
