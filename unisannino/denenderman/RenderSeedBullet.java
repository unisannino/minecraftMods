package unisannino.denenderman;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSeedBullet extends Render
{
    /**
     * Have the icon index (in items.png) that will be used to render the image. Currently, eggs and snowballs uses this
     * classes.
     */
    private int itemIconIndex;

    public RenderSeedBullet()
    {
    	itemIconIndex = 0;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        loadTexture("/gui/items.png");
        Tessellator tessellator = Tessellator.instance;

        if(par1Entity != null && par1Entity instanceof EntitySeedBullet)
        {
        	EntitySeedBullet seed = (EntitySeedBullet)par1Entity;
        	if(seed.type == 1)
        	{
        		this.itemIconIndex = Item.seeds.getIconFromDamage(0);
        	}else
        	if(seed.type == 2)
        	{
        		this.itemIconIndex = Item.melonSeeds.getIconFromDamage(0);
        	}else
        	if(seed.type == 3)
        	{
        		this.itemIconIndex = Item.pumpkinSeeds.getIconFromDamage(0);
        	}else
        	{
        		this.itemIconIndex = Item.seeds.getIconFromDamage(0);
        	}
        }

        func_40265_a(tessellator, itemIconIndex);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private void func_40265_a(Tessellator par1Tessellator, int par2)
    {
        float f = ((par2 % 16) * 16 + 0) / 256F;
        float f1 = ((par2 % 16) * 16 + 16) / 256F;
        float f2 = ((par2 / 16) * 16 + 0) / 256F;
        float f3 = ((par2 / 16) * 16 + 16) / 256F;
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
        par1Tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
        par1Tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
        par1Tessellator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, f1, f2);
        par1Tessellator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, f, f2);
        par1Tessellator.draw();
    }
}
