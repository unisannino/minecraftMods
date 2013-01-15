package unisannino.redstoneblock;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderRSBGolem extends RenderLiving
{
	private ModelRSBGolem modelG;

    public RenderRSBGolem()
    {
        super(new ModelRSBGolem(), 0.5F);
        modelG = (ModelRSBGolem)mainModel;
    }

    public void renderGolem(EntityRSBGolem par1EntityGolem, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
        GL11.glPopMatrix();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        super.doRenderLiving(par1EntityGolem, par2, par4, par6, par8, par9);
    }

    protected void func_48420_a(EntityRSBGolem par1EntityIronGolem, float par2, float par3, float par4)
    {
        super.rotateCorpse(par1EntityIronGolem, par2, par3, par4);

        if ((double)par1EntityIronGolem.legYaw < 0.01D)
        {
            return;
        }
        else
        {
            float f = 13F;
            float f1 = (par1EntityIronGolem.legSwing - par1EntityIronGolem.legYaw * (1.0F - par4)) + 6F;
            float f2 = (Math.abs(f1 % f - f * 0.5F) - f * 0.25F) / (f * 0.25F);
            GL11.glRotatef(6.5F * f2, 0.0F, 0.0F, 1.0F);
            return;
        }
    }

    protected void renderName(EntityRSBGolem par1EntityGolem, double par2, double par4, double par6)
    {
        if (Minecraft.isGuiEnabled() && par1EntityGolem != renderManager.livingPlayer)
        {
            float f = 1.6F;
            float f1 = 0.01666667F * f;
            float f2 = par1EntityGolem.getDistanceToEntity(renderManager.livingPlayer);
            float f3 = par1EntityGolem.isSneaking() ? 32F : 64F;

            if (f2 < f3 && par1EntityGolem.getOwnerName() != "")
            {
            	StringBuilder sb = new StringBuilder(" Health: ").append(par1EntityGolem.getHealth());
                String s = sb.toString();
                renderLivingLabel(par1EntityGolem, s, par2, par4 + 1.0D, par6, 64);

                /*
                if (!par1EntityGolem.isSneaking())
                {
                    if (par1EntityGolem.isPlayerSleeping())
                    {
                        renderLivingLabel(par1EntityGolem, s, par2, par4 - 1.5D, par6, 64);
                    }
                    else
                    {
                        renderLivingLabel(par1EntityGolem, s, par2, par4, par6, 64);
                    }
                }
                else
                {
                    FontRenderer fontrenderer = getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)par2 + 0.0F, (float)par4 + 2.3F, (float)par6);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    Tessellator tessellator = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    int i = fontrenderer.getStringWidth(s) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    tessellator.addVertex(-i - 1, -1D, 0.0D);
                    tessellator.addVertex(-i - 1, 8D, 0.0D);
                    tessellator.addVertex(i + 1, 8D, 0.0D);
                    tessellator.addVertex(i + 1, -1D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 0x20ffffff);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
                */
            }
        }
    }

    protected void func_48419_a(EntityRSBGolem par1EntityGolem, float par2)
    {
        super.renderEquippedItems(par1EntityGolem, par2);
        if (par1EntityGolem.func_48117_D_() == 0)
        {
            return;
        }
        else
        {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glPushMatrix();
            GL11.glRotatef(5F + (180F * modelG.field_48233_c.rotateAngleX) / (float)Math.PI, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(-0.6875F, 1.25F, -0.9375F);
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            float f = 0.8F;
            GL11.glScalef(f, -f, f);
            int i = par1EntityGolem.getBrightnessForRender(par2);
            int j = i % 0x10000;
            int k = i / 0x10000;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            loadTexture("/terrain.png");
            renderBlocks.renderBlockAsItem(Block.plantRed, 0, 1.0F);
            GL11.glPopMatrix();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            return;
        }
    }

    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        func_48419_a((EntityRSBGolem)par1EntityLiving, par2);
    }

    protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        func_48420_a((EntityRSBGolem)par1EntityLiving, par2, par3, par4);
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        renderGolem((EntityRSBGolem)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        renderGolem((EntityRSBGolem)par1Entity, par2, par4, par6, par8, par9);
    }

    protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        renderName((EntityRSBGolem)par1EntityLiving, par2, par4, par6);
    }

}
