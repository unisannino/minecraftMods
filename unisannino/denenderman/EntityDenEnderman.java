package unisannino.denenderman;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.*;

public class EntityDenEnderman extends EntityFarmers
{
    private EntityPlayer player;
    private int teleportDelay;
    private int field_35185_e;
    public int HeadID;

    private int stackamount;
	private int randomTickDivider;
	private DEHome homeObj;

	@SideOnly(Side.CLIENT)
    protected boolean hervestSeed;

    public EntityDenEnderman(World world)
    {
        super(world);
        teleportDelay = 0;
        field_35185_e = 0;

        if (Mod_DenEnderman_Core.onepiece)
        {
            texture = "/denender/denendermanMonkey.png";
        }
        else
        {
            texture = "/denender/denenderman.png";
        }

        moveSpeed = 0.28F;
        setSize(0.6F, 2.9F);
        stepHeight = 1.0F;

        isJumping =false;
        favoriteItem = Mod_DenEnderman_Core.Lavender.blockID;
        setcanPickup(0, Item.wheat.shiftedIndex);
        setcanPickup(1, Item.seeds.shiftedIndex);
        setcanPickup(2, Item.melon.shiftedIndex);
        setcanPickup(3, Block.pumpkin.blockID);
        setcanPickup(4, Item.melonSeeds.shiftedIndex);
        setcanPickup(5, Item.pumpkinSeeds.shiftedIndex);
        setcanPickup(6, Mod_DenEnderman_Core.Lavender.blockID);

        setcantPutItem(0, Item.seeds.shiftedIndex);
        setcantPutItem(1, Item.dyePowder.shiftedIndex);

        this.randomTickDivider = 0;
        this.hervestSeed = false;

        this.getNavigator().setBreakDoors(true);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAINearestTargetItems(this, this.moveSpeed));
        this.tasks.addTask(2, new EntityAIMoveCrops(this, this.moveSpeed));
        this.tasks.addTask(2, new EntityAIMoveIngates(this));
        this.tasks.addTask(3, new EntityAITempt(this, this.moveSpeed, this.favoriteItem, false));
        this.tasks.addTask(3, new EntityAIRestrictOpenGate(this));
        this.tasks.addTask(3, new EntityAIPlantCrops(this));
        this.tasks.addTask(4, new EntityAIOpenGate(this, true));
        this.tasks.addTask(4, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
        this.tasks.addTask(6, new EntityAIWander(this, this.moveSpeed));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)0));
        this.dataWatcher.addObject(17, new Byte((byte)0));
    }


    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    @Override
    protected void updateAITick()
    {
        if (--this.randomTickDivider <= 0)
        {
        	this.homecollection.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.homeObj = this.homecollection.findNearestDEHome(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);

            if (this.homeObj == null)
            {
                this.detachHome();
            }
            else
            {
                ChunkCoordinates var1 = this.homeObj.getCenter();
                this.setHomeArea(var1.posX, var1.posY, var1.posZ, this.homeObj.getVillageRadius());
            }
        }

        super.updateAITick();
    }

    @Override
    protected void addConfigPickupItems()
    {
    	String s[] = Mod_DenEnderman_Core.canPickupDE.split(",");
    	for(int j = 0; j < s.length; j++)
    	{
    		int id = Integer.parseInt(s[j].trim());
    		setcanPickup(id);
    	}
    }

    @Override
    public int getMaxHealth()
    {
        return 13;
    }

    @Override
    public int getBrightnessForRender(float f)
    {
        return super.getBrightnessForRender(f);
    }

    @Override
    public float getBrightness(float f)
    {
        return super.getBrightness(f);
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if(this.findItems || this.hervestSeed)
        {
        	this.setFaceAngry(true);
        }else
        {
        	this.setFaceAngry(false);
        }


        for (int k = 0; k < 2; k++)
        {
            worldObj.spawnParticle("reddust", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double)width, 2.0D, 200.0D, 00D);
        }
    }

    @Override
    protected boolean checkItemID(int i)
    {
    	if(i > 0)
    	{
    		if(i < 256)
    		{
    			Block block = Block.blocksList[i];
    			if(block instanceof BlockFlower)
    			{
    				return true;
    			}
    		}else
    		{
    			Item item = Item.itemsList[i];
    			if(item instanceof ItemFood)
    			{
    				return true;
    			}
    			if(item instanceof ItemSeeds)
    			{
    				return true;
    			}
    		}
    	}
    	return super.checkItemID(i);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Head", HeadID);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        HeadID = nbttagcompound.getInteger("Head");
    }

    @Override
    protected void jump()
    {
    	if(isInWater() || handleLavaMovement())
    	{
    		super.jump();
    	}
    }

    @Override
    protected String getLivingSound()
    {
        if (!Mod_DenEnderman_Core.crying)
        {
            return null;
        }
        else
        {
            return "mob.endermen.idle";
        }
    }

    @Override
    protected String getHurtSound()
    {
        if (!Mod_DenEnderman_Core.crying)
        {
            return null;
        }
        else
        {
            return "mob.endermen.hit";
        }
    }

    @Override
    protected String getDeathSound()
    {
        if (!Mod_DenEnderman_Core.crying)
        {
            return null;
        }
        else
        {
            return "mob.endermen.death";
        }
    }
    @Override
    protected int getDropItemId()
    {
        return Mod_DenEnderman_Core.DenEnderPearl.shiftedIndex;
    }

    protected boolean canHervest(World world, int i, int j, int k)
    {
		int l = world.getBlockId(i, j, k);
		int l1 = world.getBlockId(i, j - 1, k);
		int meta = world.getBlockMetadata(i, j, k);
        Block crops = Block.blocksList[l];

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
            	if(Mod_DenEnderman_Core.ToggleXies)
            	{
            		/*
                    try
                    {
                    	if(mod_XieClient.class.getClass() != null)
                    	{
                        	if(crops instanceof net.minecraft.src.xie.blocks.XieBlockPlant)
                        	{
                        		net.minecraft.src.xie.blocks.XieBlockPlant xiecrops = (net.minecraft.src.xie.blocks.XieBlockPlant)crops;
                    			if(ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops") != null)
                    			{
                    				int metaxie = world.getBlockMetadata(i, j, k);
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
                    */
            	}
            }

		}else if(crops instanceof BlockCrops && crops.idDropped(meta, rand, 0) > 0)
		{
			BlockCrops plant = (BlockCrops)Block.blocksList[l];
			//if(plant.canThisPlantGrowOnThisBlockID(l1))
			if(isPlantGrowBlocks(l1))
			{
				return true;
			}
		}
		return false;
    }

    private boolean isPlantGrowBlocks(int blockId)
    {
		try
		{
			Object result;
			Method crop = Class.forName("BlockCrops").getMethod("canThisPlantGrowOnThisBlockID", new Class[]{Integer.class});
			result = crop.invoke(null, new Object[] { blockId });
			return (Boolean) result;

		}catch(ClassNotFoundException e)
		{
			System.out.println("error");
			return false;

		}catch(Exception e)
		{
			System.out.println("error");
			return false;
		}
    }

    @SideOnly(Side.CLIENT)
    public boolean getFaceAngry()
    {
        return this.dataWatcher.getWatchableObjectByte(17) > 0;
    }

    public void setFaceAngry(boolean par1)
    {
        this.dataWatcher.updateObject(17, Byte.valueOf((byte)(par1 ? 1 : 0)));
    }

