package unisannino.denenderman;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityWolf;
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
		EntityPlayer player = null;
		String playername = "";

		if(this.getThrower() != null)
		{
			player = (EntityPlayer) this.getThrower();
			playername = player.username;
		}

        if (var1.entityHit != null)
        {
            if (!var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0));
        }

        float f0 = 0.25F;
        worldObj.spawnParticle("reddust", ((posX - motionX * f0) + rand.nextDouble() * 0.6D) - 0.3D, posY - motionY * f0 - 0.5D, ((posZ - motionZ * f0) + rand.nextDouble() * 0.6D) - 0.3D, 2.0D, 200.0D, 0.0D);
        if (!worldObj.isRemote)
        {

            EntityFarmers farmer = new EntityDenEnderman(this.worldObj); //this.getFarmer(this.worldObj);
            farmer.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            farmer.setDEHomePos(this.posX, this.posY, this.posZ);
            farmer.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            farmer.setOwner(playername);
            farmer.setTamed(true);
            this.worldObj.spawnEntityInWorld(farmer);
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

	protected EntityFarmers getFarmer(World world)
	{
		return new EntityDenEnderman(world);
	}
}
