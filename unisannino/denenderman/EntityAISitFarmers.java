package unisannino.denenderman;

import net.minecraft.src.*;

public class EntityAISitFarmers extends EntityAISit
{
    private EntityTameable theEntity;

    /** If the EntityTameable is sitting. */
    private boolean isSitting = false;

    public EntityAISitFarmers(EntityTameable par1EntityTameable)
    {
    	super(par1EntityTameable);
        this.theEntity = par1EntityTameable;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theEntity.isTamed())
        {
            return false;
        }
        else if (this.theEntity.isInWater())
        {
            return false;
        }
        else if (!this.theEntity.onGround)
        {
            return false;
        }
        else
        {
            return this.isSitting;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.theEntity.getNavigator().clearPathEntity();
        this.theEntity.setSitting(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theEntity.setSitting(false);
    }

    /**
     * Sets the sitting flag.
     */
    public void setSitting(boolean par1)
    {
        this.isSitting = par1;
    }
}
