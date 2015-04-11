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

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */

/*
 * See tutorial from herbix at http://www.minecraftforge.net/forum/index.php/topic,28714.0.html
 */
@SideOnly(Side.CLIENT)
public class MyModelLoader implements ICustomModelLoader 
{

    private IResourceManager resourceManager;
    
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) 
    {
        resourceManager = resourceManager;
    }

    @Override
    public boolean accepts(ResourceLocation l) 
    {
        return l.getResourceDomain().equals("mymodid") && l.getResourcePath().startsWith("models/block/builtin/");
    }

    @Override
    public IModel loadModel(ResourceLocation l) 
    {
        String r = l.getResourcePath().substring("models/block/builtin/".length());
        if(r.equals("mymodel")) {
            return new MyBlockModel(resourceManager);
        }
        throw new RuntimeException("A builtin model '" + r + "' is not defined.");
    }
}