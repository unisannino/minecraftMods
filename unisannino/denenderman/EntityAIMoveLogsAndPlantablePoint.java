package unisannino.denenderman;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIMoveLogsAndPlantablePoint extends EntityAIBase
{

    private EntityTreeper theFarmers;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private float field_48299_e;
    private World theWorld;
    private boolean putSeed;

    public EntityAIMoveLogsAndPlantablePoint(EntityTreeper par1EntityFarmers, float par2)
    {
        this.theFarmers = par1EntityFarmers;
        this.field_48299_e = par2;
        this.theWorld = par1EntityFarmers.worldObj;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theWorld.isDaytime())
        {
            return false;
        }
        else
        {
            Vec3 var1 = this.findPossibleLogs();

            if (var1 == null)
            {
                var1 = this.findPossiblePlantable();

                if(var1 == null)
                {
                	return false;
                }else
                {
                    this.shelterX = var1.xCoord;
                    this.shelterY = var1.yCoord;
                    this.shelterZ = var1.zCoord;


                    return true;
                }
            }
            else
            {
                this.shelterX = var1.xCoord;
                this.shelterY = var1.yCoord;
                this.shelterZ = var1.zCoord;


                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	return this.theFarmers.getNavigator().noPath();
    }

    public void resetTasks()
    {
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.theFarmers.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.field_48299_e);
    }

    private Vec3 findPossibleLogs()
    {
        Random var1 = this.theFarmers.getRNG();

        for (int var2 = 0; var2 < 10; ++var2)
        {
            int var3 = MathHelper.floor_double(this.theFarmers.posX + var1.nextInt(20) - 10.0D);
            int var4 = MathHelper.floor_double(this.theFarmers.boundingBox.minY + var1.nextInt(6) - 3.0D);
            int var5 = MathHelper.floor_double(this.theFarmers.posZ + var1.nextInt(20) - 10.0D);


            if (this.theFarmers.checkIsLogs(this.theWorld, var3, var4, var5) && this.theFarmers.underWoodBlock(this.theWorld, var3, var4, var5) && this.theFarmers.checkTopOfLogs(this.theWorld, var3, var4, var5))
            {
                return this.theWorld.getWorldVec3Pool().getVecFromPool((double)var3, (double)var4, (double)var5);
            }
        }
        return null;
    }

    private Vec3 findPossiblePlantable()
    {
        Random var1 = this.theFarmers.getRNG();

        for (int var2 = 0; var2 < 10; ++var2)
        {
            int var3 = MathHelper.floor_double(this.theFarmers.posX + var1.nextInt(20) - 10.0D);
            int var4 = MathHelper.floor_double(this.theFarmers.boundingBox.minY + var1.nextInt(6) - 3.0D);
            int var5 = MathHelper.floor_double(this.theFarmers.posZ + var1.nextInt(20) - 10.0D);

			ItemStack itemstack = null;
			Object sapling = null;
			int meta = 0;
	        for (int length = 0; length < this.theFarmers.inventory.mainInventory.length; length++)
	        {
	            if (this.theFarmers.inventory.mainInventory[length] != null)
	            {
	            	sapling = this.theFarmers.inventory.getInventoryObject(length);
					if(sapling instanceof BlockSapling)
					{
						meta = this.theFarmers.inventory.getInventoryMetadata(length);
						itemstack = new ItemStack((Block)sapling, 1, meta);
						break;
					}
	            }
	        }

	        if(itemstack != null)
	        {
	        	if(this.theFarmers.canPlanting(this.theWorld, var3, var4, var5))
	        	{
	        		return this.theWorld.getWorldVec3Pool().getVecFromPool((double)var3, (double)var4, (double)var5);
	        	}
	        }
        }
        return null;
    }
}
