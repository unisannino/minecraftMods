package unisannino.denenderman;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSeedBullet extends Render
{
    public RenderSeedBullet()
    {

    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
    	this.renderingBullet((EntitySeedBullet)par1Entity, par2, par4, par6, par8, par9);
    }

    private void renderingBullet(EntitySeedBullet bullet, double x, double y, double z, float par8, float par9)
    {
    	Icon icon = Item.seeds.getIconFromDamage(0);

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(bullet.prevRotationYaw + (bullet.rotationYaw - bullet.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(bullet.prevRotationPitch + (bullet.rotationPitch - bullet.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
        this.loadTexture("/mods/denender/textures/items/shootedbullet.png");
        Tessellator tessellator = Tessellator.instance;

        int k1 = ItemSeedBullet.colorTable[bullet.getBulletType()];
        float f3 = 1.0F;
        float f9 = (k1 >> 16 & 0xff) / 255F;
        float f12 = (k1 >> 8 & 0xff) / 255F;
        float f14 = (k1 & 0xff) / 255F;
        GL11.glColor4f(f9 * f3, f12 * f3, f14 * f3, 1.0F);

        float minU = 0.0f;
        float maxU = 17.0f / 32.0f;
        float minV = 0.0f;
        float maxV = 12.0f / 32.0f;
        float size = 0.0425f;

        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0f, 0.1425f, 0.0f);
        GL11.glScalef(size, size, size);
        for (int i = 0; i < 4; ++i)
        {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(1.0F, 0.0F, 0.0F);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double)minU, (double)minV);
            tessellator.addVertexWithUV(8.0D, -2.0D, 0.0D, (double)maxU, (double)minV);
            tessellator.addVertexWithUV(8.0D, 2.0D, 0.0D, (double)maxU, (double)maxV);
            tessellator.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double)minU, (double)maxV);
            tessellator.draw();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

}
