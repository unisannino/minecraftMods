package unisannino.denenderman;

import net.minecraft.src.*;

public class EntityAIOpenGate extends EntityAIGateInteract
{
    boolean field_75361_i;
    int field_75360_j;

    public EntityAIOpenGate(EntityLiving par1EntityLiving, boolean par2)
    {
        super(par1EntityLiving);
        this.theEntity = par1EntityLiving;
        this.field_75361_i = par2;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.field_75361_i && this.field_75360_j > 0 && super.continueExecuting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	//System.out.println("start");
        this.field_75360_j = 20;
        this.targetDoor.onBlockActivated(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, new EntityDummyPlayer(this.theEntity), 0, 0F, 0F, 0F);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        if (this.field_75361_i)
        {
            //this.targetDoor.onNeighborBlockChange(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, 0);
        	this.targetDoor.onBlockActivated(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, new EntityDummyPlayer(this.theEntity), 0, 0F, 0F, 0F);
        }
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        --this.field_75360_j;
        super.updateTask();
    }
}
