package unisannino.denenderman;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSapling;
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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class EntityTreeper extends EntityFarmers
{
	public EntityTreeper(World world)
	{
		super(world);
		texture = "/mods/denender/textures/mobs/treeper.png";
		setSize(0.8F, 2.8F);
        moveSpeed = 0.28F;

		favoriteItem = Mod_DenEnderman_Core.lavender.blockID;
		likeItem = Item.appleRed.itemID;
		this.funcItem = Item.gunpowder.itemID;

		setcanPickup(0, Block.wood.blockID);
		setcanPickup(1, Block.sapling.blockID);
		setcanPickup(2, Item.appleRed.itemID);
		setcanPickup(3, Block.leaves.blockID);
		setcanPickup(4, Mod_DenEnderman_Core.lavender.blockID);
		setcanPickup(5, Item.shears.itemID);

        this.getNavigator().setBreakDoors(true);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(0, new EntityAICuttingAndPlanting(this));
        this.tasks.addTask(0, new EntityAIPutDEBlock(this));
        this.tasks.addTask(1, new EntityAITempt(this, this.moveSpeed, this.likeItem, false));
        this.tasks.addTask(1, this.aiSit);
        this.tasks.addTask(2, new EntityAINearestTargetItems(this, this.moveSpeed));
        this.tasks.addTask(2, new EntityAIMoveLogsAndPlantablePoint(this, this.moveSpeed));
        this.tasks.addTask(2, new EntityAIMoveIngates(this));
        this.tasks.addTask(2, new EntityAIMoveDEBlock(this, this.moveSpeed));
        this.tasks.addTask(3, new EntityAIRestrictOpenGate(this));
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
		dataWatcher.addObject(20, Integer.valueOf(rand.nextInt(4))); // leaveType
	}

	public int getLeaveType()
	{
		return this.dataWatcher.getWatchableObjectInt(20);
	}

	public void setLeaveType(int i)
	{
		this.dataWatcher.updateObject(20, Integer.valueOf(i));
	}


    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    @Override
    protected void addConfigPickupItems()
    {
    	String s[] = Mod_DenEnderman_Core.canPickupTR.split(",");
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
	public float getEyeHeight()
	{
		return 2.0F;
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (!worldObj.isRemote && this.isEntityAlive())
		{
			destroyBlocksInAABB(this.boundingBox.expand(0.5D, 1.0D, 0.5D));
			/*
			if(inventory.getFirstEmptyStack() > -1)
			{
				cutting();
			}
			planting();
			*/
		}
	}

    @Override
    protected boolean checkItemID(int i)
    {
    	if(i > 0)
    	{
			Item item = Item.itemsList[i];
			if(item instanceof ItemFood || item instanceof ItemSeeds)
			{
				return true;
			}else
			if(item instanceof ItemBlock)
			{
    			Block block = Block.blocksList[i];
    			if(block instanceof BlockFlower)
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
		nbttagcompound.setInteger("leaves", this.getLeaveType());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readEntityFromNBT(nbttagcompound);
		this.setLeaveType(nbttagcompound.getInteger("leaves"));
	}

	@Override
    protected String getHurtSound()
    {
        return "mob.creeper.say";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.creeper.death";
    }

    @Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        growTree();
    }

	private void growTree()
	{
		if (!worldObj.isRemote && ((BlockSapling)Block.sapling).canPlaceBlockAt(this.worldObj, (int)posX, (int)posY, (int)posZ))
		{
			worldObj.setBlock((int)posX, (int)posY, (int)posZ, Block.sapling.blockID, this.getLeaveType(), 3);
			worldObj.createExplosion(this, posX, posY, posZ, 0F, true);
			((BlockSapling)Block.sapling).growTree(worldObj, (int)posX, (int)posY, (int)posZ, worldObj.rand);
		}
	}

	@Override
	protected int getDropItemId()
	{
		return Mod_DenEnderman_Core.treeperSeed.itemID;
	}

    private void destroyBlocksInAABB(AxisAlignedBB par1AxisAlignedBB)
    {
        int minX = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int minY = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int minZ = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int maxX = MathHelper.floor_double(par1AxisAlignedBB.maxX);
        int maxY = MathHelper.floor_double(par1AxisAlignedBB.maxY);
        int maxZ = MathHelper.floor_double(par1AxisAlignedBB.maxZ);

        for (int x = minX; x <= maxX; x++)
        {
            for (int y = minY; y <= maxY; y++)
            {
                for (int z = minZ; z <= maxZ; z++)
                {
                    int id = worldObj.getBlockId(x, y, z);
                    int m = worldObj.getBlockMetadata(x, y, z);
                    Block block = Block.blocksList[id];
                    ItemStack item = new ItemStack(Item.shears);

                    if (id == 0)
                    {
                        continue;
                    }

                    if (id == Block.leaves.blockID || block.isLeaves(this.worldObj, x, y, z))
                    {
        				StepSound stepsound = Block.leaves.stepSound;
        				worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());

        				if(block instanceof IShearable)
        				{
        					IShearable target = (IShearable) block;
        					if(target.isShearable(item, this.worldObj, x, y, z))
        					{
        						if(target.onSheared(item, this.worldObj, x, y, z, 0) != null)
        						{
        							for(ItemStack sheared : target.onSheared(item, this.worldObj, x, y, z, 0))
        							{
        		                    	if(inventory.getInventorySlotContainItem(Item.shears.itemID) > -1 && inventory.addItemStackToInventory(sheared))
        		                    	{
        		                    		inventory.mainInventory[inventory.getInventorySlotContainItem(Item.shears.itemID)].damageItem(1, this);
        		                    	}
        							}
        						}
        					}
        				}

                    	ItemStack itemstack = new ItemStack(block.idDropped(m, this.getRNG(), 0), block.quantityDropped(this.getRNG()) ,block.damageDropped(m));

        				if (rand.nextInt(20) == 0 && inventory.addItemStackToInventory(itemstack))
        				{
        					worldObj.playSoundAtEntity(this, "random.pop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        					//spawnItems(x, y + dleave, z, itemstack);
        				}

        				if ((m & 3) == 0 && rand.nextInt(200) == 0 && inventory.addItemStackToInventory(new ItemStack(Item.appleRed)))
        				{
        					worldObj.playSoundAtEntity(this, "random.pop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        					//spawnItems(x, y + dleave, z, new ItemStack(Item.appleRed));
        				}

        				Mod_DenEnderman_Core.proxy.addBlockDestroyEffects(x, y, z);
                    	worldObj.setBlock(x, y, z, 0);
                    }
                }
            }
        }
    }

	private void spawnItems(int i, int j, int k, ItemStack itemstack)
	{
		float f = 0.7F;
		double d = (worldObj.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
		double d1 = (worldObj.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
		double d2 = (worldObj.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
		EntityItem entityitem = new EntityItem(worldObj, i + d, j + d1, k + d2, itemstack);
		entityitem.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(entityitem);
	}

	protected boolean canPlanting(World world, int i, int j, int k)
	{

		int grow1 = world.getBlockId(i + 1, j, k);
		int grow2 = world.getBlockId(i - 1, j, k);
		int grow3 = world.getBlockId(i, j, k - 1);
		int grow4 = world.getBlockId(i, j, k + 1);
		int growmain = world.getBlockId(i, j, k);

		return world.isAirBlock(i, j + 1, k)
				&& grow1 == Block.glowStone.blockID
				&& grow2 == Block.glowStone.blockID
				&& grow3 == Block.glowStone.blockID
				&& grow4 == Block.glowStone.blockID
				&& (growmain == Block.grass.blockID || growmain == Block.dirt.blockID);
	}

	protected boolean checkIsLogs(World world, int i, int j, int k)
	{
		int var1 = world.getBlockId(i, j, k);

		return var1 == Block.wood.blockID || (Block.blocksList[var1] != null && Block.blocksList[var1].isWood(world, i, j, k));

	}

	protected boolean underWoodBlock(World world, int i, int j, int k)
	{
		int checkroot = world.getBlockId(i, j - 1, k);

		return checkroot == Block.dirt.blockID || checkroot == Block.grass.blockID;
	}

	protected boolean checkIsLeaves(World world, int i, int j, int k)
	{
		int var1 = world.getBlockId(i, j, k);
		int var2 = world.getBlockId(i, j + 1, k);

		return var1 == Block.leaves.blockID || (Block.blocksList[var1] != null &&  Block.blocksList[var1].isLeaves(world, i, j, k));
	}

	protected boolean checkTopOfLogs(World world, int x, int y, int z)
	{
		for(int y1 = 0;world.getBlockId(x, y + y1, z) > 0;y1++)
		{
			int var1 = world.getBlockId(x, y + y1, z);

			if(Block.blocksList[var1] != null &&  Block.blocksList[var1].isLeaves(world, x, y + y1, z))
			{
				return true;
			}
		}

		return false;
	}
}
