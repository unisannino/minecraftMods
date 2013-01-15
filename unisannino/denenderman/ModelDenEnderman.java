package unisannino.denenderman;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDenEnderman extends ModelBase
{
    public boolean isPlanting;
    public ModelRenderer bipedHead;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer bipedEars;
    public ModelRenderer bipedCloak;
    public int field_1279_h;
    public int field_1278_i;
    public boolean isSneak;
    public boolean field_40333_u;

    public ModelDenEnderman()
    {
        field_1279_h = 0;
        field_1278_i = 0;
        isSneak = false;
        field_40333_u = false;
        isPlanting = false;
        textureWidth = 64;
        textureHeight = 64;
        float f = -14F;
        float f1 = 0.0F;
        bipedHead = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, 0);
        bipedHead.setRotationPoint(0.0F, 0.0F + f, 0.0F);
        bipedHeadwear = (new ModelRenderer(this, 0, 16)).setTextureSize(64, 64);
        bipedHeadwear.addBox(-4F, -8F, -4F, 8, 8, 8, f1 - 0.5F);
        bipedHeadwear.setRotationPoint(0.0F, 0.0F + f, 0.0F);
        bipedBody = (new ModelRenderer(this, 32, 16)).setTextureSize(64, 64);;
        bipedBody.addBox(-4F, 0.0F, -2F, 8, 12, 4, f1);
        bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
        bipedRightArm = (new ModelRenderer(this, 56, 0)).setTextureSize(64, 64);
        bipedRightArm.addBox(-1F, -2F, -1F, 2, 30, 2, f1);
        bipedRightArm.setRotationPoint(-3F, 2.0F + f, 0.0F);
        bipedLeftArm = (new ModelRenderer(this, 56, 0)).setTextureSize(64, 64);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(-1F, -2F, -1F, 2, 30, 2, f1);
        bipedLeftArm.setRotationPoint(5F, 2.0F + f, 0.0F);
        bipedRightLeg = (new ModelRenderer(this, 56, 0)).setTextureSize(64, 64);
        bipedRightLeg.addBox(-1F, 0.0F, -1F, 2, 30, 2, f1);
        bipedRightLeg.setRotationPoint(-2F, 12F + f, 0.0F);
        bipedLeftLeg = (new ModelRenderer(this, 56, 0)).setTextureSize(64, 64);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-1F, 0.0F, -1F, 2, 30, 2, f1);
        bipedLeftLeg.setRotationPoint(2.0F, 12F + f, 0.0F);
        //hat
        bipedHead.setTextureSize(64, 64).setTextureOffset(0, 43).addBox(-6F, -9F, -6F, 12, 1, 12, f1);
        bipedHead.setTextureSize(64, 64).setTextureOffset(0, 32).addBox(-3F, -13F, -3F, 6, 4, 6, f1);
    }

    @Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        bipedHead.render(f5);
        bipedBody.render(f5);
        bipedRightArm.render(f5);
        bipedLeftArm.render(f5);
        bipedRightLeg.render(f5);
        bipedLeftLeg.render(f5);
        bipedHeadwear.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        bipedHead.rotateAngleY = f3 / (180F / (float)Math.PI);
        bipedHead.rotateAngleX = f4 / (180F / (float)Math.PI);
        bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
        bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
        bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 2.0F * f1 * 0.5F;
        bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
        bipedRightArm.rotateAngleZ = 0.0F;
        bipedLeftArm.rotateAngleZ = 0.0F;
        bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        bipedRightLeg.rotateAngleY = 0.0F;
        bipedLeftLeg.rotateAngleY = 0.0F;

        if (isRiding)
        {
            bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
            bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
            bipedRightLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            bipedLeftLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
            bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
        }

        if (field_1279_h != 0)
        {
            bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * field_1279_h;
        }

        if (field_1278_i != 0)
        {
            bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * field_1278_i;
        }

        bipedRightArm.rotateAngleY = 0.0F;
        bipedLeftArm.rotateAngleY = 0.0F;

        if (onGround > -9990F)
        {
            float f6 = onGround;
            bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI * 2.0F) * 0.2F;
            bipedRightArm.rotationPointZ = MathHelper.sin(bipedBody.rotateAngleY) * 5F;
            bipedRightArm.rotationPointX = -MathHelper.cos(bipedBody.rotateAngleY) * 5F;
            bipedLeftArm.rotationPointZ = -MathHelper.sin(bipedBody.rotateAngleY) * 5F;
            bipedLeftArm.rotationPointX = MathHelper.cos(bipedBody.rotateAngleY) * 5F;
            bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
            bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
            bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;

            f6 = 1.0F - onGround;
            f6 *= f6;
            f6 *= f6;
            f6 = 1.0F - f6;
            float f8 = MathHelper.sin(f6 * (float)Math.PI);
            float f10 = MathHelper.sin(onGround * (float)Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;

            bipedRightArm.rotateAngleX -= f8 * 1.2D + f10;
            bipedRightArm.rotateAngleY += bipedBody.rotateAngleY * 2.0F;
            bipedRightArm.rotateAngleZ = MathHelper.sin(onGround * (float)Math.PI) * -0.4F;
        }

        bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;

        bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
        bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;

        //denender
        bipedHead.showModel = true;
        bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
        bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
        float f6 = -14F;
        bipedBody.rotateAngleX = 0.0F;
        bipedBody.rotationPointY = f6;
        bipedBody.rotationPointZ = -0F;
        bipedRightLeg.rotateAngleX -= 0.0F;
        bipedLeftLeg.rotateAngleX -= 0.0F;

        bipedRightArm.rotateAngleX *= 0.5D;
        bipedLeftArm.rotateAngleX *= 0.5D;

        bipedRightLeg.rotateAngleX *= 0.5D;
        bipedLeftLeg.rotateAngleX *= 0.5D;
        float f10 = 0.4F;

        if (bipedRightArm.rotateAngleX > f10)
        {
            bipedRightArm.rotateAngleX = f10;
        }

        if (bipedLeftArm.rotateAngleX > f10)
        {
            bipedLeftArm.rotateAngleX = f10;
        }

        if (bipedRightArm.rotateAngleX < -f10)
        {
            bipedRightArm.rotateAngleX = -f10;
        }

        if (bipedLeftArm.rotateAngleX < -f10)
        {
            bipedLeftArm.rotateAngleX = -f10;
        }

        if (bipedRightLeg.rotateAngleX > f10)
        {
            bipedRightLeg.rotateAngleX = f10;
        }

        if (bipedLeftLeg.rotateAngleX > f10)
        {
            bipedLeftLeg.rotateAngleX = f10;
        }

        if (bipedRightLeg.rotateAngleX < -f10)
        {
            bipedRightLeg.rotateAngleX = -f10;
        }

        if (bipedLeftLeg.rotateAngleX < -f10)
        {
            bipedLeftLeg.rotateAngleX = -f10;
        }

        bipedRightArm.rotationPointZ = 0.0F;
        bipedLeftArm.rotationPointZ = 0.0F;
        bipedRightLeg.rotationPointZ = 0.0F;
        bipedLeftLeg.rotationPointZ = 0.0F;
        bipedRightLeg.rotationPointY = 9F + f6;
        bipedLeftLeg.rotationPointY = 9F + f6;
        bipedHead.rotationPointZ = -0F;
        bipedHead.rotationPointY = f6 + 1.0F;
        bipedHeadwear.rotationPointX = bipedHead.rotationPointX;
        bipedHeadwear.rotationPointY = bipedHead.rotationPointY;
        bipedHeadwear.rotationPointZ = bipedHead.rotationPointZ;
        bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
        bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
        bipedHeadwear.rotateAngleZ = bipedHead.rotateAngleZ;

        if (this.isPlanting)
        {
            float f11 = 1.0F;
            bipedHead.rotationPointY -= f11 * 5F;
        }
    }
}
