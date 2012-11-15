package unisannino.denenderman;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

public class EntityAIUniHervestableAndEatable extends EntityAIBase
{

    private EntityUniuni theFarmers;
    private World theWorld;

    public EntityAIUniHervestableAndEatable(EntityUniuni par1EntityFarmers)
    {
        this.theFarmers = par1EntityFarmers;
        this.theWorld = par1EntityFarmers.worldObj;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	return this.theFarmers.inventory.getFirstEmptyStack() >= 0;

    }

    public void updateTask()
    {
    	this.hervest(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);

        if(this.theFarmers.getHealth() < this.theFarmers.getMaxHealth())
        {
        	this.eating(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);
        }
    }

    private void hervest(double posX, double posY, double posZ)
    {
        int x = MathHelper.floor_double((posX - 1.0D) + this.theFarmers.getRNG().nextDouble() * 2D);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double((posZ - 1.0D) + this.theFarmers.getRNG().nextDouble() * 2D);
        int r = this.theWorld.getBlockId(x, y, z);
        int r1 = this.theWorld.getBlockId(x, y + 1, z);
        int r2 = this.theWorld.getBlockId(x, y + 2, z);
        int meta = this.theWorld.getBlockMetadata(x, y, z);

        if(r > 0)
        {
            Block crops = Block.blocksList[r];
            ArrayList<ItemStack> drop = crops.getBlockDropped(this.theWorld, x, y, z, meta, 0);
            StepSound stepsound;
            if(crops instanceof BlockReed && this.isReed(r, r1, r2))
            {
                stepsound = crops.stepSound;
                this.theWorld.playSoundAtEntity(this.theFarmers, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
                this.theWorld.setBlockWithNotify(x, y , z, 0);
                if(this.theFarmers.inventory.consumeInventoryItem(crops.idDropped(meta, this.theFarmers.getRNG(), 0)))
                {
                    this.theWorld.setBlockWithNotify(x, y , z, crops.blockID);
                }
                crops.dropBlockAsItemWithChance(this.theWorld, x, y, z, 0, 1.0F, 0);
            }else
            if(crops instanceof BlockNetherStalk && meta == 3)
            {
                stepsound = crops.stepSound;
                this.theWorld.playSoundAtEntity(this.theFarmers, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
                this.theWorld.setBlockWithNotify(x, y , z, 0);
                for(ItemStack item: drop)
                {
                    if(this.theFarmers.inventory.consumeInventoryItem(item.itemID))
                    {
                        this.theWorld.setBlockAndMetadataWithNotify(x, y, z, crops.blockID, 0);
                    }
                }
                crops.dropBlockAsItemWithChance(this.theWorld, x, y, z, 3, 1.0F, 0);
            }
        }
	}

    private void eating(double posX, double posY, double posZ)
    {
        int x = MathHelper.floor_double((posX - 1.0D) + this.theFarmers.getRNG().nextDouble() * 2D);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double((posZ - 1.0D) + this.theFarmers.getRNG().nextDouble() * 2D);
        int bid = this.theWorld.getBlockId(x, y, z);

        if (this.theFarmers.canEatBlocks(this.theWorld, x, y, z))
        {
            StepSound stepsound = Block.grass.stepSound;

            if (bid == Block.melon.blockID)
            {
                stepsound = Block.melon.stepSound;
            }

            this.theWorld.playSoundAtEntity(this.theFarmers, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
            ItemStack itemstack = new ItemStack(Item.seeds, 1);

            if (bid == Block.melon.blockID)
            {
                itemstack = new ItemStack(Item.melonSeeds, 1);
            }

            if(bid != Block.melon.blockID)
            {
            	this.theFarmers.heal(1);
            }else
            {
            	this.theFarmers.heal(this.theFarmers.getMaxHealth());
            }

            EntityItem entityitem = new EntityItem(this.theWorld, x, y, z, itemstack);
            entityitem.delayBeforeCanPickup = 10;
            if(this.theWorld.setBlockWithNotify(x, y, z, 0))
            {
                this.theWorld.spawnEntityInWorld(entityitem);
            }else
            	System.out.println("bug");
        }
	}

    private boolean isReed(int bid1, int bid2, int bid3)
    {
    	return bid1 == bid2 && bid1 == bid3;
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
}

