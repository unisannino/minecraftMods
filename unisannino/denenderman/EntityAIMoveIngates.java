package unisannino.denenderman;

import net.minecraft.src.*;

public class EntityAIMoveIngates extends EntityAIBase
{
    private EntityFarmers entityObj;
    private DEHomeGateInfo gateInfo;
    private DEHomeCollection dehomeCollection;
    private int insidePosX = -1;
    private int insidePosZ = -1;

    public EntityAIMoveIngates(EntityFarmers par1EntityCreature)
    {
        this.entityObj = par1EntityCreature;
        this.dehomeCollection = par1EntityCreature.homecollection;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if ((!this.entityObj.worldObj.isDaytime() || this.entityObj.worldObj.isRaining()) && !this.entityObj.worldObj.provider.hasNoSky)
        {
        	/*
            if (this.entityObj.getRNG().nextInt(50) != 0)
            {
            	System.out.println("aaaa");
                return false;
            }
            else */
            if (this.insidePosX != -1 && this.entityObj.getDistanceSq((double)this.insidePosX, this.entityObj.posY, (double)this.insidePosZ) < 4.0D)
            {
                return false;
            }
            else
            {
                DEHome var1 = this.dehomeCollection.findNearestDEHome(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ), 14);

                if (var1 == null)
                {
                    return false;
                }
                else
                {
                    this.gateInfo = var1.findNearestGateUnrestricted(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ));
                    //System.out.println(this.gateInfo.posX +"plus"+ this.gateInfo.insideDirectionX +" "+ this.gateInfo.posZ +"plus"+ this.gateInfo.insideDirectionZ);
                    return this.gateInfo != null;
                }
            }
        }
        else
        {
            return this.entityObj.homePos != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.entityObj.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.insidePosX = -1;
        if((!this.entityObj.worldObj.isDaytime() || this.entityObj.worldObj.isRaining()) && !this.entityObj.worldObj.provider.hasNoSky)
        {
        	this.entityObj.setDEHomePos(this.entityObj.posX, this.entityObj.posY, this.entityObj.posZ);
        	
            if (this.entityObj.getDistanceSq((double)this.gateInfo.getInsidePosX(), (double)this.gateInfo.posY, (double)this.gateInfo.getInsidePosZ()) > 256.0D)
            {
                Vec3 var1 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, Vec3.getVec3Pool().getVecFromPool((double)this.gateInfo.getInsidePosX() + 0.5D, (double)this.gateInfo.getInsidePosY(), (double)this.gateInfo.getInsidePosZ() + 0.5D));

                if (var1 != null)
                {
                    this.entityObj.getNavigator().tryMoveToXYZ(var1.xCoord, var1.yCoord, var1.zCoord, 0.3F);
                	this.teleportTo(var1.xCoord, var1.yCoord, var1.zCoord);
                	this.entityObj.homePos.canTerepo = true;
                }
            }
            else
            {
               this.entityObj.getNavigator().tryMoveToXYZ((double)this.gateInfo.getInsidePosX() + 0.5D, (double)this.gateInfo.getInsidePosY(), (double)this.gateInfo.getInsidePosZ() + 0.5D, 0.3F);
               this.teleportTo((double)this.gateInfo.getInsidePosX() + 0.5D, (double)this.gateInfo.getInsidePosY(), (double)this.gateInfo.getInsidePosZ() + 0.5D);
               this.entityObj.homePos.canTerepo = true;
            }
        }else if(this.entityObj.homePos != null && this.entityObj.homePos.canTerepo)
        {
        	DEHomePosition homepos = this.entityObj.homePos;

            this.teleportTo(homepos.posX, homepos.posY, homepos.posZ);
            this.entityObj.homePos.canTerepo = false;
        }
    }

    protected boolean teleportTo(double par1, double par3, double par5)
    {
        double var7 = this.entityObj.posX;
        double var9 = this.entityObj.posY;
        double var11 = this.entityObj.posZ;
        this.entityObj.posX = par1;
        this.entityObj.posY = par3;
        this.entityObj.posZ = par5;
        boolean var13 = false;
        int var14 = MathHelper.floor_double(this.entityObj.posX);
        int var15 = MathHelper.floor_double(this.entityObj.posY);
        int var16 = MathHelper.floor_double(this.entityObj.posZ);
        int var18;

        if (this.entityObj.worldObj.blockExists(var14, var15, var16))
        {
            boolean var17 = false;

            while (!var17 && var15 > 0)
            {
                var18 = this.entityObj.worldObj.getBlockId(var14, var15 - 1, var16);

                if (var18 != 0 && Block.blocksList[var18].blockMaterial.blocksMovement())
                {
                    var17 = true;
                }
                else
                {
                    --this.entityObj.posY;
                    --var15;
                }
            }

            if (var17)
            {
                this.entityObj.setPosition(this.entityObj.posX, this.entityObj.posY, this.entityObj.posZ);

                if (this.entityObj.worldObj.getCollidingBoundingBoxes(this.entityObj, this.entityObj.boundingBox).isEmpty() && !this.entityObj.worldObj.isAnyLiquid(this.entityObj.boundingBox))
                {
                    var13 = true;
                }
            }
        }

        if (!var13)
        {
            this.entityObj.setPosition(var7, var9, var11);
            return false;
        }
        else
        {
        	if(!this.entityObj.worldObj.isRemote)
        	{
                short var30 = 128;

                for (var18 = 0; var18 < var30; ++var18)
                {
                    double var19 = (double)var18 / ((double)var30 - 1.0D);
                    float var21 = (this.entityObj.getRNG().nextFloat() - 0.5F) * 0.2F;
                    float var22 = (this.entityObj.getRNG().nextFloat() - 0.5F) * 0.2F;
                    float var23 = (this.entityObj.getRNG().nextFloat() - 0.5F) * 0.2F;
                    double var24 = var7 + (this.entityObj.posX - var7) * var19 + (this.entityObj.getRNG().nextDouble() - 0.5D) * (double)this.entityObj.width * 2.0D;
                    double var26 = var9 + (this.entityObj.posY - var9) * var19 + this.entityObj.getRNG().nextDouble() * (double)this.entityObj.height;
                    double var28 = var11 + (this.entityObj.posZ - var11) * var19 + (this.entityObj.getRNG().nextDouble() - 0.5D) * (double)this.entityObj.width * 2.0D;
                    this.entityObj.worldObj.spawnParticle("portal", var24, var26, var28, (double)var21, (double)var22, (double)var23);
                }
        	}

            this.entityObj.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
            this.entityObj.worldObj.playSoundAtEntity(this.entityObj, "mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
    	if(this.gateInfo != null)
    	{
            this.insidePosX = this.gateInfo.getInsidePosX();
            this.insidePosZ = this.gateInfo.getInsidePosZ();
            this.gateInfo = null;
    	}
    }
}
