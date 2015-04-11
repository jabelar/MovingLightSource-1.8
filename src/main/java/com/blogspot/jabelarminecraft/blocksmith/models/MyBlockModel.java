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

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
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
@SideOnly(Side.CLIENT)
public class MyBlockModel implements IModel 
{

    public static final ResourceLocation TA = new ResourceLocation("mymodid:blocks/texturea");
    public static final ResourceLocation TB = new ResourceLocation("mymodid:blocks/textureb");
    public static final ResourceLocation TC = new ResourceLocation("mymodid:blocks/texturec");
    public static final ResourceLocation MA = new ResourceLocation("mymodid:block/modela");
    public static final ResourceLocation MB = new ResourceLocation("mymodid:block/modelb");
    public static final ResourceLocation MC = new ResourceLocation("mymodid:block/modelc");

    public MyBlockModel(IResourceManager resourceManager) 
    {

    }

    @Override
    public Collection<ResourceLocation> getDependencies() 
    {
        return Arrays.asList(MA, MB, MC);
    }

    @Override
    public Collection<ResourceLocation> getTextures() 
    {
        return Arrays.asList(TA, TB, TC);
    }

    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) 
    {
        return new MyBakedBlockModel(format, bakedTextureGetter);
    }

    @Override
    public IModelState getDefaultState() {
        return null;
    }
}