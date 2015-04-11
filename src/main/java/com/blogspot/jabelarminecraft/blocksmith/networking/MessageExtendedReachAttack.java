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

package com.blogspot.jabelarminecraft.blocksmith.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.items.IExtendedReach;
import com.blogspot.jabelarminecraft.blocksmith.utilities.Utilities;

/**
 * @author jabelar
 *
 */
public class MessageExtendedReachAttack implements IMessage 
{
    private int entityId ;

    public MessageExtendedReachAttack() 
    { 
    	// need this constructor
    }

    public MessageExtendedReachAttack(int parEntityId) 
    {
    	entityId = parEntityId;
        // DEBUG
        System.out.println("Constructor");
    }

    @Override
    public void fromBytes(ByteBuf buf) 
    {
    	entityId = ByteBufUtils.readVarInt(buf, 4);
    	// DEBUG
    	System.out.println("fromBytes");
    }

    @Override
    public void toBytes(ByteBuf buf) 
    {
    	ByteBufUtils.writeVarInt(buf, entityId, 4);
        // DEBUG
        System.out.println("toBytes encoded");
    }

    public static class Handler implements IMessageHandler<MessageExtendedReachAttack, IMessage> 
    {
        @Override
        public IMessage onMessage(MessageExtendedReachAttack message, MessageContext ctx) 
        {
        	// DEBUG
        	System.out.println("Message received");
        	EntityPlayer thePlayer = BlockSmith.proxy.getPlayerEntityFromContext(ctx);
        	Entity theEntity = Utilities.getEntityByID(message.entityId, thePlayer.worldObj);
        	// DEBUG
        	System.out.println("Entity = "+theEntity);
        	
        	// Need to ensure that hackers can't cause trick kills, so double check weapon type
        	// and reach
        	if (thePlayer.getCurrentEquippedItem() == null)
        	{
        		return null;
        	}
        	if (thePlayer.getCurrentEquippedItem().getItem() instanceof IExtendedReach)
        	{
        		IExtendedReach theExtendedReachWeapon = (IExtendedReach)thePlayer.getCurrentEquippedItem().getItem();
        		double distanceSq = thePlayer.getDistanceSqToEntity(theEntity);
        		double reachSq =theExtendedReachWeapon.getReach()*theExtendedReachWeapon.getReach();
        		if (reachSq >= distanceSq)
        		{
                	thePlayer.attackTargetEntityWithCurrentItem(theEntity);
        		}
        	}
            return null; // no response in this case
        }
    }
}
