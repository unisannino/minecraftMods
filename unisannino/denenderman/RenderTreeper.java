package unisannino.denenderman;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTreeper extends RenderLiving
{
    private ModelCreeper basecreeper;
    public IBlockAccess blockAccess;
    protected Minecraft mc = FMLClientHandler.instance().getClient();

    public RenderTreeper()
    {
        super(new ModelCreeper(), 0.5F);
        basecreeper = (ModelCreeper)super.mainModel;
        setRenderPassModel(basecreeper);
    }

    protected void updateTreeperScale(EntityTreeper entitytreeper, float f)
    {
        EntityTreeper entitycreeper1 = entitytreeper;
        float f1 = entitycreeper1.setTreeperFlashTime(f);
        float f2 = 1.0F + MathHelper.sin(f1 * 100F) * f1 * 0.01F;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        f1 *= f1;
        f1 *= f1;
        float f3 = (1.0F + f1 * 0.4F) * f2;
        float f4 = (1.0F + f1 * 0.1F) / f2;
        GL11.glScalef(f3, f4, f3);
    }

    protected int updateCreeperColorMultiplier(EntityTreeper entitytreeper, float f, float f1)
    {
        EntityTreeper entitytreeper1 = entitytreeper;
        float f2 = entitytreeper1.setTreeperFlashTime(f1);

        if ((int)(f2 * 10F) % 2 == 0)
        {
            return 0;
        }

        int i = (int)(f2 * 0.2F * 255F);

        if (i < 0)
        {
            i = 0;
        }

        if (i > 255)
        {
            i = 255;
        }

        char c = '\377';
        char c1 = '\377';
        char c2 = '\377';
        return i << 24 | c << 16 | c1 << 8 | c2;
    }

    private void leavesHat(EntityTreeper treeper, float f)
    {
        super.renderEquippedItems(treeper, f);
        ItemStack itemstack = new ItemStack(Block.leaves, 1, treeper.getLeavesType());
        float randcolor = (float) treeper.getRNG().nextGaussian();

        if (itemstack != null && itemstack.getItem().shiftedIndex < 256)
        {
            GL11.glPushMatrix();
            basecreeper.head.postRender(0.0625F);

            if (RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
            {
                float f1 = 1.275F;
                GL11.glTranslatef(0.0F, -1.0F, 0.0F);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f1, -f1, f1);
            }

            renderManager.itemRenderer.renderItem(treeper, itemstack, 1);

            GL11.glPopMatrix();
        }
    }

    protected void renderEquippedItems(EntityLiving entityliving, float f)
    {
        leavesHat((EntityTreeper)entityliving, f);
    }

    protected int func_27007_b(EntityTreeper entitytreeper, int i, float f)
    {
        return -1;
    }

    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        updateTreeperScale((EntityTreeper)entityliving, f);
    }

    protected int getColorMultiplier(EntityLiving entityliving, float f, float f1)
    {
        return updateCreeperColorMultiplier((EntityTreeper)entityliving, f, f1);
    }

    protected int inheritRenderPass(EntityLiving entityliving, int i, float f)
    {
        return func_27007_b((EntityTreeper)entityliving, i, f);
    }

    /*
    public void renderBlockLightAfro(Block par1Block, int par2, float par3)
    {
        Tessellator var4 = Tessellator.instance;
        int var6;
        float var7;
        float var8;
        float var9;

        if (this.renderBlocks.useInventoryTint)
        {
            var6 = par1Block.getRenderColor(par2);

            var7 = (float)(var6 >> 16 & 255) / 255.0F;
            var8 = (float)(var6 >> 8 & 255) / 255.0F;
            var9 = (float)(var6 & 255) / 255.0F;
            GL11.glColor4f(var7 * par3, var8 * par3, var9 * par3, 1.0F);
        }

        var6 = par1Block.getRenderType();
        this.renderBlocks.func_83018_a(par1Block);
        int var14;

        par1Block.setBlockBoundsForItemRender();
        this.renderBlocks.func_83018_a(par1Block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        var4.startDrawingQuads();
        var4.setNormal(0.0F, -1.0F, 0.0F);
        this.renderBlocks.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(0, par2));
        var4.draw();

        if (var5 && this.renderBlocks.useInventoryTint)
        {
            var14 = par1Block.getRenderColor(par2);
            var8 = (float)(var14 >> 16 & 255) / 255.0F;
            var9 = (float)(var14 >> 8 & 255) / 255.0F;
            float var10 = (float)(var14 & 255) / 255.0F;
            GL11.glColor4f(var8 * par3, var9 * par3, var10 * par3, 1.0F);
        }

        var4.startDrawingQuads();
        var4.setNormal(0.0F, 1.0F, 0.0F);
        this.renderBlocks.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(1, par2));
        var4.draw();

        if (var5 && this.renderBlocks.useInventoryTint)
        {
            GL11.glColor4f(par3, par3, par3, 1.0F);
        }

        var4.startDrawingQuads();
        var4.setNormal(0.0F, 0.0F, -1.0F);
        this.renderBlocks.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(2, par2));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(0.0F, 0.0F, 1.0F);
        this.renderBlocks.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(3, par2));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(-1.0F, 0.0F, 0.0F);
        this.renderBlocks.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(4, par2));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(1.0F, 0.0F, 0.0F);
        this.renderBlocks.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(5, par2));
        var4.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
    */
}
