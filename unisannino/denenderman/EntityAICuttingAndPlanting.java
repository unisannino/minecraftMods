package unisannino.denenderman;

import java.util.Random;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

public class EntityAICuttingAndPlanting extends EntityAIBase
{

    private EntityTreeper theFarmers;
    private World theWorld;

    public EntityAICuttingAndPlanting(EntityTreeper par1EntityFarmers)
    {
        this.theFarmers = par1EntityFarmers;
        this.theWorld = par1EntityFarmers.worldObj;
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
        	if(this.theFarmers.inventory.getFirstEmptyStack() >= 0)
        	{
        		return true;
        	}
        }
        return false;
    }

    public void updateTask()
    {
    	this.startCuttingLogs(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);

    	Object obj = null;
    	for(int i = 0;i < this.theFarmers.inventory.mainInventory.length;i++)
    	{
        	obj = this.theFarmers.inventory.getInventoryObject(i);
        	if(obj == null)
        	{
        		continue;
        	}
        	if(obj instanceof BlockSapling)
        	{
                this.plantSaplings(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);
        	}
    	}
    }

    public void resetTask()
    {
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    }


    private void startCuttingLogs(double var1, double var2, double var3)
    {


        Random rand = this.theFarmers.getRNG();

		for (int h = 3; h > -2; h--)
		{
			//System.out.println("Now Y: " +  (j + height));
			int x = MathHelper.floor_double((var1 - 1.0D) + rand.nextDouble() * 2D);
			int y = MathHelper.floor_double((var2 - 1.0D) + rand.nextDouble() * h);
			int z = MathHelper.floor_double((var3 - 1.0D) + rand.nextDouble() * 2D);
			int targetid = this.theWorld.getBlockId(x, y, z);
			int targetmeta = this.theWorld.getBlockMetadata(x, y, z);

			if (targetid == Block.wood.blockID)
			{
				if (this.theFarmers.underWoodBlock(this.theWorld, x, y, z) && this.theFarmers.checkTopOfLogs(this.theWorld, x, y, z) && this.theFarmers.checkIsLogs(this.theWorld, x, y, z))
				{
					this.theWorld.createExplosion(this.theFarmers, x, y, z, 0F, true);
					this.theFarmers.getLookHelper().setLookPosition(x, y, z, 10.0F, (float)this.theFarmers.getVerticalFaceSpeed());
					this.cutting(x, y, z, targetid, targetmeta);
				}
			}
		}
				//System.out.println("Under No");
    }

    private void cutting(int x, int y, int z, int id, int meta)
    {
    	Block block = Block.blocksList[id];
    	ItemStack item = new ItemStack(block.idDropped(meta, this.theFarmers.getRNG(), 0), block.quantityDropped(this.theFarmers.getRNG()) ,block.damageDropped(meta));
		if(this.theFarmers.inventory.addItemStackToInventory(item))
		{
			this.theWorld.setBlockWithNotify(x, y, z, 0);
			/*
			ChunkPosition posi = new ChunkPosition(x, y, z);
			this.theFarmers.checkposition.add(posi);
			*/
		}

		for(int y1 = 0;y1 <= 1;y1++)
		{
			for(int x1 = -1;x1 <= 1;x1++)
			{
				for(int z1 = -1;z1 <= 1;z1++)
				{
					//System.out.println((x + x1) +" "+ (y + y1) +" "+ (z + z1));
					if(this.theFarmers.checkIsLogs(this.theWorld, x + x1, y + y1, z + z1))
					{

						int cutid = this.theWorld.getBlockId(x + x1, y + y1, z + z1);
						int cutmeta = this.theWorld.getBlockMetadata(x + x1, y + y1, z + z1);
						cutting(x + x1, y + y1, z + z1, cutid, cutmeta);
					}
				}
			}
		}

    }

    private void plantSaplings(double var1, double var2, double var3)
    {
    	Random rand = this.theFarmers.getRNG();

        for (int h = 4; h > -2; h--)
        {
            int fposX = MathHelper.floor_double((var1 - 1.0D) + rand.nextDouble() * 2D);
            int fposY = MathHelper.floor_double((var2 - 1.0D) + rand.nextDouble() * h);
            int fposZ = MathHelper.floor_double((var3 - 1.0D) + rand.nextDouble() * 2D);
            int fposBlock = this.theWorld.getBlockId(fposX, fposY, fposZ);
            int underblock = this.theWorld.getBlockId(fposX, fposY - 1, fposZ);
            int onFarmland = this.theWorld.getBlockId(fposX, fposY + 1 , fposZ);
            int fposYup = fposY + 1;

			if (this.theFarmers.canPlanting(this.theWorld, fposX, fposY + h, fposZ))
			{
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
					if (this.theFarmers.inventory.consumeInventoryItem(itemstack.itemID, itemstack.getItemDamage()))
					{
						this.theWorld.setBlockAndMetadataWithNotify(fposX, fposY + h + 1, fposZ, itemstack.itemID, itemstack.getItemDamage());
						StepSound stepsound = Block.sapling.stepSound;
						this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 10.0F, (float)this.theFarmers.getVerticalFaceSpeed());
						this.theWorld.playSoundAtEntity(this.theFarmers, stepsound.getStepSound(), stepsound.getPitch(), stepsound.getPitch());
						break;
					}
		        }
			}

        }
    }
}

