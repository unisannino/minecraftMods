package unisannino.redstoneblock;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class Mod_RSB_BlockRender implements ISimpleBlockRenderingHandler
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
        Tessellator tessellator = Tessellator.instance;

        if (modelID == Mod_RSB_Core.ModelID)
        {
            if (modelID == 0)
            {
                block.setBlockBoundsForItemRender();
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, modelID));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            if (modelID == 1)
            {
                for (int l = 0; l < 2; l++)
                {
                    if (l == 0)
                    {
                        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
                    }

                    if (l == 1)
                    {
                        block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
                    }

                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, -1F, 0.0F);
                    renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, 1.0F, 0.0F);
                    renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, 0.0F, -1F);
                    renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, 0.0F, 1.0F);
                    renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(-1F, 0.0F, 0.0F);
                    renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(1.0F, 0.0F, 0.0F);
                    renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
                    tessellator.draw();
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                }
            }

            if (modelID == 2)
            {
                for (int j1 = 0; j1 < 4; j1++)
                {
                    float f4 = 0.125F;

                    if (j1 == 0)
                    {
                        block.setBlockBounds(0.5F - f4, 0.0F, 0.0F, 0.5F + f4, 1.0F, f4 * 2.0F);
                    }

                    if (j1 == 1)
                    {
                        block.setBlockBounds(0.5F - f4, 0.0F, 1.0F - f4 * 2.0F, 0.5F + f4, 1.0F, 1.0F);
                    }

                    f4 = 0.0625F;

                    if (j1 == 2)
                    {
                        block.setBlockBounds(0.5F - f4, 1.0F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 1.0F - f4, 1.0F + f4 * 2.0F);
                    }

                    if (j1 == 3)
                    {
                        block.setBlockBounds(0.5F - f4, 0.5F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 0.5F - f4, 1.0F + f4 * 2.0F);
                    }

                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, -1F, 0.0F);
                    renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, 1.0F, 0.0F);
                    renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, 0.0F, -1F);
                    renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, 0.0F, 1.0F);
                    renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(-1F, 0.0F, 0.0F);
                    renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
                    tessellator.draw();
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(1.0F, 0.0F, 0.0F);
                    renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
                    tessellator.draw();
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                }

                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            if (modelID == 7)
            {
                float f = 0.5F;
                block.setBlockBounds(f, 0.0F, 0.0F, f + 0.2F, 1.0F, 1.0F);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, modelID));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, modelID));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
        }
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
        int m = world.getBlockMetadata(x, y, z);

        if (modelId == Mod_RSB_Core.ModelID)
        {
            if (m == 0 || m == 8)
            {
                return renderer.renderStandardBlock(block, x, y, z);
            }

            if ((m > 2 && m < 7) || (m > 8 && m < 13))
            {
                return this.renderBlockStairsR(renderer, world, block, x, y, z);
            }

            if (m == 2)
            {
                return this.renderBlockFenceR(renderer, world, (BlockRedNoCube)block, x, y, z);
            }

            if (m == 7)
            {
                return this.renderBlockPaneR(renderer, world, (BlockRedNoCube)block, x, y, z);
            }
        }

        return false;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}

	@Override
	public int getRenderId()
	{
		return 0;
	}

    public boolean renderBlockFenceR(RenderBlocks renderblocks, IBlockAccess iblockaccess, BlockRedNoCube block, int i, int j, int k)
    {
        boolean flag = false;
        float f = 0.375F;
        float f1 = 0.625F;
        block.setBlockBounds(f, 0.0F, f, f1, 1.0F, f1);
        renderblocks.renderStandardBlock(block, i, j, k);
        flag = true;
        boolean flag1 = false;
        boolean flag2 = false;

        if (block.canConnectFenceTo(iblockaccess, i - 1, j, k) || block.canConnectFenceTo(iblockaccess, i + 1, j, k))
        {
            flag1 = true;
        }

        if (block.canConnectFenceTo(iblockaccess, i, j, k - 1) || block.canConnectFenceTo(iblockaccess, i, j, k + 1))
        {
            flag2 = true;
        }

        boolean flag3 = block.canConnectFenceTo(iblockaccess, i - 1, j, k);
        boolean flag4 = block.canConnectFenceTo(iblockaccess, i + 1, j, k);
        boolean flag5 = block.canConnectFenceTo(iblockaccess, i, j, k - 1);
        boolean flag6 = block.canConnectFenceTo(iblockaccess, i, j, k + 1);

        if (!flag1 && !flag2)
        {
            flag1 = true;
        }

        f = 0.4375F;
        f1 = 0.5625F;
        float f2 = 0.75F;
        float f3 = 0.9375F;
        float f4 = flag3 ? 0.0F : f;
        float f5 = flag4 ? 1.0F : f1;
        float f6 = flag5 ? 0.0F : f;
        float f7 = flag6 ? 1.0F : f1;

        if (flag1)
        {
            block.setBlockBounds(f4, f2, f, f5, f3, f1);
            renderblocks.renderStandardBlock(block, i, j, k);
            flag = true;
        }

        if (flag2)
        {
            block.setBlockBounds(f, f2, f6, f1, f3, f7);
            renderblocks.renderStandardBlock(block, i, j, k);
            flag = true;
        }

        f2 = 0.375F;
        f3 = 0.5625F;

        if (flag1)
        {
            block.setBlockBounds(f4, f2, f, f5, f3, f1);
            renderblocks.renderStandardBlock(block, i, j, k);
            flag = true;
        }

        if (flag2)
        {
            block.setBlockBounds(f, f2, f6, f1, f3, f7);
            renderblocks.renderStandardBlock(block, i, j, k);
            flag = true;
        }

        block.setBlockBoundsBasedOnState(iblockaccess, i, j, k);
        return flag;
    }

    public boolean renderBlockStairsR(RenderBlocks renderblocks, IBlockAccess iblockaccess, Block block, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k);
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = 0.5F;
        float f3 = 1.0F;

        if (l > 8)
        {
            f = 0.5F;
            f1 = 1.0F;
            f2 = 0.0F;
            f3 = 0.5F;
        }

        block.setBlockBounds(0.0F, f, 0.0F, 1.0F, f1, 1.0F);
        renderblocks.renderStandardBlock(block, i, j, k);

        if (l == 3 || l == 9)
        {
            block.setBlockBounds(0.5F, f2, 0.0F, 1.0F, f3, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        else if (l == 4 || l == 10)
        {
            block.setBlockBounds(0.0F, f2, 0.0F, 0.5F, f3, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        else if (l == 5 || l == 11)
        {
            block.setBlockBounds(0.0F, f2, 0.5F, 1.0F, f3, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        else if (l == 6 || l == 12)
        {
            block.setBlockBounds(0.0F, f2, 0.0F, 1.0F, f3, 0.5F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }

        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    public boolean renderBlockPaneR(RenderBlocks renderblocks, IBlockAccess iblockaccess, BlockRedNoCube blockpane, int par2, int par3, int par4)
    {
        int i = iblockaccess.getHeight();
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(blockpane.getMixedBrightnessForBlock(iblockaccess, par2, par3, par4));
        float f = 1.0F;
        int j = blockpane.colorMultiplier(iblockaccess, par2, par3, par4);
        float f1 = (j >> 16 & 0xff) / 255F;
        float f2 = (j >> 8 & 0xff) / 255F;
        float f3 = (j & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        boolean flag = false;
        boolean flag1 = false;
        int i1;
        int j1;

        if (renderblocks.overrideBlockTexture >= 0)
        {
            i1 = renderblocks.overrideBlockTexture;
            j1 = renderblocks.overrideBlockTexture;
        }
        else
        {
            int k = iblockaccess.getBlockMetadata(par2, par3, par4);
            i1 = blockpane.getBlockTextureFromSideAndMetadata(0, k);
            j1 = blockpane.getSideTextureIndex();
        }

        int l = (i1 & 0xf) << 4;
        int k1 = i1 & 0xf0;
        double d = l / 256F;
        double d1 = (l + 7.99F) / 256F;
        double d2 = (l + 15.99F) / 256F;
        double d3 = k1 / 256F;
        double d4 = (k1 + 15.99F) / 256F;
        int l1 = (j1 & 0xf) << 4;
        int i2 = j1 & 0xf0;
        double d5 = (l1 + 7) / 256F;
        double d6 = (l1 + 8.99F) / 256F;
        double d7 = i2 / 256F;
        double d8 = (i2 + 8) / 256F;
        double d9 = (i2 + 15.99F) / 256F;
        double d10 = par2;
        double d11 = par2 + 0.5D;
        double d12 = par2 + 1;
        double d13 = par4;
        double d14 = par4 + 0.5D;
        double d15 = par4 + 1;
        double d16 = (par2 + 0.5D) - 0.0625D;
        double d17 = par2 + 0.5D + 0.0625D;
        double d18 = (par4 + 0.5D) - 0.0625D;
        double d19 = par4 + 0.5D + 0.0625D;
        boolean flag2 = blockpane.canThisPaneConnectToThisBlockID(iblockaccess.getBlockId(par2, par3, par4 - 1));
        boolean flag3 = blockpane.canThisPaneConnectToThisBlockID(iblockaccess.getBlockId(par2, par3, par4 + 1));
        boolean flag4 = blockpane.canThisPaneConnectToThisBlockID(iblockaccess.getBlockId(par2 - 1, par3, par4));
        boolean flag5 = blockpane.canThisPaneConnectToThisBlockID(iblockaccess.getBlockId(par2 + 1, par3, par4));
        boolean flag6 = blockpane.shouldSideBeRendered(iblockaccess, par2, par3 + 1, par4, 1);
        boolean flag7 = blockpane.shouldSideBeRendered(iblockaccess, par2, par3 - 1, par4, 0);

        if ((!flag4 || !flag5) && (flag4 || flag5 || flag2 || flag3))
        {
            if (flag4 && !flag5)
            {
                tessellator.addVertexWithUV(d10, par3 + 1, d14, d, d3);
                tessellator.addVertexWithUV(d10, par3 + 0, d14, d, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d, d4);
                tessellator.addVertexWithUV(d10, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d10, par3 + 1, d14, d1, d3);

                if (!flag3 && !flag2)
                {
                    tessellator.addVertexWithUV(d11, par3 + 1, d19, d5, d7);
                    tessellator.addVertexWithUV(d11, par3 + 0, d19, d5, d9);
                    tessellator.addVertexWithUV(d11, par3 + 0, d18, d6, d9);
                    tessellator.addVertexWithUV(d11, par3 + 1, d18, d6, d7);
                    tessellator.addVertexWithUV(d11, par3 + 1, d18, d5, d7);
                    tessellator.addVertexWithUV(d11, par3 + 0, d18, d5, d9);
                    tessellator.addVertexWithUV(d11, par3 + 0, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, par3 + 1, d19, d6, d7);
                }

                if (flag6 || par3 < i - 1 && iblockaccess.isAirBlock(par2 - 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d18, d5, d8);
                }

                if (flag7 || par3 > 1 && iblockaccess.isAirBlock(par2 - 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
                }
            }
            else if (!flag4 && flag5)
            {
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d12, par3 + 0, d14, d2, d4);
                tessellator.addVertexWithUV(d12, par3 + 1, d14, d2, d3);
                tessellator.addVertexWithUV(d12, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d12, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d2, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d2, d3);

                if (!flag3 && !flag2)
                {
                    tessellator.addVertexWithUV(d11, par3 + 1, d18, d5, d7);
                    tessellator.addVertexWithUV(d11, par3 + 0, d18, d5, d9);
                    tessellator.addVertexWithUV(d11, par3 + 0, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, par3 + 1, d19, d6, d7);
                    tessellator.addVertexWithUV(d11, par3 + 1, d19, d5, d7);
                    tessellator.addVertexWithUV(d11, par3 + 0, d19, d5, d9);
                    tessellator.addVertexWithUV(d11, par3 + 0, d18, d6, d9);
                    tessellator.addVertexWithUV(d11, par3 + 1, d18, d6, d7);
                }

                if (flag6 || par3 < i - 1 && iblockaccess.isAirBlock(par2 + 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d18, d5, d7);
                }

                if (flag7 || par3 > 1 && iblockaccess.isAirBlock(par2 + 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d7);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(d10, par3 + 1, d14, d, d3);
            tessellator.addVertexWithUV(d10, par3 + 0, d14, d, d4);
            tessellator.addVertexWithUV(d12, par3 + 0, d14, d2, d4);
            tessellator.addVertexWithUV(d12, par3 + 1, d14, d2, d3);
            tessellator.addVertexWithUV(d12, par3 + 1, d14, d, d3);
            tessellator.addVertexWithUV(d12, par3 + 0, d14, d, d4);
            tessellator.addVertexWithUV(d10, par3 + 0, d14, d2, d4);
            tessellator.addVertexWithUV(d10, par3 + 1, d14, d2, d3);

            if (flag6)
            {
                tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d18, d5, d9);
                tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d18, d5, d9);
            }
            else
            {
                if (par3 < i - 1 && iblockaccess.isAirBlock(par2 - 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, (par3 + 1) + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d18, d5, d8);
                }

                if (par3 < i - 1 && iblockaccess.isAirBlock(par2 + 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d12, (par3 + 1) + 0.01D, d18, d5, d7);
                }
            }

            if (flag7)
            {
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d9);
            }
            else
            {
                if (par3 > 1 && iblockaccess.isAirBlock(par2 - 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                }

                if (par3 > 1 && iblockaccess.isAirBlock(par2 + 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                }
            }
        }

        if ((!flag2 || !flag3) && (flag4 || flag5 || flag2 || flag3))
        {
            if (flag2 && !flag3)
            {
                tessellator.addVertexWithUV(d11, par3 + 1, d13, d, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d13, d, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d13, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d13, d1, d3);

                if (!flag5 && !flag4)
                {
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d7);
                    tessellator.addVertexWithUV(d16, par3 + 0, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 + 0, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d7);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
                    tessellator.addVertexWithUV(d17, par3 + 0, d14, d5, d9);
                    tessellator.addVertexWithUV(d16, par3 + 0, d14, d6, d9);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
                }

                if (flag6 || par3 < i - 1 && iblockaccess.isAirBlock(par2, par3 + 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, par3 + 1, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d13, d6, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d13, d5, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
                }

                if (flag7 || par3 > 1 && iblockaccess.isAirBlock(par2, par3 - 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, par3, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, par3, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, par3, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, par3, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, par3, d14, d6, d7);
                    tessellator.addVertexWithUV(d16, par3, d13, d6, d8);
                    tessellator.addVertexWithUV(d17, par3, d13, d5, d8);
                    tessellator.addVertexWithUV(d17, par3, d14, d5, d7);
                }
            }
            else if (!flag2 && flag3)
            {
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d15, d2, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d15, d2, d3);
                tessellator.addVertexWithUV(d11, par3 + 1, d15, d1, d3);
                tessellator.addVertexWithUV(d11, par3 + 0, d15, d1, d4);
                tessellator.addVertexWithUV(d11, par3 + 0, d14, d2, d4);
                tessellator.addVertexWithUV(d11, par3 + 1, d14, d2, d3);

                if (!flag5 && !flag4)
                {
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
                    tessellator.addVertexWithUV(d17, par3 + 0, d14, d5, d9);
                    tessellator.addVertexWithUV(d16, par3 + 0, d14, d6, d9);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d7);
                    tessellator.addVertexWithUV(d16, par3 + 0, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 + 0, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d7);
                }

                if (flag6 || par3 < i - 1 && iblockaccess.isAirBlock(par2, par3 + 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d8);
                    tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d8);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d8);
                }

                if (flag7 || par3 > 1 && iblockaccess.isAirBlock(par2, par3 - 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, par3, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, par3, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, par3, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, par3, d14, d6, d8);
                    tessellator.addVertexWithUV(d16, par3, d15, d5, d8);
                    tessellator.addVertexWithUV(d16, par3, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, par3, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, par3, d15, d6, d8);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(d11, par3 + 1, d15, d, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d15, d, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d13, d2, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d13, d2, d3);
            tessellator.addVertexWithUV(d11, par3 + 1, d13, d, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d13, d, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d15, d2, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d15, d2, d3);

            if (flag6)
            {
                tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d13, d6, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d13, d5, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d13, d6, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d13, d5, d9);
            }
            else
            {
                if (par3 < i - 1 && iblockaccess.isAirBlock(par2, par3 + 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, par3 + 1, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d13, d6, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d13, d5, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
                }

                if (par3 < i - 1 && iblockaccess.isAirBlock(par2, par3 + 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d8);
                    tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d8);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d8);
                }
            }

            if (flag7)
            {
                tessellator.addVertexWithUV(d17, par3, d15, d6, d9);
                tessellator.addVertexWithUV(d17, par3, d13, d6, d7);
                tessellator.addVertexWithUV(d16, par3, d13, d5, d7);
                tessellator.addVertexWithUV(d16, par3, d15, d5, d9);
                tessellator.addVertexWithUV(d17, par3, d13, d6, d9);
                tessellator.addVertexWithUV(d17, par3, d15, d6, d7);
                tessellator.addVertexWithUV(d16, par3, d15, d5, d7);
                tessellator.addVertexWithUV(d16, par3, d13, d5, d9);
            }
            else
            {
                if (par3 > 1 && iblockaccess.isAirBlock(par2, par3 - 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, par3, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, par3, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, par3, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, par3, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, par3, d14, d6, d7);
                    tessellator.addVertexWithUV(d16, par3, d13, d6, d8);
                    tessellator.addVertexWithUV(d17, par3, d13, d5, d8);
                    tessellator.addVertexWithUV(d17, par3, d14, d5, d7);
                }

                if (par3 > 1 && iblockaccess.isAirBlock(par2, par3 - 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, par3, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, par3, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, par3, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, par3, d14, d6, d8);
                    tessellator.addVertexWithUV(d16, par3, d15, d5, d8);
                    tessellator.addVertexWithUV(d16, par3, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, par3, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, par3, d15, d6, d8);
                }
            }
        }

        return true;
    }

}
