package unisannino.denenderman;

import java.util.Random;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderDenEnderman extends RenderLiving
{
    private EntityDenEnderman denender;
    public boolean getHats;
    private ModelDenEnderman denendermanModel;
    private Random rand;
    private ModelBiped modelbiped;
    private int Headtype;

    public RenderDenEnderman()
    {
        super(new ModelDenEnderman(), 0.5F);
        rand = new Random();
        denendermanModel = (ModelDenEnderman)super.mainModel;
        this.setRenderPassModel(this.denendermanModel);
    }

    public void renderEnderman(EntityDenEnderman entitydenenderman, double d, double d1, double d2,
            float f, float f1)
    {
        denendermanModel.isPlanting = entitydenenderman.getFaceAngry();

        if (entitydenenderman.getFaceAngry())
        {
            double d3 = 0.02D;
            d += rand.nextGaussian() * d3;
            d2 += rand.nextGaussian() * d3;
        }

        super.doRenderLiving(entitydenenderman, d, d1, d2, f, f1);
    }

    protected void renderCarrying(EntityDenEnderman entitydenenderman, float f)
    {
        super.renderEquippedItems(entitydenenderman, f);
    }


    @Override
	protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return super.shouldRenderPass(entityliving, i, f);
        //return renderHats((EntityDenEnderman)entityliving, i, f);
    }

    @Override
	protected void renderEquippedItems(EntityLiving entityliving, float f)
    {
        renderCarrying((EntityDenEnderman)entityliving, f);
    }

    @Override
	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1)
    {
        renderEnderman((EntityDenEnderman)entityliving, d, d1, d2, f, f1);
    }

    @Override
	public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        renderEnderman((EntityDenEnderman)entity, d, d1, d2, f, f1);
    }
}
