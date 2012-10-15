package unisannino.denenderman;

import java.util.Random;
import net.minecraft.src.*;

public class EntityAIHervestCrops extends EntityAIBase
{

    private EntityDenEnderman theFarmers;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private float field_48299_e;
    private World theWorld;

    public EntityAIHervestCrops(EntityDenEnderman par1EntityFarmers, float par2)
    {
        this.theFarmers = par1EntityFarmers;
        this.field_48299_e = par2;
        this.theWorld = par1EntityFarmers.worldObj;
        this.setMutexBits(2);
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
        	return this.theFarmers.inventory.getFirstEmptyStack() > 0;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	return this.theFarmers.onGround;
    }

    public void resetTasks()
    {
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	Random rand = this.theFarmers.getRNG();

        for (double checkheight = 4.0D; checkheight > 0.0D; checkheight--)
        {
            //System.out.println("Y..." + checkheight);
            int i = MathHelper.floor_double(this.theFarmers.posX);
            int l = MathHelper.floor_double(this.theFarmers.posY - 1);
            int j1 = MathHelper.floor_double(this.theFarmers.posZ);
            int l1 = this.theWorld.getBlockId(i, l, j1);
            int underblock = this.theWorld.getBlockId(i, l - 1, j1);
            int l2 = this.theWorld.getBlockId(i, l + 1 , j1);
            int l3 = l + 1;

            int l5 = this.theWorld.getBlockId(i + 1, l + 1 , j1);
            int l6 = this.theWorld.getBlockId(i - 1, l + 1 , j1);
            int l7 = this.theWorld.getBlockId(i, l + 1 , j1 + 1);
            int l8 = this.theWorld.getBlockId(i, l + 1 , j1 - 1);
            int l9 = this.theWorld.getBlockId(i + 1, l + 1 , j1 + 1);
            int l10 = this.theWorld.getBlockId(i + 1, l + 1 , j1 - 1);
            int l11 = this.theWorld.getBlockId(i - 1, l + 1 , j1 - 1);
            int l12 = this.theWorld.getBlockId(i - 1, l + 1 , j1 + 1);

            int l4 = this.theWorld.getBlockMetadata(i, l + 1 , j1);

            /*
            if (l2 == Block.crops.blockID && l4 == 7)
            {
                if (this.theFarmers.inventory.addItemStackToInventory(new ItemStack(Item.wheat)))
                {
                    this.theFarmers.GrowPlants = true;
                    this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                    this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    this.theWorld.setBlockWithNotify(i, l3, j1, 0);

                    if (this.theFarmers.getchanceItems(Item.seeds, 3, 0))
                    {
                    }
                }
            }
            */

            if(l2 > 0)
            {
                Block crops = Block.blocksList[l2];
                if(crops instanceof BlockCrops && crops.idDropped(l4, rand, 0) > 0)
                {
                    if (this.theFarmers.inventory.addItemStackToInventory(new ItemStack(crops.idDropped(l4, rand, 0), 1, 0)))
                    {
                    	crops.dropBlockAsItemWithChance(this.theWorld, i, l3, j1, 7, 1.0F, 0);
                    	this.theWorld.setBlockWithNotify(i, l3, j1, 0);
                        this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                        this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    }
                }else
                try
                {
                	if(Mod_DenEnderman_Core.ToggleXies)
                	{
                		/*
                    	if(mod_XieClient.class.getClass() != null)
                    	{
                        	if(crops instanceof net.minecraft.src.xie.blocks.XieBlockPlant)
                        	{
                        		net.minecraft.src.xie.blocks.XieBlockPlant xiecrops = (net.minecraft.src.xie.blocks.XieBlockPlant)crops;
                    			if(ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops") != null)
                    			{
                    				int metaxie = this.theWorld.getBlockMetadata(i, l3, j1);
                    				if(metaxie == xiecrops.maxGrowth)
                    				{
                        				net.minecraft.src.xie.Droppables  drops = (net.minecraft.src.xie.Droppables)ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops");
                        				net.minecraft.src.xie.Xie.spawnItems(drops.getDropsForMetaAndMultiplier(metaxie, 0), this.theWorld, i, l3, j1);
                                    	this.theWorld.setBlockWithNotify(i, l3, j1, 0);
                                        this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                                        this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    				}
                    			}
                        	}
                    	}
                    	*/
                	}
                }catch(Exception e)
                {
                }
            }


            if (l2 == Block.melon.blockID)
            {
                if (this.theFarmers.getchanceItems(Item.melon, 5, 3))
                {
                    this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                    this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    this.theWorld.setBlockWithNotify(i, l3, j1, 0);
                }
            }

            if (l2 == Block.pumpkin.blockID)
            {
                if (this.theFarmers.inventory.addItemStackToInventory(new ItemStack(Block.pumpkin)))
                {
                    this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                    this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    this.theWorld.setBlockWithNotify(i, l3, j1, 0);
                }
            }
        }
    }

}
