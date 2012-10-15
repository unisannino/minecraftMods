package unisannino.denenderman;

import net.minecraft.src.*;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTreeper extends RenderLiving
{
    private ModelCreeper basecreeper;

    public RenderTreeper()
    {
        super(new ModelCreeper(), 0.5F);
        basecreeper = (ModelCreeper)super.mainModel;
        setRenderPassModel(basecreeper);
    }

    protected void updateTreeperScale(EntityTreeper entitytreeper, float f)
    {
        EntityTreeper entitycreeper1 = entitytreeper;
        float f1 = entitycreeper1.setCreeperFlashTime(f);
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
        float f2 = entitytreeper1.setCreeperFlashTime(f1);

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

    private void LeavesHat(EntityTreeper entityliving, float f)
    {
        super.renderEquippedItems(entityliving, f);
        ItemStack itemstack = new ItemStack(Block.leaves, 1, entityliving.getLeavesType());

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

            renderManager.itemRenderer.renderItem(entityliving, itemstack, 1);
            GL11.glPopMatrix();
        }
    }

    protected void renderEquippedItems(EntityLiving entityliving, float f)
    {
        LeavesHat((EntityTreeper)entityliving, f);
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

    protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return super.shouldRenderPass(entityliving, i, f);
    }

    protected int inheritRenderPass(EntityLiving entityliving, int i, float f)
    {
        return func_27007_b((EntityTreeper)entityliving, i, f);
    }

    public IBlockAccess blockAccess;
}
