package unisannino.denenderman;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySeedBullet extends EntityThrowable
{
	public int knockback;
	public int damage;
	public byte type;

    public EntitySeedBullet(World par1World)
    {
        super(par1World);
        knockback = 0;
    }

    public EntitySeedBullet(World par1World, EntityLiving par2EntityLiving)
    {
        super(par1World, par2EntityLiving);
        knockback = 0;
    }

    public EntitySeedBullet(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
        knockback = 0;
    }

    @Override
	public void onUpdate()
    {
    	super.onUpdate();
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
                damage = 0;
            }
            if(par1MovingObjectPosition.entityHit instanceof EntityLiving)
            {
            	EntityLiving targetmob = (EntityLiving)par1MovingObjectPosition.entityHit;
            	targetmob.heal(0);
            }
            if (!par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage));
        }

        if (knockback > 0)
        {
            float f7 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

            if (f7 > 0.0F)
            {
                par1MovingObjectPosition.entityHit.addVelocity((motionX * knockback * 0.60000002384185791D) / f7, 0.10000000000000001D, (motionZ * knockback * 0.60000002384185791D) / f7);
            }
        }

        if (!worldObj.isRemote)
        {
            setDead();
        }
    }

	public void handlePacketdata(int seeddamage, byte seedtype)
	{
		this.damage = seeddamage;
		this.type = seedtype;
	}
}
