package unisannino.denenderman;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.pathfinding.PathEntity;

public class EntityAINearestTargetItems extends EntityAIBase
{
    EntityItem targetEntity;
    EntityFarmers theOwner;
    float moveSpeed;
    protected  boolean lastAvoidWater;
    private EntityAINearestTargetItemsSorter sorter;

    public EntityAINearestTargetItems(EntityFarmers par1EntityFarmers, float par2)
    {
    	this.theOwner = par1EntityFarmers;
    	this.moveSpeed = par2;
        this.sorter = new EntityAINearestTargetItemsSorter(this, par1EntityFarmers);
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.theOwner.inventory.getFirstEmptyStack() > -1)
        {
            List var1 = this.theOwner.worldObj.getEntitiesWithinAABB(EntityItem.class, this.theOwner.boundingBox.expand(8F, 2D, 8F));
            Collections.sort(var1, this.sorter);
            Iterator var2 = var1.iterator();

            if (var2.hasNext())
            {
                EntityItem var4 = (EntityItem)var2.next();

                /*
                 * this.theOwner.canEntityItemBeSeen(var4)で呼び出されるrayTraceBlocksメソッドの扱い方が何とも言えない。
                 * 通過可能なブロックでもなんでもブロックであればその時点でアウトと判定されてしまうっぽい
                 * 今後に期待。
                 */
                if (!var4.isDead && var4.onGround && var4.delayBeforeCanPickup <= 0/* && this.theOwner.canEntityItemBeSeen(var4)*/)
                {
                    if (this.theOwner.checkItemID(var4.func_92014_d().itemID) || this.theOwner instanceof EntityUniuni)
                    {
                    	this.targetEntity = var4;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	this.lastAvoidWater = this.theOwner.getNavigator().getAvoidsWater();
    	this.theOwner.getNavigator().setAvoidsWater(false);
    }

	public boolean continueExecuting()
	{
		return !this.targetEntity.isDead && this.theOwner.inventory.getFirstEmptyStack() > -1;
	}

	public void resetTask()
	{
		this.targetEntity = null;
		this.theOwner.getNavigator().clearPathEntity();
		this.theOwner.getNavigator().setAvoidsWater(lastAvoidWater);
	}

	public void updateTask()
	{
        this.theOwner.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30F, this.theOwner.getVerticalFaceSpeed());
        if(!this.theOwner.worldObj.isRemote)
        {
        	this.theOwner.findItems = true;
        }

        if (this.theOwner.getNavigator().noPath())
        {
            if (this.targetEntity.isInWater())
            {
    			this.theOwner.getNavigator().setAvoidsWater(false);
    		}
            PathEntity lpath = this.theOwner.getNavigator().getPathToXYZ(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ);
            this.theOwner.getNavigator().setPath(lpath, moveSpeed);
        }
	}
}
