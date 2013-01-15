package unisannino.redstoneblock;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIStayGolem extends EntityAIBase
{
    private EntityRSBGolem golem;
    private boolean field_48408_b;

    public EntityAIStayGolem(EntityRSBGolem par1EntityGolem)
    {
        field_48408_b = false;
        golem = par1EntityGolem;
        setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!golem.func_48139_F_())
        {
            return false;
        }

        if (golem.isInWater())
        {
            return false;
        }

        if (!golem.onGround)
        {
            return false;
        }

        EntityLiving entityliving = golem.getGolemOwner();

        if (entityliving == null)
        {
            return true;
        }

        if (golem.getDistanceSqToEntity(entityliving) < 144D && entityliving.getAITarget() != null)
        {
            return false;
        }
        else
        {
            return field_48408_b;
        }
    }

    public void startExecuting()
    {
        golem.getNavigator().clearPathEntity();
        golem.func_48140_f(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        golem.func_48140_f(false);
    }

    public void func_48407_a(boolean par1)
    {
        field_48408_b = par1;
    }
}
