package unisannino.denenderman;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderThrowingItemColor extends RenderSnowball
{
	private ItemStack colorItem;

    public RenderThrowingItemColor(int i, ItemStack itemstack)
    {
        super(i);
        colorItem = itemstack;
    }

    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        /*
        float red = 0.87F;
        float green = 0.81F;
        float blue = 0.16F;
        float bright = entitydenenderpearl.getEntityBrightness(f1);
        GL11.glColor4f(red * bright, green * bright, blue * bright, 1.0F);
        */
        int k1 = Item.itemsList[colorItem.itemID].getColorFromItemStack(this.colorItem, 0);
        float f3 = 1.0F;
        float f9 = (k1 >> 16 & 0xff) / 255F;
        float f12 = (k1 >> 8 & 0xff) / 255F;
        float f14 = (k1 & 0xff) / 255F;
        GL11.glColor4f(f9 * f3, f12 * f3, f14 * f3, 1.0F);
        super.doRender(entity, d, d1, d2, f, f1);
    }
}
