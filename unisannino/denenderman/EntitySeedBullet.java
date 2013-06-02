package unisannino.denenderman;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySeedBullet extends EntityThrowable
{

	public static int type2Damage[] = {1, 2, 3, 2};

    public EntitySeedBullet(World par1World)
    {
        super(par1World);
    }

    public EntitySeedBullet(World par1World, EntityLiving par2EntityLiving)
    {
        super(par1World, par2EntityLiving);
    }

    public EntitySeedBullet(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    @Override
    public void entityInit()
    {
    	this.dataWatcher.addObject(18, Integer.valueOf(0));
    	this.dataWatcher.addObject(19, Integer.valueOf(0));
    	this.dataWatcher.addObject(20, Integer.valueOf(0));
    }

    /**
     * Called when the throwable hits a block or entity.
     */
    @Override
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (par1MovingObjectPosition.entityHit != null)
        {
            if (par1MovingObjectPosition.entityHit instanceof EntityBlaze)
            {
                this.setBulletDamage(0);
            }
            if(par1MovingObjectPosition.entityHit instanceof EntityLiving)
            {
            	EntityLiving targetmob = (EntityLiving)par1MovingObjectPosition.entityHit;
            	targetmob.heal(0);
            }
            if (!par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.getBulletDamage()));
        }

        if (this.getBulletKB() > 0)
        {
            float f7 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

            if (f7 > 0.0F)
            {
                par1MovingObjectPosition.entityHit.addVelocity((motionX * this.getBulletKB() * 0.60000002384185791D) / f7, 0.10000000000000001D, (motionZ * this.getBulletKB() * 0.60000002384185791D) / f7);
            }
        }
        
        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }

    }

    public void setBulletDamage(int i)
    {
    	this.dataWatcher.updateObject(18, Integer.valueOf(i));
    }

    public int getBulletDamage()
    {
    	return this.dataWatcher.getWatchableObjectInt(18);
    }

    public void setBulletKB(int i)
    {
    	this.dataWatcher.updateObject(19, Integer.valueOf(i));
    }

    public int getBulletKB()
    {
    	return this.dataWatcher.getWatchableObjectInt(19);
    }

    public void setBulletType(int i)
    {
    	this.dataWatcher.updateObject(20, Integer.valueOf(i));
    }

    public int getBulletType()
    {
    	return this.dataWatcher.getWatchableObjectInt(20);
    }

    @Deprecated
	public void handlePacketdata(int seeddamage, int seedtype)
	{

	}

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("damage", this.getBulletDamage());
        par1NBTTagCompound.setInteger("knockback", this.getBulletKB());
        par1NBTTagCompound.setInteger("type", this.getBulletType());
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readEntityFromNBT(par1NBTTagCompound);
        if (par1NBTTagCompound.hasKey("damage"))
        {
            this.setBulletDamage(par1NBTTagCompound.getInteger("damage"));
        }
        this.setBulletKB(par1NBTTagCompound.getInteger("knockback"));
        this.setBulletType(par1NBTTagCompound.getInteger("type"));
    }
}
