/*******************************************************************************
 * Copyright 2014, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package biomesoplenty.core;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import biomesoplenty.api.block.IBOPBlock;
import biomesoplenty.api.particle.BOPParticleTypes;
import biomesoplenty.client.particle.EntityPixieTrailFX;
import biomesoplenty.common.config.MiscConfigurationHandler;
import biomesoplenty.common.entities.EntityPixie;
import biomesoplenty.common.entities.EntityWasp;
import biomesoplenty.common.entities.RenderPixie;
import biomesoplenty.common.entities.RenderWasp;
import biomesoplenty.common.entities.projectiles.EntityDart;
import biomesoplenty.common.entities.projectiles.RenderDart;

public class ClientProxy extends CommonProxy
{
    public static ResourceLocation[] bopTitlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_0.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_1.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_2.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_3.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_4.png"), new ResourceLocation("biomesoplenty:textures/gui/title/background/panorama_5.png")};
    
    @Override
    public void registerRenderers()
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        TextureManager textureManager = minecraft.renderEngine;
        
        if (MiscConfigurationHandler.overrideTitlePanorama)
            GuiMainMenu.titlePanoramaPaths = bopTitlePanoramaPaths;
            
        //Entity rendering and other stuff will go here in future
        RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, new RenderDart(minecraft.getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityWasp.class, new RenderWasp(minecraft.getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityPixie.class, new RenderPixie(minecraft.getRenderManager()));
        
        
        // load the texture for EntityPixieTrailFX
        EntityPixieTrailFX.texture = new SimpleTexture(EntityPixieTrailFX.textureLocation);
        textureManager.loadTexture(EntityPixieTrailFX.textureLocation, EntityPixieTrailFX.texture);

    }
    
    @Override
    public void registerItemVariantModel(Item item, String name, int metadata) 
    {
        if (item != null) 
        { 
            ModelBakery.addVariantName(item, BiomesOPlenty.MOD_ID + ":" + name);
            ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(BiomesOPlenty.MOD_ID + ":" + name, "inventory"));
        }
    }
    
    @Override
    public void registerNonRenderingProperties(Block block) 
    {
        if (block instanceof IBOPBlock)
        {
            IBOPBlock bopBlock = (IBOPBlock)block;
            IProperty[] nonRenderingProperties = bopBlock.getNonRenderingProperties();
            
            if (nonRenderingProperties != null)
            {
                // use a custom state mapper which will ignore the properties specified in the block as being non-rendering
                IStateMapper custom_mapper = (new StateMap.Builder()).addPropertiesToIgnore(nonRenderingProperties).build();
                ModelLoader.setCustomStateMapper(block, custom_mapper);
            }
        }
    }
    
    @Override
    public void spawnParticle(BOPParticleTypes type, double x, double y, double z)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityFX entityFx = null;
        switch (type)
        {
            case PIXIETRAIL:
                entityFx = new EntityPixieTrailFX(minecraft.theWorld, x, y, z, MathHelper.getRandomDoubleInRange(minecraft.theWorld.rand, -0.03, 0.03), -0.02D, MathHelper.getRandomDoubleInRange(minecraft.theWorld.rand, -0.03, 0.03));
        }
        
        if (entityFx != null) {minecraft.effectRenderer.addEffect(entityFx);}
    }

}
