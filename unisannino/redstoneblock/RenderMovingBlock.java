package unisannino.redstoneblock;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class RenderMovingBlock extends Render
{
    private RenderBlocks renderBlocks;

    public RenderMovingBlock()
    {
        renderBlocks = new RenderBlocks();
        shadowSize = 0.5F;
    }

    public void doRenderFallingBlock(EntityMovingBlock entity, double d, double d1, double d2,
            float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        loadTexture("/terrain.png");
        Block block = Block.blocksList[Mod_RSB_Core.RedstoneBlock.blockID];
        World world = entity.getWorld();
        GL11.glDisable(GL11.GL_LIGHTING);
        renderBlockFalling(block, entity, world, MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        doRenderFallingBlock((EntityMovingBlock)entity, d, d1, d2, f, f1);
    }

    private void renderBlockFalling(Block block, EntityMovingBlock entity, World world, int i, int j, int k)
    {
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, i, j, k));
        int i1 = block.colorMultiplier(world, i, j, k);
        float fR = (i1 >> 16 & 0xff) / 255F;
        float fG = (i1 >> 8 & 0xff) / 255F;
        float fB = (i1 & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (fR * 30F + fG * 59F + fB * 11F) / 100F;
            float f5 = (fR * 30F + fG * 70F) / 100F;
            float f6 = (fR * 30F + fB * 70F) / 100F;
            fR = f4;
            fG = f5;
            fB = f6;
        }
        if(entity.moveTime < 900 || (entity.moveTime > 900 && entity.moveTime%10 > 5))
        tessellator.setColorOpaque_F(1.0F * fR, 1.0F * fG, 1.0F * fB);
        float f4 = 1.0F;
        float f5 = 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        //tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
        renderBlocks.renderBottomFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(0, 3));
        f5 = 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        //tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
        renderBlocks.renderTopFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(1, 3));
        f5 = 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        //tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderBlocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(2, 3));
        f5 = 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        //tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderBlocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(3, 3));
        f5 = 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        //tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderBlocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(4, 3));
        f5 = 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        //tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderBlocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSideAndMetadata(5, 3));
        tessellator.draw();
    }
}
