package unisannino.denenderman;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;

public class EntityAppleBomb extends EntityThrowable
{
    public EntityAppleBomb(World world)
    {
        super(world);
    }

    public EntityAppleBomb(World world, EntityLiving entityliving)
    {
        super(world, entityliving);
    }

    public EntityAppleBomb(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

    public void onUpdate()
    {
        float f1 = 0.25F;

        if (isInWater())
        {
            for (int i = 0; i < 4; i++)
            {
                worldObj.spawnParticle("bubble", posX - motionX * (double)f1, posY - motionY * (double)f1, posZ - motionZ * (double)f1, motionX, motionY, motionZ);
            }
            setDead();
        	worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
        }
    	super.onUpdate();
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition)
    {
        if (!worldObj.isRemote && !isInWater())
        {
            if (movingobjectposition.entityHit != null)
            {
            	if(movingobjectposition.entityHit == thrower)
            	{
            		return;
            	}
                if (!movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), 0));
            }
        	createExplosionMobDamageA();
        	worldObj.createExplosion(this, posX, posY, posZ, 0.0F, true);
        }

        if (!worldObj.isRemote)
        {
            setDead();
        }
    }

    private void createExplosionMobDamageA()
    {
    	float explosionSize = 2.0F * 2.0F;
        int k = MathHelper.floor_double(posX - (double)explosionSize - 1.0D);
        int i1 = MathHelper.floor_double(posX + (double)explosionSize + 1.0D);
        int k1 = MathHelper.floor_double(posY - (double)explosionSize - 1.0D);
        int l1 = MathHelper.floor_double(posY + (double)explosionSize + 1.0D);
        int i2 = MathHelper.floor_double(posZ - (double)explosionSize - 1.0D);
        int j2 = MathHelper.floor_double(posZ + (double)explosionSize + 1.0D);

        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(k, k1, i2, i1, l1, j2));
        Vec3 vec3d = this.worldObj.func_82732_R().getVecFromPool(posX, posY, posZ);

        for (int k2 = 0; k2 < list.size(); k2++)
        {
            Entity entity = (Entity)list.get(k2);
            double d4 = entity.getDistance(posX, posY, posZ) / (double)explosionSize;

            if (d4 <= 1.0D)
            {
                double d6 = entity.posX - posX;
                double d8 = entity.posY - posY;
                double d10 = entity.posZ - posZ;
                double d11 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
                d6 /= d11;
                d8 /= d11;
                d10 /= d11;
                double d12 = worldObj.getBlockDensity(vec3d, entity.boundingBox);
                double d13 = (1.0D - d4) * d12;
                entity.attackEntityFrom(DamageSource.explosion, (int)(((d13 * d13 + d13) / 2D) * 8D * (double)explosionSize + 1.0D));
                double d14 = d13;
                entity.motionX += d6 * d14 * 2.0F;
                entity.motionY += d8 * d14 * 2.0F;
                entity.motionZ += d10 * d14 * 2.0F;
            }
        }
        createExplosionMobDamageB(explosionSize);
    }

    private void createExplosionMobDamageB(float esize)
    {
        //worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 0.0D, 0.0D, 0.0D);

        double d = (float)Math.floor(posX) + worldObj.rand.nextFloat();
        double d1 = (float)Math.floor(posY) + worldObj.rand.nextFloat();
        double d2 = (float)Math.floor(posZ) + worldObj.rand.nextFloat();
        double d3 = d - posX;
        double d4 = d1 - posY;
        double d5 = d2 - posZ;
        double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
        d3 /= d6;
        d4 /= d6;
        d5 /= d6;
        double d7 = 0.5D / (d6 / (double)esize + 0.1D);
        d7 *= worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F;
        d3 *= d7;
        d4 *= d7;
        d5 *= d7;
        worldObj.spawnParticle("explode", (d + posX * 1.0D) / 2D, (d1 + posY * 1.0D) / 2D, (d2 + posZ * 1.0D) / 2D, d3, d4, d5);
        worldObj.spawnParticle("smoke", d, d1, d2, d3, d4, d5);
    }
}
