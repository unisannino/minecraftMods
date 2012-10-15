package unisannino.denenderman;

import java.util.Random;
import net.minecraft.src.*;

public class EntityAIPlantCrops extends EntityAIBase
{

    private EntityDenEnderman theFarmers;
    private World theWorld;

    public EntityAIPlantCrops(EntityDenEnderman par1EntityFarmers)
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
    	this.hervestCrops(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);

    	Object obj = null;
    	for(int i = 0;i < this.theFarmers.inventory.mainInventory.length;i++)
    	{
        	obj = this.theFarmers.inventory.getInventoryObject(i);
        	if(obj == null)
        	{
        		continue;
        	}
        	if(obj instanceof ItemSeeds)
        	{
                this.plantCrops(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);
        	}else
        	if(obj instanceof ItemDye)
        	{
        		if(this.theFarmers.inventory.getInventoryMetadata(i) == 15)
        		{
        	        this.plantCrops(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);
        		}
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

    private void hervestCrops(double var1, double var2, double var3)
    {
        Random rand = this.theFarmers.getRNG();

        for (double h = 4.0D; h > 0.0D; h--)
        {
            //System.out.println("Y..." + checkheight);
            /*
            int fposX = MathHelper.floor_double(var1 + (double)rand.nextInt(2) - 1);
            int fposY = MathHelper.floor_double(var2 - 1 + h);
            int fposZ = MathHelper.floor_double(var3 + (double)rand.nextInt(2) - 1);
            */
            int fposX = MathHelper.floor_double((var1 - 1.0D) + rand.nextDouble() * 2D);
            int fposY = MathHelper.floor_double((var2 - 1.0D) + rand.nextDouble() * h);
            int fposZ = MathHelper.floor_double((var3 - 1.0D) + rand.nextDouble() * 2D);
            int fposBlock = this.theWorld.getBlockId(fposX, fposY, fposZ);
            int underblock = this.theWorld.getBlockId(fposX, fposY - 1, fposZ);
            int onFarmland = this.theWorld.getBlockId(fposX, fposY + 1 , fposZ);
            int fposYup = fposY + 1;

            int l4 = this.theWorld.getBlockMetadata(fposX, fposY + 1 , fposZ);


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


            if(onFarmland > 0)
            {
                Block crops = Block.blocksList[onFarmland];
                if(crops instanceof BlockCrops && crops.idDropped(l4, rand, 0) > 0)
                {
                    if (this.theFarmers.inventory.addItemStackToInventory(new ItemStack(crops.idDropped(l4, rand, 0), 1, 0)))
                    {
                    	crops.dropBlockAsItemWithChance(this.theWorld, fposX, fposYup, fposZ, 7, 1.0F, 0);
                    	this.theWorld.setBlockWithNotify(fposX, fposYup, fposZ, 0);
                    	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
            			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                        this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        if(!this.theFarmers.worldObj.isRemote)
                        {
                        	this.theFarmers.hervestSeed = true;
                        }
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
                    				int metaxie = this.theWorld.getBlockMetadata(fposX, fposYup, fposZ);
                    				if(metaxie == xiecrops.maxGrowth)
                    				{
                        				net.minecraft.src.xie.Droppables  drops = (net.minecraft.src.xie.Droppables)ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops");
                        				net.minecraft.src.xie.Xie.spawnItems(drops.getDropsForMetaAndMultiplier(metaxie, 0), this.theWorld, fposX, fposYup, fposZ);
                                    	this.theWorld.setBlockWithNotify(fposX, fposYup, fposZ, 0);
                                    	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                            			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                                        this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                                        this.theFarmers.hervestSeed = true;
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


            if (onFarmland == Block.melon.blockID)
            {
                if (this.theFarmers.getchanceItems(Item.melon, 5, 3))
                {
                	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
        			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                    this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    this.theWorld.setBlockWithNotify(fposX, fposYup, fposZ, 0);
                    if(!this.theFarmers.worldObj.isRemote)
                    {
                    	this.theFarmers.hervestSeed = true;
                    }
                }
            }

            if (onFarmland == Block.pumpkin.blockID)
            {
                if (this.theFarmers.inventory.addItemStackToInventory(new ItemStack(Block.pumpkin)))
                {
                	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
        			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                    this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    this.theWorld.setBlockWithNotify(fposX, fposYup, fposZ, 0);
                    if(!this.theFarmers.worldObj.isRemote)
                    {
                    	this.theFarmers.hervestSeed = true;
                    }
                }
            }
        }
    }

    private void plantCrops(double var1, double var2, double var3)
    {
    	Random rand = this.theFarmers.getRNG();

        for (double h = 4; h > 0; h--)
        {
            //System.out.println("Y..." + checkheight);
            /*
            int fposX = MathHelper.floor_double(var1 + (double)rand.nextInt(2) - 1);
            int fposY = MathHelper.floor_double(var2 - 1 + h);
            int fposZ = MathHelper.floor_double(var3 + (double)rand.nextInt(2) - 1);
            */
            int fposX = MathHelper.floor_double((var1 - 1.0D) + rand.nextDouble() * 2D);
            int fposY = MathHelper.floor_double((var2 - 1.0D) + rand.nextDouble() * h);
            int fposZ = MathHelper.floor_double((var3 - 1.0D) + rand.nextDouble() * 2D);
            int fposBlock = this.theWorld.getBlockId(fposX, fposY, fposZ);
            int underblock = this.theWorld.getBlockId(fposX, fposY - 1, fposZ);
            int onFarmland = this.theWorld.getBlockId(fposX, fposY + 1 , fposZ);
            int fposYup = fposY + 1;

            //doro katu uegananimonakattara tagayasu
            if (fposBlock == Block.dirt.blockID && onFarmland == 0)
            {
            	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
    			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                StepSound stepsound1 = Block.tilledField.stepSound;
                this.theWorld.playSoundAtEntity(this.theFarmers, stepsound1.getStepSound(), stepsound1.getPitch(), stepsound1.getPitch());
                this.theWorld.setBlockWithNotify(fposX, fposY, fposZ, Block.tilledField.blockID);
            }

            int l5 = this.theWorld.getBlockId(fposX + 1, fposY + 1 , fposZ);
            int l6 = this.theWorld.getBlockId(fposX - 1, fposY + 1 , fposZ);
            int l7 = this.theWorld.getBlockId(fposX, fposY + 1 , fposZ + 1);
            int l8 = this.theWorld.getBlockId(fposX, fposY + 1 , fposZ - 1);
            int l9 = this.theWorld.getBlockId(fposX + 1, fposY + 1 , fposZ + 1);
            int l10 = this.theWorld.getBlockId(fposX + 1, fposY + 1 , fposZ - 1);
            int l11 = this.theWorld.getBlockId(fposX - 1, fposY + 1 , fposZ - 1);
            int l12 = this.theWorld.getBlockId(fposX - 1, fposY + 1 , fposZ + 1);

            //種うえましょう
            if (l5 == Block.pumpkinStem.blockID || l6 == Block.pumpkinStem.blockID || l7 == Block.pumpkinStem.blockID || l8 == Block.pumpkinStem.blockID || l9 == Block.pumpkinStem.blockID || l10 == Block.pumpkinStem.blockID || l11 == Block.pumpkinStem.blockID || l12 == Block.pumpkinStem.blockID)
            {
            }
            else
            {
                //melon no nae check
                if (l5 == Block.melonStem.blockID || l6 == Block.melonStem.blockID || l7 == Block.melonStem.blockID || l8 == Block.melonStem.blockID || l9 == Block.melonStem.blockID || l10 == Block.melonStem.blockID || l11 == Block.melonStem.blockID || l12 == Block.melonStem.blockID)
                {
                }
                else
                {

					ItemStack itemstack = null;
					Object obj = null;
					ItemSeeds seeds = null;
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
							}
			            }
			        }
			        if(itemstack != null)
			        {
                        try
                        {
                            if(seeds != null)
                            {
                            	int cropblock = (Integer)ModLoader.getPrivateValue(ItemSeeds.class, seeds, 0);
                            	int baseblock = (Integer)ModLoader.getPrivateValue(ItemSeeds.class, seeds, 1);
                            	Block crops = Block.blocksList[cropblock];
                            	if(((fposBlock == baseblock && baseblock != 0 ) || crops.canPlaceBlockAt(this.theWorld, fposX, fposYup, fposZ) )&& onFarmland == 0)
                            	{
                            		if(this.theFarmers.inventory.consumeInventoryItem(itemstack.itemID, itemstack.getItemDamage()))
                            		{
                                    	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                            			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                                        StepSound stepsound2 = Block.crops.stepSound;
                                        this.theWorld.playSoundAtEntity(this.theFarmers, stepsound2.getStepSound(), stepsound2.getPitch(), stepsound2.getPitch());
    									this.theWorld.setBlockWithNotify(fposX, fposYup, fposZ, cropblock);
                            		}
                            	}
							}
                        }
                        catch (Exception e)
                        {
                        }
			        }
                }
            }

            int l4 = this.theWorld.getBlockMetadata(fposX, fposY + 1 , fposZ);

            //kosoku seicho
            if (onFarmland == Block.crops.blockID && l4 < 7)
            {
            	if(this.theFarmers.inventory.consumeInventoryItem(Item.dyePowder.shiftedIndex, 15))
            	{
                	this.theFarmers.facetoPath(fposX, fposYup, fposZ, 100F, 100F);
                    this.theWorld.playSoundAtEntity(this.theFarmers, "mob.chickenplop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    ((BlockCrops)Block.crops).fertilize(this.theWorld, fposX, fposYup, fposZ);
            	}
            }

            int l14 = MathHelper.floor_double(this.theFarmers.posY);
            int l13 = this.theWorld.getBlockId(fposX, l14 - 1, fposZ);
            int l15 = this.theWorld.getBlockId(fposX, l14, fposZ);

            if (rand.nextInt(2000) == 0)
            {
                if (l13 == Block.grass.blockID && l15 == 0)
                {
                    if (this.theFarmers.inventory.consumeInventoryItem(Mod_DenEnderman_Core.FarmerSeeds.shiftedIndex))
                    {
                    	this.theFarmers.facetoPath(fposX, l14, fposZ, 100F, 100F);
                        StepSound stepsound1 = Block.plantRed.stepSound;
                        this.theWorld.playSoundAtEntity(this.theFarmers, stepsound1.getStepSound(), stepsound1.getPitch(), stepsound1.getPitch());
                        this.theWorld.setBlockWithNotify(fposX, l14, fposZ, Mod_DenEnderman_Core.Lavender.blockID);
                    }
                }
            }
        }
    }
}

