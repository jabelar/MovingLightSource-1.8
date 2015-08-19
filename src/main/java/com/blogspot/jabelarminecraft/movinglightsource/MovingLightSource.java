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

import java.io.File;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import com.blogspot.jabelarminecraft.movinglightsource.blocks.BlockMovingLightSource;
import com.blogspot.jabelarminecraft.movinglightsource.proxy.CommonProxy;

@Mod(modid = 
      MovingLightSource.MODID, 
      name = MovingLightSource.MODNAME, 
      version = MovingLightSource.MODVERSION)
public class MovingLightSource
{
    public static final String MODID = "movinglightsource";
    public static final String MODNAME = "Jabelar's Moving Light Source";
    public static final String MODVERSION = "1.0.1";
    public static final String MODDESCRIPTION = "Let there be light!";
    public static final String MODAUTHOR = "jabelar";
    public static final String MODCREDITS = "";
    public static final String MODURL = "www.jabelarminecraft.blogspot.com";
    public static final String MODLOGO = "modconfigraphic.png";

    // this is tag used for sub-compound in extended properties and packet syncing
	public final static String EXT_PROPS_NAME = "extendedPropertiesMagicBeans";
 
	// use a named channel to identify packets related to this mod
    public static final String NETWORK_CHANNEL_NAME = "MovingLightSource";
	public static FMLEventChannel channel;

	// networking
	public static SimpleNetworkWrapper network;

    // set up configuration properties (will be read from config file in preInit)
    public static File configFile;
    public static Configuration config;
    public static boolean allowDeconstructUnrealistic = false;
    public static boolean allowDeconstructEnchantedBooks = true;
    public static boolean allowHorseArmorCrafting = true;
    public static boolean allowPartialDeconstructing = true;
    
    // instantiate materials
//    public final static MaterialTanningRack materialTanningRack = new MaterialTanningRack();
    // see custom armor tutorial at: http://bedrockminer.jimdo.com/modding-tutorials/basic-modding/custom-armor/
    // public final static ArmorMaterial SAFEFALLINGLEATHER = EnumHelper.addArmorMaterial("SAFEFALLINGLEATHER", "safe_falling", 5, new int[]{2, 6, 5, 2}, 15);
    
    // instantiate blocks
    // need to instantiate some blocks before related item if the item constructor associates with block
    public final static BlockMovingLightSource blockMovingLightSource = new BlockMovingLightSource();
	
    // instantiate items
	// important to do this after blocks where item is associated with custom block, like with crop
//	public final static ItemCowHide cowHide = new ItemCowHide();
    
	// initiate spawn egg items
   
    // instantiate structures
    // important to do this after blocks in case structure uses custom block

	// instantiate achievements
    public static Achievement achievementTanningAHide;
    public static Achievement craftTable;
    public static Achievement deconstructAny;
    public static Achievement deconstructDiamondHoe;
    public static Achievement deconstructJunk;
    public static Achievement deconstructDiamondShovel;
    public static Achievement theHatStandAchievement;
    
    // enumerate guis
    public enum GUI_ENUM 
    {
        GRINDER, COMPACTOR, DECONSTRUCTOR, TANNING_RACK, FORGE
    }
    
    public static StatBasic deconstructedItemsStat;
    
    // instantiate the mod
    @Instance(MODID)
    public static MovingLightSource instance;
    
    // create custom creativetab for mod items
        
    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="com.blogspot.jabelarminecraft.movinglightsource.proxy.ClientProxy", serverSide="com.blogspot.jabelarminecraft.movinglightsource.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    // Version checking instance
	public static VersionChecker versionChecker;
	public static boolean haveWarnedVersionOutOfDate = false;
            
    @EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry."
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) 
    {   	
        // DEBUG
        System.out.println("preInit()"+event.getModMetadata().name);
                
        // hard-code mod information so don't need mcmod.info file
        event.getModMetadata().autogenerated = false ; // stops it from complaining about missing mcmod.info
        event.getModMetadata().credits = EnumChatFormatting.BLUE+MODCREDITS;
        event.getModMetadata().authorList.add(EnumChatFormatting.RED+MODAUTHOR);
        event.getModMetadata().description = EnumChatFormatting.YELLOW+MODDESCRIPTION;
        event.getModMetadata().url = MODURL;
        event.getModMetadata().logoFile = MODLOGO;
        
        proxy.fmlLifeCycleEvent(event);
    }

	@EventHandler
    // Do your mod setup. Build whatever data structures you care about. Register recipes."
    // Register network handlers
    public void fmlLifeCycleEvent(FMLInitializationEvent event) 
    {
    	
        // DEBUG
        System.out.println("init()");
        
        proxy.fmlLifeCycleEvent(event);
    }

	@EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void fmlLifeCycle(FMLPostInitializationEvent event) 
	{
        // DEBUG
        System.out.println("postInit()");
        
        proxy.fmlLifeCycleEvent(event);
    }

	@EventHandler
	public void fmlLifeCycle(FMLServerAboutToStartEvent event)
	{
        // DEBUG
        System.out.println("Server about to start");
        
		proxy.fmlLifeCycleEvent(event);
	}

	@EventHandler
	// register server commands
	// refer to tutorial at http://www.minecraftforge.net/wiki/Server_Command#Mod_Implementation
	public void fmlLifeCycle(FMLServerStartingEvent event)
	{
        // DEBUG
        System.out.println("Server starting");
        
		proxy.fmlLifeCycleEvent(event);
	}

	@EventHandler
	public void fmlLifeCycle(FMLServerStartedEvent event)
	{
        // DEBUG
        System.out.println("Server started");
        
		proxy.fmlLifeCycleEvent(event);
	}

	@EventHandler
	public void fmlLifeCycle(FMLServerStoppingEvent event)
	{
        // DEBUG
        System.out.println("Server stopping");
        
		proxy.fmlLifeCycleEvent(event);
	}

	@EventHandler
	public void fmlLifeCycle(FMLServerStoppedEvent event)
	{
        // DEBUG
        System.out.println("Server stopped");
        
		proxy.fmlLifeCycleEvent(event);
	}


    public static void saveProperties()
    {
        try
        {
            config.save();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
