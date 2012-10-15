package unisannino.denenderman;

import net.minecraft.src.*;

public class EntityAIRestrictOpenGate extends EntityAIBase
{
    private EntityCreature entityObj;
    private DEHomeGateInfo frontGate;
    private DEHomeCollection dehomeCollection;

    public EntityAIRestrictOpenGate(EntityFarmers par1EntityCreature)
    {
        this.entityObj = par1EntityCreature;
        this.dehomeCollection = par1EntityCreature.homecollection;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.entityObj.worldObj.isDaytime())
        {
            return false;
        }
        else
        {
            DEHome var1 = this.dehomeCollection.findNearestDEHome(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ), 16);

            if (var1 == null)
            {
                return false;
            }
            else
            {
                this.frontGate = var1.findNearestDoor(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ));
                return this.frontGate == null ? false : (double)this.frontGate.getInsideDistanceSquare(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ)) < 2.25D;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.entityObj.worldObj.isDaytime() ? false : !this.frontGate.isDetachedFromVillageFlag && this.frontGate.isInside(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posZ));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entityObj.getNavigator().setBreakDoors(false);
        this.entityObj.getNavigator().setEnterDoors(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.entityObj.getNavigator().setBreakDoors(true);
        this.entityObj.getNavigator().setEnterDoors(true);
        this.frontGate = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.frontGate.incrementDoorOpeningRestrictionCounter();
    }
}
