package unisannino.denenderman;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

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

		EntityPlayer player = (EntityPlayer) this.getThrower();

        if (var1.entityHit != null)
        {
            if (!var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0));
        }

        if (!worldObj.isRemote)
        {
            float f1 = 0.25F;
            worldObj.spawnParticle("reddust", ((posX - motionX * f1) + rand.nextDouble() * 0.6D) - 0.3D, posY - motionY * f1 - 0.5D, ((posZ - motionZ * f1) + rand.nextDouble() * 0.6D) - 0.3D, 2.0D, 200.0D, 0.0D);
            EntityFarmers farmer = this.getFarmer();
            farmer.setDEHomePos(this.posX, this.posY, this.posZ);
            farmer.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            farmer.setOwner(player.username);
            farmer.setTamed(true);
            worldObj.spawnEntityInWorld(farmer);
            farmer.spawnExplosionParticle();
        }

        float f1 = 0.25F;

        if (isInWater())
        {
            for (int i = 0; i < 4; i++)
            {
                worldObj.spawnParticle("bubble", posX - motionX * f1, posY - motionY * f1, posZ - motionZ * f1, motionX, motionY, motionZ);
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
