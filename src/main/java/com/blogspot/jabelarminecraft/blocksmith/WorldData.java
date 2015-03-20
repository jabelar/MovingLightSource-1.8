package com.blogspot.jabelarminecraft.blocksmith;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class WorldData extends WorldSavedData 
{

	private static final String IDENTIFIER = BlockSmith.MODID;
	
//	private boolean hasCastleSpawned = false;
//	private boolean familyCowHasGivenLead = false;
	
	public WorldData() 
	{
		this(IDENTIFIER);
	}
	
	public WorldData(String parIdentifier) 
	{
		super(parIdentifier);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		// DEBUG
		System.out.println("WorldData readFromNBT");
		
//		hasCastleSpawned = nbt.getBoolean("hasCastleSpawned");
//		familyCowHasGivenLead = nbt.getBoolean("familyCowHasGivenLead");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		// DEBUG
		System.out.println("MagicBeansWorldData writeToNBT");
		
//		nbt.setBoolean("hasCastleSpawned", hasCastleSpawned);
//		nbt.setBoolean("familyCowHasGivenLead", familyCowHasGivenLead);
	}
	
//	public boolean getHasCastleSpwaned() 
//	{
//		return hasCastleSpawned;
//	}
//	
//	public void setHasCastleSpawned(boolean parHasCastleSpawned) 
//	{
//		// DEBUG
//		System.out.println("World Data setHasCastleSpawned = "+parHasCastleSpawned);
//		if (!hasCastleSpawned) 
//		{
//			hasCastleSpawned = true;
//			markDirty();
//			// new PacketWorldData(this).sendToAll(); shouldn't need to send packet as this field is only used on server side
//		}
//	}
//	
//	public boolean getFamilyCowHasGivenLead() 
//	{
//		return familyCowHasGivenLead;
//	}
//	
//	public void setFamilyCowHasGivenLead(boolean parFamilyCowHasGivenLead) 
//	{
//		// DEBUG
//		System.out.println("World Data familyCowHasGivenLead = "+parFamilyCowHasGivenLead);
//		if (!familyCowHasGivenLead) 
//		{
//			familyCowHasGivenLead = true;
//			markDirty();
//			// new PacketWorldData(this).sendToAll(); shouldn't need to send packet as this field is only used on server side
//		}
//	}
		
	public static WorldData get(World world) 
	{
		WorldData data = (WorldData)world.loadItemData(WorldData.class, IDENTIFIER);
		if (data == null) 
		{
			// DEBUG
			System.out.println("WorldData didn't exist so creating it");
			
			data = new WorldData();
			world.setItemData(IDENTIFIER, data);
		}
		return data;
	}
}