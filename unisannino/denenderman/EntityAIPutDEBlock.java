package unisannino.denenderman;

import java.util.Random;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIPutDEBlock extends EntityAIBase
{

    private final EntityFarmers theFarmers;
    private final World theWorld;
    private int tilePosX;
    private int tilePosY;
    private int tilePosZ;
    public IInventory targetDEBInv;
    public TileEntity targetDEB;
    private final DEBlockSorter sorter;


    public EntityAIPutDEBlock(EntityFarmers par1EntityFarmers)
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
    	Random rand = this.theFarmers.getRNG();

        if(this.theFarmers.inventory.getFirstEmptyStack() == -1)
        {
            for (double var1 = 4.0D; var1 > 0.0D; var1--)
            {
                int var2 = MathHelper.floor_double((this.theFarmers.posX - 1.0D) + rand.nextDouble() * 2D);
                int var3 = MathHelper.floor_double((this.theFarmers.posY - 1.0D) + rand.nextDouble() * var1);
                int var4 = MathHelper.floor_double((this.theFarmers.posZ - 1.0D) + rand.nextDouble() * 2D);
                TileEntity var5 = this.theWorld.getBlockTileEntity(var2, var3, var4);

            	if(var5 instanceof TileEntityDenEnder)
            	{
            		TileEntityDenEnder target = (TileEntityDenEnder) var5;
            		targetDEB = target;
            		this.tilePosX = target.xCoord;
            		this.tilePosY = target.yCoord;
            		this.tilePosZ = target.zCoord;

            		return target.getFirstEmptyStack() > -1;

            	}
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
            boolean putcrop = false;

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

                    if (!putcrop)
                    {

                        String s = "Oh No! Slot of DenEnderBlock is full!";
            			if(!this.theWorld.isRemote && theFarmers.getOwnerName().equalsIgnoreCase(this.theFarmers.getOwnerName()))
            			{
            				EntityPlayerMP player = ((EntityPlayerMP)this.theFarmers.getOwner());
            				player.sendChatToPlayer(s);
            			}
                    	this.theFarmers.deblockList.remove(this.theWorld.getBlockTileEntity(tilePosX, tilePosY, tilePosZ));
                        return;
                    }
                }
            }

            if (putcrop)
            {
                this.theWorld.playSoundAtEntity(this.theFarmers, "random.pop", 0.5F, (this.theFarmers.getRNG().nextFloat() - this.theFarmers.getRNG().nextFloat()) * 0.2F + 1.0F);
                String s = "Putted Inventory Items!!";
    			if(!this.theWorld.isRemote && this.theFarmers.getOwnerName().equalsIgnoreCase(this.theFarmers.getOwnerName()))
    			{
    				EntityPlayerMP player = ((EntityPlayerMP)this.theFarmers.getOwner());
    				player.sendChatToPlayer(s);
    			}
    			putcrop = false;
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

