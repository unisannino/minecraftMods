package unisannino.denenderman;

import net.minecraft.src.*;

public class EntityFarmerSpawner extends EntityThrowable
{
    public EntityFarmerSpawner(World world)
    {
        super(world);
    }

    public EntityFarmerSpawner(World world, EntityLiving entityliving)
    {
        super(world, entityliving);
    }

    public EntityFarmerSpawner(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

	@Override
	protected void onImpact(MovingObjectPosition var1)
	{
        if (var1.entityHit != null)
        {
            if (!var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), 0));
        }

        if (!worldObj.isRemote)
        {
            float f1 = 0.25F;
            worldObj.spawnParticle("reddust", ((posX - motionX * (double)f1) + rand.nextDouble() * 0.6D) - 0.3D, posY - motionY * (double)f1 - 0.5D, ((posZ - motionZ * (double)f1) + rand.nextDouble() * 0.6D) - 0.3D, 2.0D, 200.0D, 0.0D);
            EntityFarmers farmer = this.getFarmer();
            farmer.setDEHomePos(this.posX, this.posY, this.posZ);
            farmer.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            worldObj.spawnEntityInWorld(farmer);
            farmer.spawnExplosionParticle();
        }

        float f1 = 0.25F;

        if (isInWater())
        {
            for (int i = 0; i < 4; i++)
            {
                worldObj.spawnParticle("bubble", posX - motionX * (double)f1, posY - motionY * (double)f1, posZ - motionZ * (double)f1, motionX, motionY, motionZ);
            }
        }

        if (!worldObj.isRemote)
        {
            setDead();
        }
	}

	protected EntityFarmers getFarmer()
	{
		return null;
	}
}
