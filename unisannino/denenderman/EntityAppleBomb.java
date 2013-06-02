package unisannino.denenderman;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

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

    @Override
	public void onUpdate()
    {
        if (isInWater())
        {
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
                if (!movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0));
            }
            this.newExplosionAb(this, this.posX, this.posY, this.posZ,  2.0F * 2.0F, false, true);
        	worldObj.createExplosion(this, posX, posY, posZ, 0.0F, true);
        }

        if (!worldObj.isRemote)
        {
            setDead();
        }
    }

    public Explosion newExplosionAb(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10)
    {
        Explosion explosion = new ExplosionAppleBomb(this.worldObj, par1Entity, par2, par4, par6, par8);
        explosion.isFlaming = par9;
        explosion.isSmoking = par10;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

}
