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

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel.Builder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import com.google.common.base.Function;

/**
 * @author jabelar
 *
 */

/*
 * See tutorial from herbix at http://www.minecraftforge.net/forum/index.php/topic,28714.0.html
 */
public class MyBakedBlockModel extends SmartBlockModel 
{

	public MyBakedBlockModel(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) 
	{
		super(false, false, bakedTextureGetter.apply(MyBlockModel.TA), format, bakedTextureGetter);
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		Builder theBuilder = new Builder(this, null);
		theBuilder.setTexture(this.getTexture());
		// ...
		BakedQuad myquad = new BakedQuad(null, 0, EnumFacing.NORTH); // ...;
		// ...
		theBuilder.addGeneralQuad(myquad);
		// ...
		// myquad = ...;
		// ...
		theBuilder.addFaceQuad(EnumFacing.UP, myquad);
		// ...
		return theBuilder.makeBakedModel();
	}
}