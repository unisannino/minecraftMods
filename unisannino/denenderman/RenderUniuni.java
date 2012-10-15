package unisannino.denenderman;

import net.minecraft.src.*;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUniuni extends RenderLiving
{
    public RenderUniuni()
    {
        super(new ModelUniuni(), 0.5F);
    }

    public void renderUniuni(EntityUniuni entityuniuni, double d, double d1, double d2,
            float f, float f1)
    {
        super.doRenderLiving(entityuniuni, d, d1, d2, f, f1);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1)
    {
        renderUniuni((EntityUniuni)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        renderUniuni((EntityUniuni)entity, d, d1, d2, f, f1);
    }
}
