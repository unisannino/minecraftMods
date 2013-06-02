package unisannino.denenderman;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

    private void leavesHat(EntityTreeper treeper, float f)
    {
        super.renderEquippedItems(treeper, f);
        ItemStack itemstack = new ItemStack(Block.leaves, 1, treeper.getLeaveType());
        float randcolor = (float) treeper.getRNG().nextGaussian();

        if (itemstack != null && itemstack.getItem().itemID < 256)
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

    protected int inheritRenderPass(EntityLiving entityliving, int i, float f)
    {
        return func_27007_b((EntityTreeper)entityliving, i, f);
    }
}
