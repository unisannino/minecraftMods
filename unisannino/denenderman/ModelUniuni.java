package unisannino.denenderman;

import net.minecraft.src.*;

public class ModelUniuni extends ModelBase
{
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;

    public ModelUniuni()
    {
        textureWidth = 64;
        textureHeight = 32;
        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(-4F, 0F, 0F, 8, 8, 20);
        Shape1.setRotationPoint(0F, 16F, -9F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 56, 0);
        Shape2.addBox(0F, 0F, 0F, 3, 2, 1);
        Shape2.setRotationPoint(-1.5F, 20F, -10F);
        Shape2.setTextureSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
    }

    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        super.render(par1Entity, par2, par3, par4, par5, par6, par7);
        setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
        Shape1.render(par7);
        Shape2.render(par7);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity par7Entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, par7Entity);
    }
}
