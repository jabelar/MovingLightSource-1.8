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

package com.blogspot.jabelarminecraft.movinglightsource;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import com.blogspot.jabelarminecraft.movinglightsource.blocks.BlockMovingLightSource;


public class FMLEventHandler 
{
    
    /*
     * Common events
     */

    // events in the cpw.mods.fml.common.event package are actually handled with
    // @EventHandler annotation in the main mod class or the proxies.
    
    /*
     * Game input events
     */

//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(InputEvent event)
//    {
//        
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(KeyInputEvent event)
//    {
//
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(MouseInputEvent event)
//    {
//
//    }
//    
//    /*
//     * Player events
//     */
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ItemCraftedEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ItemPickupEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ItemSmeltedEvent event)
//    {
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerChangedDimensionEvent event)
//    {
//        
//    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(PlayerLoggedInEvent event)
    {
        if (event.player.getDisplayName().equals("MistMaestro"))
        {
            // DEBUG
            System.out.println("Welcome Master!");
        }
        
        // DEBUG
//        System.out.println("WorldData hasCastleSpawned ="+WorldData.get(((EntityPlayerSP)thePlayer).movementInput.worldObj).getHasCastleSpwaned()+
//                ", familyCowHasGivenLead ="+WorldData.get(((EntityPlayerSP)thePlayer).movementInput.worldObj).getFamilyCowHasGivenLead());
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(PlayerLoggedOutEvent event)
    {
        // DEBUG
        System.out.println("Player logged out");
        
    }

//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PlayerRespawnEvent event)
//    {
//        // DEBUG
//        System.out.println("The memories of past existences are but glints of light.");
//        
//    }
//
//    /*
//     * Tick events
//     */
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ClientTickEvent event) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
//    {
//        if (event.phase == TickEvent.Phase.END) // only proceed if START phase otherwise, will execute twice per tick
//        {
//            return;
//        }    
//
//    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(PlayerTickEvent event)
    {        
        if (event.phase == TickEvent.Phase.START && event.player.worldObj.isRemote) // only proceed if START phase otherwise, will execute twice per tick
        {
            EntityPlayer thePlayer = event.player;
            if (!MovingLightSource.haveWarnedVersionOutOfDate && !MovingLightSource.versionChecker.isLatestVersion())
            {
                ClickEvent versionCheckChatClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, "http://jabelarminecraft.blogspot.com");
                ChatStyle clickableChatStyle = new ChatStyle().setChatClickEvent(versionCheckChatClickEvent);
                ChatComponentText versionWarningChatComponent = new ChatComponentText("Your Magic Beans Mod is not latest version!  Click here to update.");
                versionWarningChatComponent.setChatStyle(clickableChatStyle);
                thePlayer.addChatMessage(versionWarningChatComponent);
                MovingLightSource.haveWarnedVersionOutOfDate = true;
            }
        }
        else if (event.phase == TickEvent.Phase.START && !event.player.worldObj.isRemote)
        {
            if (event.player.getCurrentEquippedItem() != null)
            {
                if (BlockMovingLightSource.isLightEmittingItem(event.player.getCurrentEquippedItem().getItem()))
                {
                    int blockX = MathHelper.floor_double(event.player.posX);
                    int blockY = MathHelper.floor_double(event.player.posY-0.2D - event.player.getYOffset());
                    int blockZ = MathHelper.floor_double(event.player.posZ);
                    // place light at head level
                    BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up();
                    if (event.player.worldObj.getBlockState(blockLocation).getBlock() == Blocks.air)
                    {
                        event.player.worldObj.setBlockState(blockLocation, MovingLightSource.blockMovingLightSource.getDefaultState());
                    }
                    else 
                        if (event.player.worldObj.getBlockState(blockLocation.add(event.player.getLookVec().xCoord, event.player.getLookVec().yCoord, event.player.getLookVec().zCoord)).getBlock() == Blocks.air)
                    {
                        event.player.worldObj.setBlockState(blockLocation.add(event.player.getLookVec().xCoord, event.player.getLookVec().yCoord, event.player.getLookVec().zCoord), MovingLightSource.blockMovingLightSource.getDefaultState());
                    }
                }
            }
        }
    }

//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RenderTickEvent event)
//    {
//        if (event.phase == TickEvent.Phase.END) // only proceed if START phase otherwise, will execute twice per tick
//        {
//            return;
//        }
//        
//    }
//
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(ServerTickEvent event)
//    {
//        if (event.phase == TickEvent.Phase.END) // only proceed if START phase otherwise, will execute twice per tick
//        {
//            return;
//        }    
//        
//    }

//  @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//  public void onEvent(WorldTickEvent event)
//  {
//      if (event.phase == TickEvent.Phase.END) // only proceed if START phase otherwise, will execute twice per tick
//      {
//          return;
//      }          
//  }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(OnConfigChangedEvent eventArgs) 
    {
        // DEBUG
        System.out.println("OnConfigChangedEvent");
        if(eventArgs.modID.equals(MovingLightSource.MODID))
        {
            System.out.println("Syncing config for mod ="+eventArgs.modID);
            MovingLightSource.config.save();
            MovingLightSource.proxy.syncConfig();
        }
    }

//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(PostConfigChangedEvent eventArgs) 
//    {
//        // useful for doing something if another mod's config has changed
//        // if(eventArgs.modID.equals(MagicBeans.MODID))
//        // {
//        //        // do whatever here
//        // }
//    }
}
