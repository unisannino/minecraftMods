package unisannino.redstoneblock;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIOwnerHurtByTargetGolem extends EntityAITarget
{
    EntityRSBGolem golem;
    EntityLiving targetEntity;

    public EntityAIOwnerHurtByTargetGolem(EntityRSBGolem par1EntityTameable)
    {
        super(par1EntityTameable, 32F, false);
        golem = par1EntityTameable;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if (!golem.func_48139_F_())
        {
            return false;
        }

        EntityLiving entityliving = golem.getGolemOwner();

        if (entityliving == null)
        {
            return false;
        }
        else
        {
            targetEntity = entityliving.getAITarget();
            if(targetEntity instanceof EntityVillager)
            {
            	return false;
            }
            return this.isSuitableTarget(targetEntity, false);
        }
    }

    @Override
    public void startExecuting()
    {
    	taskOwner.setAttackTarget(targetEntity);
        super.startExecuting();
    }
}