/*
    private void createFarms()
    {
        for (double checkheight = 4; checkheight > 0; checkheight--)
        {
            //System.out.println("Y..." + checkheight);
            int i = MathHelper.floor_double((posX - 1.0D) + rand.nextDouble() * 2D);
            int l = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * checkheight);
            int j1 = MathHelper.floor_double((posZ - 1.0D) + rand.nextDouble() * 2D);
            int l1 = worldObj.getBlockId(i, l, j1);
            int underblock = worldObj.getBlockId(i, l - 1, j1);
            int l2 = worldObj.getBlockId(i, l + 1 , j1);
            int l3 = l + 1;

            //doro katu uegananimonakattara tagayasu
            if (l1 == Block.dirt.blockID && l2 == 0)
            {
            	facetoPath(i, l, j1, 100F, 100F);
                StepSound stepsound1 = Block.tilledField.stepSound;
                worldObj.playSoundAtEntity(this, stepsound1.getStepSound(), stepsound1.getPitch(), stepsound1.getPitch());
                worldObj.setBlockWithNotify(i, l, j1, Block.tilledField.blockID);
            }

            int l5 = worldObj.getBlockId(i + 1, l + 1 , j1);
            int l6 = worldObj.getBlockId(i - 1, l + 1 , j1);
            int l7 = worldObj.getBlockId(i, l + 1 , j1 + 1);
            int l8 = worldObj.getBlockId(i, l + 1 , j1 - 1);
            int l9 = worldObj.getBlockId(i + 1, l + 1 , j1 + 1);
            int l10 = worldObj.getBlockId(i + 1, l + 1 , j1 - 1);
            int l11 = worldObj.getBlockId(i - 1, l + 1 , j1 - 1);
            int l12 = worldObj.getBlockId(i - 1, l + 1 , j1 + 1);

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
			        for (int length = 0; length < inventory.mainInventory.length; length++)
			        {
			            if (inventory.mainInventory[length] != null)
			            {
			            	obj = inventory.getInventoryObject(length);
							if(obj instanceof ItemSeeds)
							{
								seeds = (ItemSeeds)obj;
								if(seeds.shiftedIndex == Item.pumpkinSeeds.shiftedIndex || seeds.shiftedIndex == Item.melonSeeds.shiftedIndex)
								{
									continue;
								}
								meta = inventory.getInventoryMetadata(length);
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
                            	if(((l1 == baseblock && baseblock != 0 ) || crops.canPlaceBlockAt(worldObj, i, l3, j1) )&& l2 == 0)
                            	{
                            		if(inventory.consumeInventoryItem(itemstack.itemID, itemstack.getItemDamage()))
                            		{
                                    	facetoPath(i, l3, j1, 100F, 100F);
                                        StepSound stepsound2 = Block.crops.stepSound;
                                        worldObj.playSoundAtEntity(this, stepsound2.getStepSound(), stepsound2.getPitch(), stepsound2.getPitch());
    									worldObj.setBlockWithNotify(i, l3, j1, cropblock);
                            		}
                            	}
							}
                        }
                        catch (Exception e)
                        {
                        }
                        //worldObj.setBlockWithNotify(i, l3, j1, itemstack.itemID);
			        }
                    if (inventory.consumeInventoryItem(Item.seeds.shiftedIndex))
                    {
                        //wheat no tanemaki
                    	facetoPath(i, l3, j1, 100F, 100F);
                        StepSound stepsound2 = Block.crops.stepSound;
                        worldObj.playSoundAtEntity(this, stepsound2.getStepSound(), stepsound2.getPitch(), stepsound2.getPitch());
                        worldObj.setBlockWithNotify(i, l3, j1, Block.crops.blockID);
                    }
                }
            }


            int l4 = worldObj.getBlockMetadata(i, l + 1 , j1);

            //kosoku seicho
            if (l2 == Block.crops.blockID && l4 < 7)
            {
            	if(inventory.consumeInventoryItem(Item.dyePowder.shiftedIndex, 15))
            	{
                	facetoPath(i, l3, j1, 100F, 100F);
                    worldObj.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    ((BlockCrops)Block.crops).fertilize(worldObj, i, l3, j1);
                    //System.out.println("Lucky! A wheat Growing! XYZ: (" + i + ", " + l3 + ", " + j1 + ")");
            	}
            }


            if (l2 == Block.crops.blockID && l4 == 7)
            {
                if (inventory.addItemStackToInventory(new ItemStack(Item.wheat)))
                {
                    GrowPlants = true;
                    facetoPath(i, l3, j1, 100F, 100F);
                    worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    worldObj.setBlockWithNotify(i, l3, j1, 0);

                    if (getchanceItems(Item.seeds, 3, 0))
                    {
                    }
                    pathToCrop = null;
                    //setPathToEntity(pathToCrop);
                    //WheatAmount++;
                    //WAmounts = Byte.toString(WheatAmount);
                    //System.out.println(WheatAmount + "Wheats");
                }
            }


            if (l2 == Block.melon.blockID)
            {
                if (getchanceItems(Item.melon, 5, 3))
                {
                    GrowPlants = true;
                    facetoPath(i, l3, j1, 100F, 100F);
                    worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    worldObj.setBlockWithNotify(i, l3, j1, 0);
                    pathToCrop = null;
                    //setPathToEntity(pathToCrop);
                    //MelonAmount++;
                    //MAmounts = Byte.toString(MelonAmount);
                    //System.out.println(MelonAmount +"Melons");
                }
            }

            if (l2 == Block.pumpkin.blockID)
            {
                if (inventory.addItemStackToInventory(new ItemStack(Block.pumpkin)))
                {
                    GrowPlants = true;
                    facetoPath(i, l3, j1, 100F, 100F);
                    worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    worldObj.setBlockWithNotify(i, l3, j1, 0);
                    pathToCrop = null;
                    //setPathToEntity(pathToCrop);
                    //PumpAmount++;
                    //PAmounts = Byte.toString(PumpAmount);
                    //System.out.println(PumpAmount + "Pumps");
                }
            }
            else if(l2 > 0 && !GrowPlants)
            {
                Block crops = Block.blocksList[l2];
                if(crops instanceof BlockCrops && crops.idDropped(l4, rand, 0) > 0)
                {
                    if (inventory.addItemStackToInventory(new ItemStack(crops.idDropped(l4, rand, 0), 1, 0)))
                    {
                    	crops.dropBlockAsItemWithChance(worldObj, i, l3, j1, 7, 1.0F, 0);
                    	worldObj.setBlockWithNotify(i, l3, j1, 0);
                        GrowPlants = true;
                        facetoPath(i, l3, j1, 100F, 100F);
                        worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        pathToCrop = null;
                    }
                }else
                try
                {
                	if(mod_DenEnderman.ToggleXies)
                	{
                    	if(mod_XieClient.class.getClass() != null)
                    	{
                        	if(crops instanceof net.minecraft.src.xie.blocks.XieBlockPlant)
                        	{
                        		net.minecraft.src.xie.blocks.XieBlockPlant xiecrops = (net.minecraft.src.xie.blocks.XieBlockPlant)crops;
                    			if(ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops") != null)
                    			{
                    				int metaxie = worldObj.getBlockMetadata(i, l3, j1);
                    				if(metaxie == xiecrops.maxGrowth)
                    				{
                        				net.minecraft.src.xie.Droppables  drops = (net.minecraft.src.xie.Droppables)ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops");
                        				net.minecraft.src.xie.Xie.spawnItems(drops.getDropsForMetaAndMultiplier(metaxie, 0), worldObj, i, l3, j1);
                                    	worldObj.setBlockWithNotify(i, l3, j1, 0);
                                        GrowPlants = true;
                                        facetoPath(i, l3, j1, 100F, 100F);
                                        worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                                        pathToCrop = null;
                    				}
                    			}
                        	}
                    	}
                	}
                }catch(Exception e)
                {
                }
            }

            int l14 = MathHelper.floor_double(posY);
            int l13 = worldObj.getBlockId(i, l14 - 1, j1);
            int l15 = worldObj.getBlockId(i, l14, j1);

            if (rand.nextInt(2000) == 0)
            {
                if (l13 == Block.grass.blockID && l15 == 0)
                {
                    if (inventory.consumeInventoryItem(mod_DenEnderman.FarmerSeeds.shiftedIndex))
                    {
                    	facetoPath(i, l14, j1, 100F, 100F);
                        StepSound stepsound1 = Block.plantRed.stepSound;
                        worldObj.playSoundAtEntity(this, stepsound1.getStepSound(), stepsound1.getPitch(), stepsound1.getPitch());
                        worldObj.setBlockWithNotify(i, l14, j1, mod_DenEnderman.Lavender.blockID);
                        //System.out.println("Lucky! DenEnderman planted a lavender! XYZ: (" + i + ", " + l14 + ", " + j1 + ")");
                    }
                }
            }
        }
    }
    */
}
