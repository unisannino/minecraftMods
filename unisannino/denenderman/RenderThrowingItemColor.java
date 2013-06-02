package unisannino.denenderman;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderThrowingItemColor extends RenderSnowball
{
	private Item colorItem;
	private int itemdmg;

    public RenderThrowingItemColor(Item item, int dmg)
    {
        super(item, dmg);
        colorItem = item;
        this.itemdmg = dmg;
    }

    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9)
    {
    	Icon icon = this.colorItem.getIconFromDamage(itemdmg);

    	if(icon != null)
    	{
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par2, (float)par4, (float)par6);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.loadTexture("/gui/items.png");
            Tessellator tessellator = Tessellator.instance;

            int k1 = this.colorItem.getColorFromItemStack(new ItemStack(this.colorItem, 0), this.itemdmg);
            float f3 = 1.0F;
            float f9 = (k1 >> 16 & 0xff) / 255F;
            float f12 = (k1 >> 8 & 0xff) / 255F;
            float f14 = (k1 & 0xff) / 255F;
            GL11.glColor4f(f9 * f3, f12 * f3, f14 * f3, 1.0F);
            
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
    	}

    	super.doRender(entity, par2, par4, par6, par8, par9);
    }
    
    
}
