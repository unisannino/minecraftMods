package unisannino.denenderman;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Deprecated
public class EntityAIPutDEBlock_Sort extends EntityAIBase
{

    private final EntityDenEnderman theFarmers;
    private final World theWorld;
    private int tilePosX;
    private int tilePosY;
    private int tilePosZ;
    public IInventory targetDEBInv;
    public TileEntity targetDEB;
    private final DEBlockSorter sorter;


	/*
	 * 一定範囲内の田園ダーブロックの中で一番近いものをソートする
	 * ・・・予定だったけどそのために田園ダーブロックの位置を取得する方法が思い当らなかったので保留。
	 */
    public EntityAIPutDEBlock_Sort(EntityDenEnderman par1EntityFarmers)
    {
        this.theFarmers = par1EntityFarmers;
        this.theWorld = par1EntityFarmers.worldObj;
        this.sorter = new DEBlockSorter(this.theFarmers);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if(this.theFarmers.inventory.getFirstEmptyStack() == -1)
        {

        	List var1 = this.theFarmers.deblockList;
        	Collections.sort(var1, this.sorter);
        	Iterator var2 = var1.iterator();

        	if(var2.hasNext())
        	{
        		TileEntityDenEnder target = (TileEntityDenEnder) var2.next();
        		targetDEB = target;
        		this.tilePosX = target.xCoord;
        		this.tilePosY = target.yCoord;
        		this.tilePosZ = target.zCoord;

        		System.out.println(this.theFarmers.getDistance(tilePosZ, tilePosY, tilePosZ));
        		return this.theFarmers.getDistance(tilePosZ, tilePosY, tilePosZ) < 12;
        	}


        }
        return false;
    }

    @Override
    public void updateTask()
    {
        if (this.targetDEB != null)
        {
            targetDEBInv = (IInventory)this.targetDEB;
            this.putDBlock();
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return this.targetDEB != null;
    }

    protected void putDBlock()
    {
        if (targetDEBInv != null)
        {
            for (int i = 0; i < this.theFarmers.inventory.getSizeInventory(); i++)
            {
                //System.out.println(i);
                ItemStack itemstackF = this.theFarmers.inventory.getStackInSlot(i);
                if (itemstackF != null)
                {
                	if(!this.theFarmers.checkItemCanPut(itemstackF.itemID))
                	{
                		continue;
                	}
                    boolean putcrop = false;

                    for (int j = 0; j < targetDEBInv.getSizeInventory(); j++)
                    {
                        ItemStack itemstackDE = targetDEBInv.getStackInSlot(j);

                        if (itemstackDE == null)
                        {
                            targetDEBInv.setInventorySlotContents(j, itemstackF);
                            this.theFarmers.inventory.setInventorySlotContents(i, null);
                            putcrop = true;
                            break;
                        }
                    }

                    if (putcrop)
                    {
                        this.theWorld.playSoundAtEntity(this.theFarmers, "random.pop", 0.5F, (this.theFarmers.getRNG().nextFloat() - this.theFarmers.getRNG().nextFloat()) * 0.2F + 1.0F);
                        String s = "Putted Inventory Items!!";
            			if(!this.theWorld.isRemote)
            			{
            				EntityPlayerMP player = ((EntityPlayerMP)this.theFarmers.getOwner());
            				player.sendChatToPlayer(s);
            			}
                    }
                    else
                    {
                        String s = "Oh No! Slot of DenEnderBlock is full!";
            			if(!this.theWorld.isRemote)
            			{
            				EntityPlayerMP player = ((EntityPlayerMP)this.theFarmers.getOwner());
            				player.sendChatToPlayer(s);
            			}
                    	this.theFarmers.deblockList.remove(this.theWorld.getBlockTileEntity(tilePosX, tilePosY, tilePosZ));
                        return;
                    }
                }
            }

            targetDEB = null;
            targetDEBInv = null;
            this.theFarmers.sayLogs = false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
    }
}

