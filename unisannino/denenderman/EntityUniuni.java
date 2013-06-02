package unisannino.denenderman;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherStalk;
import net.minecraft.block.BlockReed;
import net.minecraft.block.StepSound;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTwardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityUniuni extends EntityFarmers
{
    private static int[] eatableIBlocks = {Block.tallGrass.blockID, Block.vine.blockID, Block.crops.blockID, Block.leaves.blockID, Block.melon.blockID};

    public EntityUniuni(World world)
    {
        super(world);
        texture = "/mods/denender/textures/mobs/uniuni.png";
        setSize(0.8F, 0.6F);
        yOffset = 0.16F;
		favoriteItem = Mod_DenEnderman_Core.lavender.blockID;
		likeItem = Item.melon.itemID;

        this.getNavigator().setBreakDoors(true);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(0, new EntityAIUniHarvestableAndEatable(this));
        this.tasks.addTask(0, new EntityAIPutDEBlock(this));
        this.tasks.addTask(1, new EntityAITempt(this, this.moveSpeed, this.likeItem, false));
        this.tasks.addTask(1, this.aiSit);
        this.tasks.addTask(2, new EntityAINearestTargetItems(this, this.moveSpeed));
        this.tasks.addTask(2, new EntityAIMoveUniHervestableAndEatable(this, this.moveSpeed));
        this.tasks.addTask(2, new EntityAIMoveDEBlock(this, this.moveSpeed));
        this.tasks.addTask(3, new EntityAIRestrictOpenGate(this));
        this.tasks.addTask(4, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
        this.tasks.addTask(6, new EntityAIWander(this, this.moveSpeed));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        //this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityDenEnderman.class, 16.0F, 0, true));
    }

    @Override
    protected void entityInit()
	{
    	super.entityInit();
		this.dataWatcher.addObject(20, Byte.valueOf((byte) 0)); //eatingMelons
	}

    public void setEatingMelons(boolean b)
	{
		this.dataWatcher.updateObject(20, Byte.valueOf((byte) (b ? 1 : 0)));
	}

    public boolean getEatingMelons()
	{
    	return this.dataWatcher.getWatchableObjectByte(20) == 1;
	}

    @Override
    protected void addConfigPickupItems()
    {
    }

    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public int getMaxHealth()
    {
        return 10;
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if (!worldObj.isRemote)
        {
        	if(isInWater() || handleLavaMovement())
        	{
        		motionY += 0.02D;
        	}
        	if(this.getAttackTarget() instanceof EntityDenEnderman)
        	{
        		System.out.println("denender");
        		heartpop++;
        		/*
        		if(rand.nextInt(100) == 0)
        		{
        			this.setAttackTarget(null);
        		}
        		*/
        	}

            /*
            eating(worldObj);
            hervestSugercane(worldObj);
            hervestNether(worldObj);
            */

            if (this.health > 0 && rand.nextInt(10) == 0)
            {
                healing();
            }

            for (int j = 0; j < 4; j++)
            {
                int l11 = MathHelper.floor_double(posX + (((j % 2) * 2 - 1) * 0.25F));
                int j11 = MathHelper.floor_double(posY);
                int k1 = MathHelper.floor_double(posZ + ((((j / 2) % 2) * 2 - 1) * 0.25F));

                if (worldObj.getBlockId(l11, j11, k1) == 0 && Mod_DenEnderman_Core.starSand.canPlaceBlockAt(worldObj, l11, j11, k1))
                {
                    worldObj.setBlock(l11, j11, k1, Mod_DenEnderman_Core.starSandID);
                }
            }
        }
    }

    private void healing()
    {
        if (this.health < this.getMaxHealth() - 1)
        {
            if (inventory.consumeInventoryItem(Block.tallGrass.blockID)
                    || inventory.consumeInventoryItem(Block.vine.blockID)
                    || inventory.consumeInventoryItem(Item.wheat.itemID)
                    || inventory.consumeInventoryItem(Block.leaves.blockID)
                    || inventory.consumeInventoryItem(Item.melon.itemID))
            {
                heal(1);
                return;
            }
            else if (inventory.consumeInventoryItem(Block.melon.blockID))
            {
                heal(this.getMaxHealth());
                return;
            }
        }

        return;
    }

    @Override
    protected int getDropItemId()
    {
        return Mod_DenEnderman_Core.uniuniSoul.itemID;
    }

    /*
    protected void updateEntityActionState()
    {
        super.updateEntityActionState();

        if (entityToAttack == null && !hasPath() && worldObj.rand.nextInt(100) == 0)
        {
            List list = worldObj.getEntitiesWithinAABB(EntityDenEnderman.class, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(8D, 4D, 8D));

            if (!list.isEmpty())
            {
                setTarget((Entity)list.get(worldObj.rand.nextInt(list.size())));
            }
        }
    }
    */

    @Override
	protected String getLivingSound()
    {
        return "mob.silverfish.say";
    }

    @Override
	protected String getHurtSound()
    {
        return "mob.silverfish.hit";
    }

    @Override
	protected String getDeathSound()
    {
        return "mob.silverfish.kill";
    }

    @Override
    protected void fall(float f)
    {
    }

    @Override
    public double getYOffset()
    {
        if ((ridingEntity instanceof EntityPlayer) && !worldObj.isRemote)
        {
            return (yOffset - 1.15F);
        }
        else
        {
            return yOffset;
        }
    }

    @Override
    public boolean interact(EntityPlayer entityplayer)
    {
        super.interact(entityplayer);
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if(itemstack != null)
        {
            if (this.isBreedingItem(itemstack) && getGrowingAge() == 0)
            {
                itemstack.stackSize--;

                if (itemstack.stackSize <= 0)
                {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                }

                heartpop = 600;
                heal(1);

                for (int i = 0; i < 7; i++)
                {
                    double d = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;
                    double d2 = rand.nextGaussian() * 0.02D;
                    worldObj.spawnParticle("heart", (posX + (rand.nextFloat() * width * 2.0F)) - width, posY + 0.5D + (rand.nextFloat() * height), (posZ + (rand.nextFloat() * width * 2.0F)) - width, d, d1, d2);
                }
            }

            if (itemstack.itemID == Block.melon.blockID)
            {
                itemstack.stackSize--;

                if (itemstack.stackSize <= 0)
                {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                }

                if (!worldObj.isRemote && (ridingEntity == null || ridingEntity == entityplayer))
                {
                    this.mountEntity(entityplayer);
                }
            }
        }

        return true;
    }

    @Deprecated
    private void eating(World world)
    {
        if (rand.nextInt(20) == 0 || this.getHealth() < this.getMaxHealth())
        {
            for (double checkXZ = 10; checkXZ > 0; checkXZ--)
            {
                for (double checkheight = 3; checkheight > 0; checkheight--)
                {
                    int i1 = MathHelper.floor_double((posX - (rand.nextDouble() * 9D)) + checkXZ);
                    int l = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * checkheight);
                    int j1 = MathHelper.floor_double((posZ - (rand.nextDouble() * 9D)) + checkXZ);

                    if (canEatBlocks(world, i1, l, j1) && !hasPathCrops())
                    {
                    	/*
                        pathToCrop = world.getEntityPathToXYZ(this, i1, l, j1, 16F, true, false, false, true);
                        setPathToEntity(pathToCrop);
                        */
                    }
                    int x = MathHelper.floor_double((posX - 1.0D) + rand.nextDouble() * 2D);
                    int y = MathHelper.floor_double(posY + rand.nextDouble() * 2D);
                    int z = MathHelper.floor_double((posZ - 1.0D) + rand.nextDouble() * 2D);
                    int r = world.getBlockId(x, y, z);

                    if (canEatBlocks(world, x, y, z))
                    {
                        StepSound stepsound = Block.grass.stepSound;

                        if (this.getEatingMelons())
                        {
                            stepsound = Block.melon.stepSound;
                        }

                        world.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
                        world.setBlock(x, y, z, 0);
                        ItemStack itemstack = new ItemStack(Item.seeds, 1);

                        if (this.getEatingMelons())
                        {
                            itemstack = new ItemStack(Item.melonSeeds, 1);
                        }

                        EntityItem entityitem = new EntityItem(worldObj, posX, (posY - 0.3D), posZ, itemstack);
                        entityitem.delayBeforeCanPickup = 10;
                        worldObj.spawnEntityInWorld(entityitem);

                        if (!this.getEatingMelons())
                        {
                            heal(1);
                        }
                        else
                        {
                            heal(this.getMaxHealth());
                        }

                        this.setEatingMelons(false);
                        //pathToCrop = null;
                    }
                }
            }
        }
    }

    protected boolean canEatBlocks(World world, int x, int y, int z)
    {
        int target = worldObj.getBlockId(x, y, z);

        for(int i =0; i < EntityUniuni.eatableIBlocks.length;i++)
        {
        	int eatable = EntityUniuni.eatableIBlocks[i];
            if (target == eatable)
            {
            	return true;
            }
        }
        return false;
    }

    @Deprecated
    private void hervestSugercane(World world)
    {
        for (double checkXZ = 10; checkXZ > 0; checkXZ--)
        {
            for (double checkheight = 3; checkheight > -1; checkheight--)
            {
                int i = MathHelper.floor_double((posX - (rand.nextDouble() * 9D)) + checkXZ);
                int l = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * checkheight);
                int j1 = MathHelper.floor_double((posZ - (rand.nextDouble() * 9D)) + checkXZ);
                int l1 = worldObj.getBlockId(i, l, j1);

                if (l1 == Block.reed.blockID && !hasPathCrops())
                {
                    int l2 = worldObj.getBlockId(i, l + 1, j1);
                    int l3 = worldObj.getBlockId(i, l + 2, j1);

                    if (l2 == Block.reed.blockID && l3 == Block.reed.blockID)
                    {
                    	/*
                        pathToCrop = world.getEntityPathToXYZ(this, i, l, j1, 16F, true, false, false, true);
                        setPathToEntity(pathToCrop);
                        */
                    }
                }
                int x = MathHelper.floor_double((posX - 1.0D) + rand.nextDouble() * 2D);
                int y = MathHelper.floor_double(posY);
                int z = MathHelper.floor_double((posZ - 1.0D) + rand.nextDouble() * 2D);
                int r = worldObj.getBlockId(x, y, z);
                int r1 = worldObj.getBlockId(x, y + 1, z);
                int r2 = worldObj.getBlockId(x, y + 2, z);

                /*
                if (r == Block.reed.blockID && r1 == Block.reed.blockID && r2 == Block.reed.blockID)
                {
                    StepSound stepsound = Block.reed.stepSound;
                    worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
                    world.setBlockWithNotify(x, y , z, 0);
                    world.setBlockWithNotify(x, y , z, Block.reed.blockID);
                    ItemStack itemstack = new ItemStack(Item.reed, 1);
                    EntityItem entityitem = new EntityItem(worldObj, posX, (posY - 0.3D), posZ, itemstack);
                    entityitem.delayBeforeCanPickup = 10;
                    worldObj.spawnEntityInWorld(entityitem);
                    pathToCrop = null;
                }
                */

                //else
                if(r > 0 && r1 > 0)
                {
                    Block crops = Block.blocksList[r];
                    if(crops instanceof BlockReed && r == r1)
                    {
                            StepSound stepsound = crops.stepSound;
                            worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
                            world.setBlock(x, y , z, 0);
                            world.setBlock(x, y , z, crops.blockID);
                            crops.dropBlockAsItemWithChance(worldObj, x, y, z, 0, 1.0F, 0);
                            //pathToCrop = null;
                    }
                }
            }
        }
    }

    @Deprecated
    private void hervestNether(World world)
    {
        for (double checkXZ = 10; checkXZ > 0; checkXZ--)
        {
            for (double checkheight = 3; checkheight > -1; checkheight--)
            {
                int i = MathHelper.floor_double((posX - (rand.nextDouble() * 9D)) + checkXZ);
                int l = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * checkheight);
                int j1 = MathHelper.floor_double((posZ - (rand.nextDouble() * 9D)) + checkXZ);
                int l1 = worldObj.getBlockId(i, l, j1);
                int l2 = worldObj.getBlockMetadata(i, l, j1);

                if (l1 == Block.netherStalk.blockID && l2 == 3 && !hasPathCrops())
                {
                	/*
                    pathToCrop = world.getEntityPathToXYZ(this, i, l, j1, 16F, true, false, false, true);
                    setPathToEntity(pathToCrop);
                    */
                }
                int x = MathHelper.floor_double((posX - 1.0D) + rand.nextDouble() * 2D);
                int y = MathHelper.floor_double(posY + rand.nextDouble() * 2D);
                int z = MathHelper.floor_double((posZ - 1.0D) + rand.nextDouble() * 2D);
                int r = worldObj.getBlockId(x, y, z);
                int r1 = worldObj.getBlockMetadata(x, y, z);

                /*
                if (r == Block.netherStalk.blockID && r1 == 3)
                {
                    StepSound stepsound = Block.netherStalk.stepSound;
                    worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
                    world.setBlockAndMetadataWithNotify(x, y, z, Block.netherStalk.blockID, 0);
                    ItemStack itemstack = new ItemStack(Item.netherStalkSeeds, 1);
                    EntityItem entityitem = new EntityItem(worldObj, posX, (posY - 0.3D), posZ, itemstack);
                    entityitem.delayBeforeCanPickup = 10;
                    worldObj.spawnEntityInWorld(entityitem);
                    pathToCrop = null;
                }
                */

                //else
                	if(r > 0)
                {
                    Block crops = Block.blocksList[r];
                    if(crops instanceof BlockNetherStalk && r1 == 3)
                    {
                            StepSound stepsound = crops.stepSound;
                            worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
                            world.setBlock(x, y, z, crops.blockID, 0, 3);
                            crops.dropBlockAsItemWithChance(worldObj, x, y, z, 3, 1.0F, 0);
                            //pathToCrop = null;
                    }
                }
            }
        }
    }
}
