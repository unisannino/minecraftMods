package unisannino.denenderman;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIMoveUniHervestableAndEatable extends EntityAIBase
{

    private EntityUniuni theFarmers;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private float field_48299_e;
    private World theWorld;
    private boolean putSeed;

    public EntityAIMoveUniHervestableAndEatable(EntityUniuni par1EntityFarmers, float par2)
    {
        this.theFarmers = par1EntityFarmers;
        this.field_48299_e = par2;
        this.theWorld = par1EntityFarmers.worldObj;
        this.putSeed = false;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        Vec3 var1 = this.findPossibleHervestable();

        if (var1 == null)
        {
            var1 = this.findPossibleEatable();

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

    private Vec3 findPossibleHervestable()
    {
        Random var1 = this.theFarmers.getRNG();

        for (int var2 = 0; var2 < 10; ++var2)
        {
            int var3 = MathHelper.floor_double(this.theFarmers.posX + var1.nextInt(20) - 10.0D);
            int var4 = MathHelper.floor_double(this.theFarmers.boundingBox.minY + var1.nextInt(6) - 3.0D);
            int var5 = MathHelper.floor_double(this.theFarmers.posZ + var1.nextInt(20) - 10.0D);
            int var6 = this.theWorld.getBlockId(var3, var4, var5);
            int var7 = this.theWorld.getBlockMetadata(var3, var4, var5);

            if (this.theWorld.getBlockId(var3, var4, var5) == Block.reed.blockID)
            {
                int var8 = this.theWorld.getBlockId(var3, var4 + 1, var5);
                int var9 = this.theWorld.getBlockId(var3, var4 + 2, var5);

                if (var8 == Block.reed.blockID && var9 == Block.reed.blockID)
                {
                	return this.theWorld.getWorldVec3Pool().getVecFromPool((double)var3, (double)var4, (double)var5);
                }
            }else
            if (var6 == Block.netherStalk.blockID && var7 == 3)
            {
            	return this.theWorld.getWorldVec3Pool().getVecFromPool((double)var3, (double)var4, (double)var5);
            }
        }
        return null;
    }

    private Vec3 findPossibleEatable()
    {
        Random var1 = this.theFarmers.getRNG();

        for (int var2 = 0; var2 < 10; ++var2)
        {
            int var3 = MathHelper.floor_double(this.theFarmers.posX + var1.nextInt(20) - 10.0D);
            int var4 = MathHelper.floor_double(this.theFarmers.boundingBox.minY + var1.nextInt(6) - 3.0D);
            int var5 = MathHelper.floor_double(this.theFarmers.posZ + var1.nextInt(20) - 10.0D);
            int var6 = this.theWorld.getBlockId(var3, var4, var5);
            int var7 = this.theWorld.getBlockMetadata(var3, var4, var5);

            if (this.theFarmers.canEatBlocks(this.theWorld, var3, var4, var5) && this.theFarmers.getRNG().nextInt(20) == 0 || this.theFarmers.getHealth() < this.theFarmers.getMaxHealth())
            {
            	return this.theWorld.getWorldVec3Pool().getVecFromPool((double)var3, (double)var4, (double)var5);
            }
        }

		return null;
	}
}
