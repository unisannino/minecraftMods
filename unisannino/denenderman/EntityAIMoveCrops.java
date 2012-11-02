package unisannino.denenderman;

import java.util.Random;
import net.minecraft.src.*;

public class EntityAIMoveCrops extends EntityAIBase
{

    private EntityDenEnderman theFarmers;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private float field_48299_e;
    private World theWorld;
    private boolean putSeed;

    public EntityAIMoveCrops(EntityDenEnderman par1EntityFarmers, float par2)
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
        if (!this.theWorld.isDaytime())
        {
            return false;
        }
        else
        {
            Vec3 var1 = this.findPossibleCrops();

            if (var1 == null)
            {
                var1 = this.findPossibleFarmlands();

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

    private Vec3 findPossibleCrops()
    {
        Random var1 = this.theFarmers.getRNG();

        for (int var2 = 0; var2 < 10; ++var2)
        {
            int var3 = MathHelper.floor_double(this.theFarmers.posX + (double)var1.nextInt(20) - 10.0D);
            int var4 = MathHelper.floor_double(this.theFarmers.boundingBox.minY + (double)var1.nextInt(6) - 3.0D);
            int var5 = MathHelper.floor_double(this.theFarmers.posZ + (double)var1.nextInt(20) - 10.0D);

            if (this.theFarmers.canHervest(theWorld, var3, var4, var5))
            {
                return this.theWorld.func_82732_R().getVecFromPool((double)var3, (double)var4, (double)var5);
            }
        }
        return null;
    }

    private Vec3 findPossibleFarmlands()
    {
        Random var1 = this.theFarmers.getRNG();

        for (int var2 = 0; var2 < 10; ++var2)
        {
            int var3 = MathHelper.floor_double(this.theFarmers.posX + (double)var1.nextInt(20) - 10.0D);
            int var4 = MathHelper.floor_double(this.theFarmers.boundingBox.minY + (double)var1.nextInt(6) - 3.0D);
            int var5 = MathHelper.floor_double(this.theFarmers.posZ + (double)var1.nextInt(20) - 10.0D);
            int var6 = this.theWorld.getBlockId(var3, var4, var5);
            int var7 = this.theWorld.getBlockId(var3, var4 + 1, var5);
            int var8 = var4 + 1;


            ItemStack itemstack = null;
			Object obj = null;
			ItemSeeds seeds = null;
			ItemSeedFood fseeds = null;
			int meta = 0;
	        for (int length = 0; length < this.theFarmers.inventory.mainInventory.length; length++)
	        {
	            if (this.theFarmers.inventory.mainInventory[length] != null)
	            {
	            	obj = this.theFarmers.inventory.getInventoryObject(length);
					if(obj instanceof ItemSeeds)
					{
						seeds = (ItemSeeds)obj;
						if(seeds.shiftedIndex == Item.pumpkinSeeds.shiftedIndex || seeds.shiftedIndex == Item.melonSeeds.shiftedIndex)
						{
							continue;
						}
						meta = this.theFarmers.inventory.getInventoryMetadata(length);
						itemstack = new ItemStack(seeds, 1, meta);
						break;
					}else if(obj instanceof ItemSeedFood)
					{
						fseeds = (ItemSeedFood) obj;
						meta = this.theFarmers.inventory.getInventoryMetadata(length);
						itemstack = new ItemStack(fseeds, 1, meta);
						break;
					}
	            }
	        }
	        if(itemstack != null)
	        {
	        	if(Block.blocksList[var6] != null && Block.blocksList[var6].isFertile(this.theWorld, var3, var4, var5) && this.theWorld.isAirBlock(var3, var4 + 1, var5))
	        	{
	        		return this.theWorld.func_82732_R().getVecFromPool((double)var3, (double)var4, (double)var5);
	        	}
	        }
        }
        return null;
    }

    /*
    protected boolean canHervest(World world, int i, int j, int k)
    {
		int l = world.getBlockId(i, j, k);
		int l1 = world.getBlockId(i, j - 1, k);
		int meta = world.getBlockMetadata(i, j, k);
        Block crops = Block.blocksList[l];
        Random rand = this.theFarmers.getRNG();

		if(l1 == Block.tilledField.blockID)
		{
			if((l == Block.crops.blockID && meta ==7) || l == Block.pumpkin.blockID || l == Block.melon.blockID)
			{
				return true;
			}else if(crops instanceof BlockCrops && crops.idDropped(meta, rand, 0) > 0)
            {
            	return true;
            }else
            {
            	if(mod_DenEnderman.ToggleXies)
            	{
                    try
                    {
                    	if(mod_XieClient.class.getClass() != null)
                    	{
                        	if(crops instanceof net.minecraft.src.xie.blocks.XieBlockPlant)
                        	{
                        		net.minecraft.src.xie.blocks.XieBlockPlant xiecrops = (net.minecraft.src.xie.blocks.XieBlockPlant)crops;
                    			if(ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops") != null)
                    			{
                    				int metaxie = theWorld.getBlockMetadata(i, j, k);
                    				if(metaxie == xiecrops.maxGrowth)
                    				{
                    					return true;
                    				}
                    			}
                        	}
                    	}
                    }catch(Exception e)
                    {
                    }
            	}
            }

		}else if(crops instanceof BlockCrops && crops.idDropped(meta, rand, 0) > 0)
		{
			BlockCrops plant = (BlockCrops)Block.blocksList[l];
			if(plant.canThisPlantGrowOnThisBlockID(l1))
			{
				return true;
			}
		}
		return false;
    }
    */
}
