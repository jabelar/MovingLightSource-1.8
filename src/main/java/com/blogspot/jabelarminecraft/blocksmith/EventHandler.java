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

	If you're interested in licensing the code under different terms you can
	contact the author at julian_abelar@hotmail.com 
*/

package com.blogspot.jabelarminecraft.blocksmith;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.fml.client.GuiIngameModOptions;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.blogspot.jabelarminecraft.blocksmith.gui.GuiConfig;

public class EventHandler 
{
    /*
     * Miscellaneous events
     */    

//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ForceChunkEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(UnforceChunkEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(AnvilUpdateEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(CommandEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ServerChatEvent event)
//    {
//        
//    }
//    
//    /*
//     * Brewing events
//     */
//        
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PotionBrewedEvent event)
//    {
//        
//    }
//    
//    /*
//     * Entity related events
//     */
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(EnteringChunk event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(EntityConstructing event)
//    {
//        // Register extended entity properties
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(EntityJoinWorldEvent event)
//    {
//
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(EntityStruckByLightningEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlaySoundAtEntityEvent event)
//    {
//        
//    }
//
//    /*
//     * Item events (these extend EntityEvent)
//     */
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ItemExpireEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ItemTossEvent event)
//    {
//        
//    }
//    
//    /*
//     * Living events (extend EntityEvent)
//     */
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(LivingJumpEvent event)
//    {
//
//    }
//
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(LivingUpdateEvent event)
    {
        // This event has an Entity variable, access it like this: event.entity;
        // and can check if for player with if (event.entity instanceof EntityPlayer)
		
    	if (event.entityLiving instanceof EntityPlayer)
    	{
    		EntityPlayer thePlayer = (EntityPlayer)event.entityLiving;
    		if (thePlayer.ticksExisted > 5) // make sure there is a valid prev position, don't run on first tick
    		{
	    		thePlayer.setPosition(thePlayer.prevPosX, thePlayer.prevPosY, thePlayer.prevPosZ);
    		}
    	}
    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(EnderTeleportEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(LivingAttackEvent event)
//    {
//
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(LivingDeathEvent event)
//    {
//        
//    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(LivingDropsEvent event)
    {
		for (EntityItem dropItem: event.drops)
		{
			if (dropItem.getEntityItem().getItem() == Items.leather)
			{
				int stackSize = dropItem.getEntityItem().stackSize;

				if (event.entityLiving instanceof EntityCow)
				{
					dropItem.setEntityItemStack(new ItemStack(BlockSmith.cowHide, stackSize));
				}
				if (event.entityLiving instanceof EntityHorse)
				{
					dropItem.setEntityItemStack(new ItemStack(BlockSmith.horseHide, stackSize));
				}
				if (event.entityLiving instanceof EntityMooshroom)
				{
					dropItem.setEntityItemStack(new ItemStack(BlockSmith.cowHide, stackSize));
				}
			}
    	}
    	
		if (event.entityLiving instanceof EntityPig)
		{
			event.drops.add(new EntityItem(
					event.entityLiving.worldObj, 
					event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, 
					new ItemStack(BlockSmith.pigSkin)));
		}
		else if (event.entityLiving instanceof EntitySheep)
		{
			event.drops.add(new EntityItem(
					event.entityLiving.worldObj, 
					event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, 
					new ItemStack(BlockSmith.sheepSkin)));
		}
    }
    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(LivingFallEvent event)
//    {
//
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(LivingHurtEvent event)
//    {
//
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(LivingPackSizeEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(LivingSetAttackTargetEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ZombieEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(CheckSpawn event)
//    {  	
//
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(SpecialSpawn event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(AllowDespawn event)
//    {
//        
//    }
//    
//    /*
//     * Player events (extend LivingEvent)
//     */
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(BreakSpeed event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(Clone event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(HarvestCheck event)
//    {
//        
//    }
    
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(NameFormat event)
    {
    	// DEBUG
    	System.out.println("NameFormat event for username = "+event.username);
        if (event.username.equalsIgnoreCase("jnaejnae"))
        {
            event.displayname = event.username+" the Great and Powerful";
        }        
        else if (event.username.equalsIgnoreCase("MistMaestro"))
        {
            event.displayname = event.username+" the Wise";
        }    
        else if (event.username.equalsIgnoreCase("Taliaailat"))
        {
            event.displayname = event.username+" the Beautiful";
        }    
        else
        {
            event.displayname = event.username+" the Ugly";            
        }
    }
    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ArrowLooseEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ArrowNockEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(AttackEntityEvent event)
//    {
//
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(BonemealEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(EntityInteractEvent event)
//    {
//
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(EntityItemPickupEvent event)
//    {
//
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FillBucketEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ItemTooltipEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerDestroyItemEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerDropsEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerFlyableFallEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerInteractEvent event)
//    {
//
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerOpenContainerEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerPickupXpEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerSleepInBedEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerUseItemEvent.Finish event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerUseItemEvent.Start event)
//    {
//    	
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerUseItemEvent.Stop event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerUseItemEvent.Tick event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(UseHoeEvent event)
//    {
//        
//    }
//    
//    /*
//     * Minecart events (extends EntityEvent)
//     */
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(MinecartCollisionEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(MinecartInteractEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(MinecartUpdateEvent event)
//    {
//        
//    }
//    
//    /*
//     * World events
//     */
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(WorldEvent.Load event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(WorldEvent.PotentialSpawns event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(WorldEvent.Unload event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(BlockEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(BlockEvent.BreakEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(BlockEvent.HarvestDropsEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ChunkEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ChunkEvent.Save event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ChunkEvent.Unload event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ChunkDataEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ChunkDataEvent.Load event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ChunkDataEvent.Save event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ChunkWatchEvent event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ChunkWatchEvent.Watch event)
//    {
//        
//    }
//    
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ChunkWatchEvent.UnWatch event)
//    {
//        
//    }
//
//
//    /*
//     * Client events
//     */    
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ClientChatReceivedEvent event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(DrawBlockHighlightEvent event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderFogEvent event)
//    {
//        
//    }
//    
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FogDensity event)
//    {
//
//    }
//
//    
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FogColors event)
//    {
//
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FOVUpdateEvent event)
//    {
//        
//    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(GuiOpenEvent event)
    {
        if (event.gui instanceof GuiIngameModOptions)
        {
            System.out.println("GuiOpenEvent for GuiIngameModOptions");
            event.gui = new GuiConfig(null);        
        }
    }

//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(GuiScreenEvent.ActionPerformedEvent event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(GuiScreenEvent.DrawScreenEvent event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(GuiScreenEvent.InitGuiEvent event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(MouseEvent event)
//    {
//
//    }
//    
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderGameOverlayEvent event)
//    {
//        
//    }
//    
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderGameOverlayEvent.Chat event)
//    {
//    	// This event actually extends Pre
//
//    }
//    
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderGameOverlayEvent.Post event)
//    {
//        
//    }
//    
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderGameOverlayEvent.Pre event)
//    {
//    	// you can check which elements of the GUI are being rendered
//    	// by checking event.type against things like ElementType.CHAT, ElementType.CROSSHAIRS, etc.
//    	// Note that ElementType.All is fired first apparently, then individual elements
//    }
//    
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderGameOverlayEvent.Text event)
//    {
//    	// This event actually extends Pre
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderHandEvent event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderLivingEvent.Post event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderLivingEvent.Pre event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderPlayerEvent.Post event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderPlayerEvent.Pre event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderPlayerEvent.SetArmorModel event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderWorldEvent.Post event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderWorldEvent.Pre event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderWorldLastEvent event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(TextureStitchEvent.Post event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(TextureStitchEvent.Pre event)
//    {
//        
//    }
//    
//    /*
//     * Fluid events
//     */
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FluidEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FluidContainerRegisterEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FluidDrainingEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FluidFillingEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FluidMotionEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FluidRegisterEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(FluidSpilledEvent event)
//    {
//        
//    }
//
//    /*
//     * Ore dictionary events
//     */
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(OreRegisterEvent event)
//    {
//        
//    }
//    
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(PopulateChunkEvent event)
//	{
//		
//	}
//	
//	// for some reason the PopulateChunkEvents are fired on the main EVENT_BUT
//	// even though they are in the terraingen package
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(PopulateChunkEvent.Populate event)
//	{
//		
//	}
//	
//	// for some reason the PopulateChunkEvents are fired on the main EVENT_BUT
//	// even though they are in the terraingen package
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(PopulateChunkEvent.Post event)
//	{ 
//		
//	}
//	
//	// for some reason the PopulateChunkEvents are fired on the main EVENT_BUT
//	// even though they are in the terraingen package
//	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//	public void onEvent(PopulateChunkEvent.Pre event)
//	{
//		
//	}
}

