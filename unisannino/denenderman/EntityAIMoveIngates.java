package unisannino.denenderman;

import net.minecraft.src.*;

public class EntityAIMoveIngates extends EntityAIBase
{
    private EntityFarmers theFarmers;
    private DEHomeGateInfo gateInfo;
    private DEHomeCollection dehomeCollection;
    private int insidePosX = -1;
    private int insidePosZ = -1;

    public EntityAIMoveIngates(EntityFarmers par1EntityCreature)
    {
        this.theFarmers = par1EntityCreature;
        this.dehomeCollection = par1EntityCreature.homecollection;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	if(!this.theFarmers.isSitting())
    	{
            if (this.isRestTime())
            {
            	/*
                if (this.entityObj.getRNG().nextInt(50) != 0)
                {
                	System.out.println("aaaa");
                    return false;
                }
                else */
                if (this.insidePosX != -1 && this.theFarmers.getDistanceSq((double)this.insidePosX, this.theFarmers.posY, (double)this.insidePosZ) < 4.0D)
                {
                    return false;
                }
                else
                {
                    DEHome var1 = this.dehomeCollection.findNearestDEHome(MathHelper.floor_double(this.theFarmers.posX), MathHelper.floor_double(this.theFarmers.posY), MathHelper.floor_double(this.theFarmers.posZ), 14);

                    if (var1 == null)
                    {
                        return false;
                    }
                    else
                    {
                        this.gateInfo = var1.findNearestGateUnrestricted(MathHelper.floor_double(this.theFarmers.posX), MathHelper.floor_double(this.theFarmers.posY), MathHelper.floor_double(this.theFarmers.posZ));
                        //System.out.println(this.gateInfo.posX +"plus"+ this.gateInfo.insideDirectionX +" "+ this.gateInfo.posZ +"plus"+ this.gateInfo.insideDirectionZ);
                        return this.gateInfo != null;
                    }
                }
            }
            else
            {
                return this.hasHomesInfo()&& !this.isRestTime();
            }
    	}

    	return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void updateTask()
    {
        this.insidePosX = -1;
        if(this.isRestTime() && this.isPrevTerepo())
        {
        	this.theFarmers.setDEHomePos(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);

            if (this.theFarmers.getDistanceSq((double)this.gateInfo.getInsidePosX(), (double)this.gateInfo.posY, (double)this.gateInfo.getInsidePosZ()) > 256.0D)
            {
                Vec3 var1 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theFarmers, 14, 3, this.theFarmers.worldObj.getWorldVec3Pool().getVecFromPool((double)this.gateInfo.getInsidePosX() + 0.5D, (double)this.gateInfo.getInsidePosY(), (double)this.gateInfo.getInsidePosZ() + 0.5D));

                if (var1 != null)
                {
                    //this.entityObj.getNavigator().tryMoveToXYZ(var1.xCoord, var1.yCoord, var1.zCoord, 0.3F);
                    if(this.teleportTo(var1.xCoord, var1.yCoord, var1.zCoord))
                    {
                    	this.theFarmers.homePos.isinHome = true;
                    }
                }
            }
            else
            {
               //this.entityObj.getNavigator().tryMoveToXYZ((double)this.gateInfo.getInsidePosX() + 0.5D, (double)this.gateInfo.getInsidePosY(), (double)this.gateInfo.getInsidePosZ() + 0.5D, 0.3F);
               if(this.teleportTo((double)this.gateInfo.getInsidePosX() + 0.5D, (double)this.gateInfo.getInsidePosY(), (double)this.gateInfo.getInsidePosZ() + 0.5D))
               {
            	   this.theFarmers.homePos.isinHome = true;
               }
            }
        }else if(this.hasHomesInfo() && !this.isRestTime())
        {
        	DEHomePosition homepos = this.theFarmers.homePos;

            if(this.teleportTo(homepos.posX, homepos.posY, homepos.posZ))
            {
            	this.theFarmers.homePos = null;
            }
        }
    }

    private boolean isRestTime()
    {
    	return !(this.theFarmers.worldObj.isDaytime() || this.theFarmers.worldObj.isRaining()) && !this.theFarmers.worldObj.provider.hasNoSky;
    }

    private boolean hasHomesInfo()
    {
    	return this.theFarmers.homePos != null && this.theFarmers.homePos.isinHome;
    }

    private boolean isPrevTerepo()
    {
    	return this.theFarmers.homePos == null || !this.theFarmers.homePos.isinHome;
    }

    protected boolean teleportTo(double par1, double par3, double par5)
    {
        double var7 = this.theFarmers.posX;
        double var9 = this.theFarmers.posY;
        double var11 = this.theFarmers.posZ;
        this.theFarmers.posX = par1;
        this.theFarmers.posY = par3;
        this.theFarmers.posZ = par5;
        boolean var13 = false;
        int var14 = MathHelper.floor_double(this.theFarmers.posX);
        int var15 = MathHelper.floor_double(this.theFarmers.posY);
        int var16 = MathHelper.floor_double(this.theFarmers.posZ);
        int var18;

        if (this.theFarmers.worldObj.blockExists(var14, var15, var16))
        {
            boolean var17 = false;

            while (!var17 && var15 > 0)
            {
                var18 = this.theFarmers.worldObj.getBlockId(var14, var15 - 1, var16);

                if (var18 != 0 && Block.blocksList[var18].blockMaterial.blocksMovement())
                {
                    var17 = true;
                }
                else
                {
                    --this.theFarmers.posY;
                    --var15;
                }
            }

            if (var17)
            {
                this.theFarmers.setPosition(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);

                if (this.theFarmers.worldObj.getCollidingBoundingBoxes(this.theFarmers, this.theFarmers.boundingBox).isEmpty() && !this.theFarmers.worldObj.isAnyLiquid(this.theFarmers.boundingBox))
                {
                    var13 = true;
                }
            }
        }

        if (!var13)
        {
            this.theFarmers.setPosition(var7, var9, var11);
            return false;
        }
        else
        {
        	if(!this.theFarmers.worldObj.isRemote)
        	{
                short var30 = 128;

                for (var18 = 0; var18 < var30; ++var18)
                {
                    double var19 = (double)var18 / ((double)var30 - 1.0D);
                    float var21 = (this.theFarmers.getRNG().nextFloat() - 0.5F) * 0.2F;
                    float var22 = (this.theFarmers.getRNG().nextFloat() - 0.5F) * 0.2F;
                    float var23 = (this.theFarmers.getRNG().nextFloat() - 0.5F) * 0.2F;
                    double var24 = var7 + (this.theFarmers.posX - var7) * var19 + (this.theFarmers.getRNG().nextDouble() - 0.5D) * (double)this.theFarmers.width * 2.0D;
                    double var26 = var9 + (this.theFarmers.posY - var9) * var19 + this.theFarmers.getRNG().nextDouble() * (double)this.theFarmers.height;
                    double var28 = var11 + (this.theFarmers.posZ - var11) * var19 + (this.theFarmers.getRNG().nextDouble() - 0.5D) * (double)this.theFarmers.width * 2.0D;
                    this.theFarmers.worldObj.spawnParticle("portal", var24, var26, var28, (double)var21, (double)var22, (double)var23);
                }
        	}

            this.theFarmers.worldObj.playSoundAtEntity(this.theFarmers, "mob.endermen.portal", 1.0F, 1.0F);
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
