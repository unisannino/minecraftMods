package unisannino.denenderman;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.src.*;

public class DEHome
{
    private final World worldObj;

    /** list of VillageDoorInfo objects */
    private final List villageDoorInfoList = new ArrayList();

    /**
     * This is the sum of all door coordinates and used to calculate the actual village center by dividing by the number
     * of doors.
     */
    private final ChunkCoordinates centerHelper = new ChunkCoordinates(0, 0, 0);

    /** This is the actual village center. */
    private final ChunkCoordinates center = new ChunkCoordinates(0, 0, 0);
    private int villageRadius = 0;
    private int lastAddDoorTimestamp = 0;
    private int tickCounter = 0;
    private int numVillagers = 0;
    private List villageAgressors = new ArrayList();
    private TreeMap field_82693_j = new TreeMap();

    public DEHome(World par1World)
    {
        this.worldObj = par1World;
    }

    /**
     * Called periodically by VillageCollection
     */
    public void tick(int par1)
    {
        this.tickCounter = par1;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();

        if (par1 % 20 == 0)
        {
            this.updateNumVillagers();
        }

    }

    private void updateNumVillagers()
    {
        List var1 = this.worldObj.getEntitiesWithinAABB(EntityVillager.class, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)(this.center.posX - this.villageRadius), (double)(this.center.posY - 4), (double)(this.center.posZ - this.villageRadius), (double)(this.center.posX + this.villageRadius), (double)(this.center.posY + 4), (double)(this.center.posZ + this.villageRadius)));
        this.numVillagers = var1.size();

        if (this.numVillagers == 0)
        {
            this.field_82693_j.clear();
        }
    }

    public ChunkCoordinates getCenter()
    {
        return this.center;
    }

    public int getVillageRadius()
    {
        return this.villageRadius;
    }

    /**
     * Actually get num village door info entries, but that boils down to number of doors. Called by
     * EntityAIVillagerMate and VillageSiege
     */
    public int getNumVillageDoors()
    {
        return this.villageDoorInfoList.size();
    }

    public int getTicksSinceLastDoorAdding()
    {
        return this.tickCounter - this.lastAddDoorTimestamp;
    }

    public int getNumVillagers()
    {
        return this.numVillagers;
    }

    /**
     * Returns true, if the given coordinates are within the bounding box of the village.
     */
    public boolean isInRange(int par1, int par2, int par3)
    {
        return this.center.getDistanceSquared(par1, par2, par3) < (float)(this.villageRadius * this.villageRadius);
    }

    /**
     * called only by class EntityAIMoveThroughVillage
     */
    public List getVillageDoorInfoList()
    {
        return this.villageDoorInfoList;
    }

    public DEHomeGateInfo findNearestDoor(int par1, int par2, int par3)
    {
    	DEHomeGateInfo var4 = null;
        int var5 = Integer.MAX_VALUE;
        Iterator var6 = this.villageDoorInfoList.iterator();

        while (var6.hasNext())
        {
        	DEHomeGateInfo var7 = (DEHomeGateInfo)var6.next();
            int var8 = var7.getDistanceSquared(par1, par2, par3);

            if (var8 < var5)
            {
                var4 = var7;
                var5 = var8;
            }
        }

        return var4;
    }

    /**
     * Find a door suitable for shelter. If there are more doors in a distance of 16 blocks, then the least restricted
     * one (i.e. the one protecting the lowest number of villagers) of them is chosen, else the nearest one regardless
     * of restriction.
     */
    public DEHomeGateInfo findNearestGateUnrestricted(int par1, int par2, int par3)
    {
    	DEHomeGateInfo var4 = null;
        int var5 = Integer.MAX_VALUE;
        Iterator var6 = this.villageDoorInfoList.iterator();

        while (var6.hasNext())
        {
        	DEHomeGateInfo var7 = (DEHomeGateInfo)var6.next();
            int var8 = var7.getDistanceSquared(par1, par2, par3);

            if (var8 > 256)
            {
                var8 *= 1000;
            }
            else
            {
                var8 = var7.getDoorOpeningRestrictionCounter();
            }

            if (var8 < var5)
            {
                var4 = var7;
                var5 = var8;
            }
        }

        return var4;
    }

    public DEHomeGateInfo getVillageDoorAt(int par1, int par2, int par3)
    {
        if (this.center.getDistanceSquared(par1, par2, par3) > (float)(this.villageRadius * this.villageRadius))
        {
            return null;
        }
        else
        {
            Iterator var4 = this.villageDoorInfoList.iterator();
            DEHomeGateInfo var5;

            do
            {
                if (!var4.hasNext())
                {
                    return null;
                }

                var5 = (DEHomeGateInfo)var4.next();
            }
            while (var5.posX != par1 || var5.posZ != par3 || Math.abs(var5.posY - par2) > 1);

            return var5;
        }
    }

    public void addVillageDoorInfo(DEHomeGateInfo par1VillageDoorInfo)
    {
        this.villageDoorInfoList.add(par1VillageDoorInfo);
        this.centerHelper.posX += par1VillageDoorInfo.posX;
        this.centerHelper.posY += par1VillageDoorInfo.posY;
        this.centerHelper.posZ += par1VillageDoorInfo.posZ;
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = par1VillageDoorInfo.lastActivityTimestamp;
    }

    /**
     * Returns true, if there is not a single village door left. Called by VillageCollection
     */
    public boolean isAnnihilated()
    {
        return this.villageDoorInfoList.isEmpty();
    }

    public void addOrRenewAgressor(EntityLiving par1EntityLiving)
    {
        Iterator var2 = this.villageAgressors.iterator();
        DEHomeAgressor var3;

        do
        {
            if (!var2.hasNext())
            {
                this.villageAgressors.add(new DEHomeAgressor(this, par1EntityLiving, this.tickCounter));
                return;
            }

            var3 = (DEHomeAgressor)var2.next();
        }
        while (var3.agressor != par1EntityLiving);

        var3.agressionTime = this.tickCounter;
    }

    public EntityLiving findNearestVillageAggressor(EntityLiving par1EntityLiving)
    {
        double var2 = Double.MAX_VALUE;
        DEHomeAgressor var4 = null;
        Iterator var5 = this.villageAgressors.iterator();

        while (var5.hasNext())
        {
        	DEHomeAgressor var6 = (DEHomeAgressor)var5.next();
            double var7 = var6.agressor.getDistanceSqToEntity(par1EntityLiving);

            if (var7 <= var2)
            {
                var4 = var6;
                var2 = var7;
            }
        }

        return var4 != null ? var4.agressor : null;
    }

    private void removeDeadAndOldAgressors()
    {
        Iterator var1 = this.villageAgressors.iterator();

        while (var1.hasNext())
        {
        	DEHomeAgressor var2 = (DEHomeAgressor)var1.next();

            if (!var2.agressor.isEntityAlive() || Math.abs(this.tickCounter - var2.agressionTime) > 300)
            {
                var1.remove();
            }
        }
    }

    private void removeDeadAndOutOfRangeDoors()
    {
        boolean var1 = false;
        boolean var2 = this.worldObj.rand.nextInt(50) == 0;
        Iterator var3 = this.villageDoorInfoList.iterator();

        while (var3.hasNext())
        {
        	DEHomeGateInfo var4 = (DEHomeGateInfo)var3.next();

            if (var2)
            {
                var4.resetDoorOpeningRestrictionCounter();
            }

            if (!this.isBlockGate(var4.posX, var4.posY, var4.posZ) || Math.abs(this.tickCounter - var4.lastActivityTimestamp) > 1200)
            {
                this.centerHelper.posX -= var4.posX;
                this.centerHelper.posY -= var4.posY;
                this.centerHelper.posZ -= var4.posZ;
                var1 = true;
                var4.isDetachedFromVillageFlag = true;
                var3.remove();
            }
        }

        if (var1)
        {
            this.updateVillageRadiusAndCenter();
        }
    }

    private boolean isBlockGate(int par1, int par2, int par3)
    {
        int var4 = this.worldObj.getBlockId(par1, par2, par3);
        boolean var5 = this.worldObj.getBlockId(par1, par2 - 1, par3) == Block.glowStone.blockID;
        return var4 <= 0 ? false : var4 == Block.fenceGate.blockID && var5;
    }

    private void updateVillageRadiusAndCenter()
    {
        int var1 = this.villageDoorInfoList.size();

        if (var1 == 0)
        {
            this.center.set(0, 0, 0);
            this.villageRadius = 0;
        }
        else
        {
            this.center.set(this.centerHelper.posX / var1, this.centerHelper.posY / var1, this.centerHelper.posZ / var1);
            int var2 = 0;
            DEHomeGateInfo var4;

            for (Iterator var3 = this.villageDoorInfoList.iterator(); var3.hasNext(); var2 = Math.max(var4.getDistanceSquared(this.center.posX, this.center.posY, this.center.posZ), var2))
            {
                var4 = (DEHomeGateInfo)var3.next();
            }

            this.villageRadius = Math.max(32, (int)Math.sqrt((double)var2) + 1);
        }
    }
}
