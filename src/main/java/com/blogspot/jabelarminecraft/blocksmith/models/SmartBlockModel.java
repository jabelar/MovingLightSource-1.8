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

package com.blogspot.jabelarminecraft.blocksmith.models;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Function;

/**
 * @author jabelar
 *
 */

/*
 * See tutorial from herbix at http://www.minecraftforge.net/forum/index.php/topic,28714.0.html
 */
@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public abstract class SmartBlockModel implements IFlexibleBakedModel, ISmartBlockModel 
{

    private final boolean isAmbientOcclusion;
    private final boolean isGui3d;
    private final TextureAtlasSprite texture;
    private final VertexFormat format;
    protected Function<ResourceLocation, TextureAtlasSprite> textureGetter;

    public SmartBlockModel(
            boolean ambientOcclusion,
            boolean gui3d,
            TextureAtlasSprite texture,
            VertexFormat format,
            Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) 
    {
        this.isAmbientOcclusion = ambientOcclusion;
        this.isGui3d = gui3d;
        this.texture = texture;
        this.format = format;
        this.textureGetter = bakedTextureGetter;
    }

    @Override
    public boolean isAmbientOcclusion() 
    {
        return isAmbientOcclusion;
    }

    @Override
    public boolean isGui3d() 
    {
        return isGui3d;
    }

    @Override
    public boolean isBuiltInRenderer() 
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getTexture() 
    {
        return texture;
    }

    @Override
    public VertexFormat getFormat() 
    {
        return format;
    }

	@Override
	public ItemCameraTransforms getItemCameraTransforms() 
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing side) 
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<BakedQuad> getGeneralQuads() 
	{
		return Collections.EMPTY_LIST;
	}
}