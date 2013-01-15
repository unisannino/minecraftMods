package unisannino.denenderman;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;

@Deprecated
public class EntityAIMoveDEBlock_Sort extends EntityAIBase
{

    private EntityFarmers theFarmers;
    private double blockPosX;
    private double blockPosY;
    private double blockPosZ;
    private float moveSpeed;
    private World theWorld;
    private Minecraft mc = FMLClientHandler.instance().getClient();
    private DEBlockSorter sorter;

	/*
	 * 一定範囲内の田園ダーブロックの中で一番近いものをソートする
	 * ・・・予定だったけどそのために田園ダーブロックの位置を取得する方法が思い当らなかったので保留。
	 */
    public EntityAIMoveDEBlock_Sort(EntityFarmers par1EntityFarmers, float par2)
    {
        this.theFarmers = par1EntityFarmers;
        this.moveSpeed = par2;
        this.theWorld = par1EntityFarmers.worldObj;
        this.sorter = new DEBlockSorter(this.theFarmers);
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if(this.theFarmers.inventory.getFirstEmptyStack() == -1)
        {
            Vec3 var1 = this.findPossibleDEBlock();

            if (!(var1 == null))
            {
                this.blockPosX = var1.xCoord;
                this.blockPosY = var1.yCoord;
                this.blockPosZ = var1.zCoord;

        		String s = new StringBuilder(this.theFarmers.getFarmersName()).append(" is searching DenenderBlock...").toString();
        		if(mc.thePlayer != null)
        		{
        			if(!this.theFarmers.worldObj.isRemote && !this.theFarmers.sayLogs)
        			{
            			mc.thePlayer.addChatMessage(s);
            			this.theFarmers.sayLogs = true;
        			}
        		}

                return true;
            }
        }

        return false;

    }

    @Override
    public boolean continueExecuting()
    {
    	return this.theFarmers.getNavigator().noPath();
    }

    @Override
    public void startExecuting()
    {
        this.theFarmers.getNavigator().tryMoveToXYZ(this.blockPosX, this.blockPosY, this.blockPosZ, this.moveSpeed);
    }

    private Vec3 findPossibleDEBlock()
    {
    	List<TileEntityDenEnder> var1 = this.theFarmers.deblockList;
    	Collections.sort(var1, sorter);
    	Iterator var2 = var1.iterator();
    	if(var2.hasNext())
    	{
    		TileEntityDenEnder target = (TileEntityDenEnder) var2.next();
    		int var3 = target.xCoord;
    		int var4 = target.yCoord;
    		int var5 = target.zCoord;
    		return this.theWorld.getWorldVec3Pool().getVecFromPool((double)var3, (double)var4, (double)var5);
    	}

        return null;
    }

}
