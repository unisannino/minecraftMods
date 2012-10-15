package unisannino.denenderman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.*;

public class EntityTreeper extends EntityFarmers
{
	public int timeSinceIgnited;
	public int lastActiveTime;

	private int widthX;
	private int widthZ;

	private int CutExplosion;
	private int CutTime;
	private int leavetype;
	private List<Integer> checklistX;
	private List<Integer> checklistZ;
	private List<ChunkPosition> checkposition;

	public EntityTreeper(World world)
	{
		super(world);
		texture = "/denender/treeper.png";
		setSize(0.8F, 2.8F);
		checklistX = new ArrayList<Integer>();
		checklistZ = new ArrayList<Integer>();
		checkposition = new ArrayList<ChunkPosition>();
		widthX = 0;
		widthZ = 0;
		favoriteItem = Mod_DenEnderman_Core.Lavender.blockID;
		likeItem = Item.appleRed.shiftedIndex;
		setcanPickup(0, Block.wood.blockID);
		setcanPickup(1, Block.sapling.blockID);
		setcanPickup(2, Item.appleRed.shiftedIndex);
		setcanPickup(3, Block.leaves.blockID);
		setcanPickup(4, Mod_DenEnderman_Core.Lavender.blockID);
		setcanPickup(5, Item.shears.shiftedIndex);

		setLeavesType();
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

	public int getMaxHealth()
	{
		return 13;
	}

	protected int getLeavesType()
	{
		return leavetype;
	}

	private void setLeavesType()
	{
		leavetype = rand.nextInt(4);
	}

	@Override
	public float getEyeHeight()
	{
		return 2.0F;
	}

	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) - 1));
	}

	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (!worldObj.isRemote && this.health > 0)
		{
			destroyBlocksInAABB(this.boundingBox.expand(0.5D, 1.0D, 0.5D));
			if(inventory.getFirstEmptyStack() > -1)
			{
				Cutting();
			}
			Planting();
		}
		/*
		Class c = this.getClass();
		String s = c.getCanonicalName();
		System.out.println(s);
		*/
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

	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("leaves", leavetype);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readEntityFromNBT(nbttagcompound);
		leavetype = nbttagcompound.getInteger("leaves");
	}

	protected String getHurtSound()
	{
		return "mob.creeper";
	}

	protected String getDeathSound()
	{
		return "mob.creeperdeath";
	}

    @Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        GrowTree();
    }

	private void GrowTree()
	{
		if (worldObj.isRemote)
		{
			return;
		}
		worldObj.setBlockAndMetadataWithNotify((int)posX, (int)posY, (int)posZ, Block.sapling.blockID, leavetype);
		worldObj.createExplosion(this, posX, posY, posZ, 0F);
		((BlockSapling)Block.sapling).growTree(worldObj, (int)posX, (int)posY, (int)posZ, worldObj.rand);
	}


	public float setCreeperFlashTime(float f)
	{
		return ((float)lastActiveTime + (float)(timeSinceIgnited - lastActiveTime) * f) / 1F;
	}

	private int getCreeperState()
	{
		return dataWatcher.getWatchableObjectByte(16);
	}

	private void setCreeperState(int i)
	{
		dataWatcher.updateObject(16, Byte.valueOf((byte)i));
	}


	protected int getDropItemId()
	{
		return Mod_DenEnderman_Core.TreeperSeed.shiftedIndex;
	}

	@Override
	public EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
	{
		return null;
	}

	private void Planting()
	{
		for (double checkXZ = 10; checkXZ > 0; checkXZ--)
		{
			for (int height = 4; height > -1; height--)
			{
				int i = MathHelper.floor_double((posX - (rand.nextDouble() * 9D)) + checkXZ);
				int j = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * height);
				int k = MathHelper.floor_double((posZ - (rand.nextDouble() * 9D)) + checkXZ);

				if (CanPlanting(worldObj, i, j, k) && !hasPathCrops())
				{
					pathToCrop = worldObj.getEntityPathToXYZ(this, i, j, k, 16F, true, false, false, true);
					setPathToEntity(pathToCrop);
				}

			}
		}

		for (double checkXZ = 4; checkXZ > 0; checkXZ--)
		{
			{
				int i = MathHelper.floor_double((posX - (rand.nextDouble() * 3D)) + checkXZ);
				int j = MathHelper.floor_double(posY);
				int k = MathHelper.floor_double((posZ - (rand.nextDouble() * 3D)) + checkXZ);

				for (int height = 3; height > - 2; height--)
				{
					//System.out.println("Now Y: " + (j + height) + "and Can Planting:" + CanPlanting(worldObj, i, j + height, k));
					if (CanPlanting(worldObj, i, j + height, k))
					{
						ItemStack itemstack = null;
						Object sapling = null;
						int meta = 0;
				        for (int length = 0; length < inventory.mainInventory.length; length++)
				        {
				            if (inventory.mainInventory[length] != null)
				            {
				            	sapling = inventory.getInventoryObject(length);
								if(sapling instanceof BlockSapling)
								{
									meta = inventory.getInventoryMetadata(length);
									itemstack = new ItemStack((Block)sapling, 1, meta);
									break;
								}
				            }
				        }
				        if(itemstack != null)
				        {
							if (inventory.consumeInventoryItem(itemstack.itemID, itemstack.getItemDamage()))
							{
								worldObj.setBlockAndMetadataWithNotify(i, j + height + 1, k, itemstack.itemID, itemstack.getItemDamage());
								StepSound stepsound = Block.sapling.stepSound;
								worldObj.playSoundAtEntity(this, stepsound.getStepSound(), stepsound.getPitch(), stepsound.getPitch());
								pathToCrop = null;
								//setPathToEntity(pathToCrop);
								break;
							}
				        }
					}
					/*
					ChunkPosition posi = new ChunkPosition(i, j + height + 1, k);
					if(!getPosChecked(posi))
					{
						int l = worldObj.getBlockId(i, j + height + 1, k);
						if(l == Block.sapling.blockID && inventory.consumeInventoryItem(Item.dyePowder.shiftedIndex, 15))
						{
							setPosChecked(posi);
			                worldObj.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			                ((BlockSapling)Block.sapling).growTree(worldObj, i, j + height + 1, k, rand);
						}
					}
					*/
				}
			}
		}
	}

	private boolean CanPlanting(World world, int i, int j, int k)
	{

		int grow1 = world.getBlockId(i + 1, j, k);
		int grow2 = world.getBlockId(i - 1, j, k);
		int grow3 = world.getBlockId(i, j, k - 1);
		int grow4 = world.getBlockId(i, j, k + 1);
		int growmain = world.getBlockId(i, j, k);
		int alreadyplanted = world.getBlockId(i, j + 1, k);

		if (alreadyplanted == 0
				&& grow1 == Block.glowStone.blockID
				&& grow2 == Block.glowStone.blockID
				&& grow3 == Block.glowStone.blockID
				&& grow4 == Block.glowStone.blockID
				&& (growmain == Block.grass.blockID || growmain == Block.dirt.blockID))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

    private void destroyBlocksInAABB(AxisAlignedBB par1AxisAlignedBB)
    {
        int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int k = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int l = MathHelper.floor_double(par1AxisAlignedBB.maxX);
        int i1 = MathHelper.floor_double(par1AxisAlignedBB.maxY);
        int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ);

        for (int k1 = i; k1 <= l; k1++)
        {
            for (int l1 = j; l1 <= i1; l1++)
            {
                for (int i2 = k; i2 <= j1; i2++)
                {
                    int j2 = worldObj.getBlockId(k1, l1, i2);
                    int m = worldObj.getBlockMetadata(k1, l1, i2);

                    if (j2 == 0)
                    {
                        continue;
                    }

                    if (j2 == Block.leaves.blockID)
                    {
        				StepSound stepsound = Block.leaves.stepSound;
        				worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());

                    	if(inventory.getInventorySlotContainItem(Item.shears.shiftedIndex) > -1 && inventory.addItemStackToInventory(new ItemStack(Block.leaves, 1, m & 3)))
                    	{
                    		inventory.mainInventory[inventory.getInventorySlotContainItem(Item.shears.shiftedIndex)].damageItem(1, this);
                    	}

                    	ItemStack itemstack = new ItemStack(Block.sapling, 1, m & 3);

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

                    	mc.effectRenderer.addBlockDestroyEffects(k1, l1, i2, Block.leaves.blockID & 0xfff, Block.leaves.blockID >> 12 & 0xff);
                    	worldObj.setBlockWithNotify(k1, l1, i2, 0);
                    }
                }
            }
        }
    }

	private void Cutting()
	{
		for (double checkXZ = 10; checkXZ > 0; checkXZ--)
		{
			for (int height = 4; height > -1; height--)
			{

				int i = MathHelper.floor_double((posX - (rand.nextDouble() * 9D)) + checkXZ);
				int j = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * height);
				int k = MathHelper.floor_double((posZ - (rand.nextDouble() * 9D)) + checkXZ);

				if(CheckLogs(worldObj, i, j, k) && UnderWoodBlock(worldObj, i, j, k) && !hasPathCrops())
				{
					//System.out.println("getpathcut");
					pathToCrop = worldObj.getEntityPathToXYZ(this, i, j, k, 16F, true, false, false, true);
					setPathToEntity(pathToCrop);
					//worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);

				}

				for (int woodheight = 3; woodheight > -2; woodheight--)
				{
					//System.out.println("Now Y: " +  (j + height));
					int x = MathHelper.floor_double((posX - 1.0D) + rand.nextDouble() * 3D);
					int y = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * woodheight);
					int z = MathHelper.floor_double((posZ - 1.0D) + rand.nextDouble() * 3D);
					int l1 = worldObj.getBlockId(x, y, z);

					if (l1 == Block.wood.blockID)
					{
						if (UnderWoodBlock(worldObj, x, y, z) && CheckLogs(worldObj, x, y, z))
						{
							int meta = worldObj.getBlockMetadata(x, y, z);
							ChunkPosition posi = new ChunkPosition(x, y, z);
							checkposition.add(posi);
							facetoPath(x, y, z, 100F, 100F);
							widthX = 1;
							widthZ = 1;
							for (int y1 = 0; worldObj.getBlockId(x, y + y1, z) != 0; y1++)
							{
								ChunkPosition posi2 = new ChunkPosition(x, y + y1, z);
								checkposition.add(posi2);
								int meta1 = worldObj.getBlockMetadata(x, y + y1, z);
								//if(checkConnecting(worldObj, x, y + y1, z, meta))
								{
									//labelLoopX:
									for (int x1 = -widthX; x1 <= widthX; x1++)
									{
										//labelLoopZ:
										for (int z1 = -widthZ; z1 <= widthZ; z1++)
										{
											/*
											System.out.println(widthX +"B.widthX");
											System.out.println(widthZ + "B.witdhZ");
											*/
											/*
											int prevx1 = x1;
											int prevz1 = z1;
											*/

											int meta2 = worldObj.getBlockMetadata(x + x1, y + y1, z + z1);
											if(checkConnecting(worldObj, x + x1, y + y1, z + z1, meta))
											{
												//continue;
											}

											//System.out.println("checkconnect: " +checkConnecting(worldObj, x + x1, y + y1, z + z1, meta));
												//System.out.println("x"+x1+"y"+y1+"z"+z1);
											/*
												System.out.println(widthX +"widthX");
												System.out.println(widthZ + "witdhZ");
												*/

												/*
												if(prevwidthX < widthX && widthX == 1 && widthZ < 2)
												{
													x1 = -widthX;
													System.out.println("loopX");
													continue labelLoopX;
												}
												*/
												/*
												if(prevwidthZ < widthZ)
												{
													z1 = -widthZ;
													System.out.println("loopZ");
													continue labelLoopZ;
												}
												*/

												if(widthX > 15 || widthZ > 15)
												{
													//throw new StackOverflowError("over!");
												}

											//System.out.println(meta + " " + meta2);
												if(meta2 == meta && CheckLogs(worldObj, x + x1, y + y1, z + z1) && inventory.addItemStackToInventory(new ItemStack(Block.wood, 1, meta)))
												{
													//worldObj.playSoundAtEntity(this, "random.pop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
													worldObj.setBlockWithNotify(x + x1, y + y1, z + z1, 0);
													/*
													if(worldObj.setBlockWithNotify(x + x1, y + y1, z + z1, 0))
													{
														ItemStack itemstack = new ItemStack(Block.wood, 1, meta);
														spawnItems(x + x1, y, z + z1, itemstack);
													}
													*/
												}
												/*else
													if(meta1 == meta && CheckLogs(worldObj, x, y + y1, z) && inventory.addItemStackToInventory(new ItemStack(Block.wood, 1, meta)))
													{
														//worldObj.playSoundAtEntity(this, "random.pop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
														worldObj.setBlockWithNotify(x, y + y1, z, 0);
														/*
														if(worldObj.setBlockWithNotify(x, y + y1, z, 0))
														{
															ItemStack itemstack = new ItemStack(Block.wood, 1, meta);
															spawnItems(x, y, z, itemstack);
														}

													}
													*/
											}
										}
								}
							}
							worldObj.createExplosion(this, posX, posY, posZ, 0F);
							pathToCrop = null;
							checkposition.clear();
						}
						//System.out.println("Under No");
					}
				}
			}
		}
	}

	private void spawnItems(int i, int j, int k, ItemStack itemstack)
	{
		float f = 0.7F;
		double d = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
		double d1 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
		double d2 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
		EntityItem entityitem = new EntityItem(worldObj, (double)i + d, (double)j + d1, (double)k + d2, itemstack);
		entityitem.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(entityitem);
	}

	private boolean UnderWoodBlock(World world, int i, int j, int k)
	{
		int checkroot = world.getBlockId(i, j - 1, k);

		if (checkroot == Block.dirt.blockID
				|| checkroot == Block.grass.blockID)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private boolean CheckLogs(World world, int i, int j, int k)
	{
		int logs1 = world.getBlockId(i, j, k);
		int logs2 = world.getBlockId(i, j + 1, k);
		//System.out.println(logs1);
		if (logs1 == Block.wood.blockID)
		{
			return true;
		}
		/*
		else
		if (logs2 != 0)
		{
			return true;
		}*/
		{
			return false;
		}

	}

	private boolean CheckLeaves(World world, int i, int j, int k)
	{
		int logs1 = world.getBlockId(i, j, k);
		int logs2 = world.getBlockId(i, j + 1, k);

		if (logs1 == Block.leaves.blockID)
		{
			return true;
		}
		else
		if (logs2 != 0)
		{
			return true;
		}
		{
			return false;
		}
	}

	/*
	private void setXchecked(int i)
	{
		checklistX.add(i);
	}

	private void setZchecked(int i)
	{
		checklistZ.add(i);
	}

	private void setXZchecked(int i, int j)
	{
		checklistX.add(i);
		checklistZ.add(j);
	}
	*/

	private void setPosChecked(ChunkPosition chunk)
	{
		checkposition.add(chunk);
	}

	private boolean getPosChecked(ChunkPosition chunk)
	{
		if(checkposition.size() > 0)
		{
			for(int i = 0; i < checkposition.size(); i++)
			{
				//System.out.println(checklistX.get(x)+"and"+i);
				ChunkPosition listc = checkposition.get(i);
				if(listc.equals(chunk))
				{
					return false;
				}
			}
		}
		return true;
	}

	/*
	private boolean getXZchecked(int i, int j)
	{
		if(checklistX.size() > 0)
		{
			for(int x = 0; x < checklistX.size(); x++)
			{
				//System.out.println(checklistX.get(x)+"and"+i);
				if(checklistX.get(x) == i)
				{
					return false;
				}
			}
		}
		if(checklistZ.size() > 0)
		{
			for(int z = 0; z < checklistZ.size(); z++)
			{
				//System.out.println(checklistZ.get(z)+"and"+j);
				if(checklistZ.get(z) == j)
				{
					return false;
				}
			}
		}
		return true;
	}
	*/

	private boolean checkConnecting(World world, int i, int j, int k, int m)
	{
		boolean isplusX = false;
		boolean isplusZ = false;

		label0:
		for(int x = -1; x <= 1; x++)
		{
			for(int z = -1; z <= 1; z++)
			{
				int l = world.getBlockId(i + x, j, k + z);
				int m1 = world.getBlockMetadata(i + x, j, k + z);
				ChunkPosition posi  = new ChunkPosition(i + x, j, k + z);
				if(//!getXZchecked(i, k) ||
						!getPosChecked(posi))
				{
					int id = world.getBlockId(posi.x, posi.y, posi.z);
					//String name = Block.blocksList[id].getBlockName();
					String s = new StringBuilder("checked ").append(posi.x).append(" ").append(posi.y).append(" ").append(posi.z).append(" ").toString();
					//System.out.println(s +id);
					return false;
				}
				if( (l == Block.wood.blockID || l == Block.leaves.blockID) && m1 == m)
				{
					if(x == 0 && Math.abs(z) > 0)
					{
						//setZchecked(k);
						if(!isplusZ)
						{
							if(widthZ < 10)
							{
								widthZ++;
							}
							isplusZ = true;
						}
						setPosChecked(posi);
						continue label0;
					}
					if(Math.abs(x) > 0 && z == 0)
					{
						//setXchecked(i);
						if(!isplusX)
						{
							if(widthX < 10)
							{
								widthX++;
							}
							isplusX = true;
						}
						setPosChecked(posi);
						continue label0;
					}
					if(Math.abs(x) > 0 && Math.abs(z) > 0)
					{
						//setXZchecked(i, k);
						if(!isplusX)
						{
							if(widthX < 10)
							{
								widthX++;
							}
							isplusX = true;
						}
						if(!isplusZ)
						{
							if(widthZ < 10)
							{
								widthZ++;
							}
							isplusZ = true;
						}
						setPosChecked(posi);
						continue label0;
					}
					return true;
				}
			}
		}
		return false;
		/*
		int ipkp = world.getBlockId(i + 1, j, k + 1);
		int imkp = world.getBlockId(i - 1, j, k + 1);
		int ipkm = world.getBlockId(i + 1, j, k - 1);
		int imkm = world.getBlockId(i - 1, j, k - 1);

		int ip = world.getBlockId(i + 1, j, k);
		int im = world.getBlockId(i - 1, j, k);
		int km = world.getBlockId(i, j, k - 1);
		int kp = world.getBlockId(i, j, k + 1);

		int vine = Block.vine.blockID;

		if(ipkp == Block.wood.blockID || ipkp == Block.leaves.blockID
				|| imkp == Block.wood.blockID || imkp == Block.leaves.blockID
				|| ipkm == Block.wood.blockID || ipkm == Block.leaves.blockID
				|| imkm == Block.wood.blockID || imkm == Block.leaves.blockID)
		{
			widthX++;
			widthZ++;
			//System.out.println("one");
			return true;
		}else
		if(ip == Block.wood.blockID || ip == Block.leaves.blockID
				|| im == Block.wood.blockID || im == Block.leaves.blockID)
		{
			widthX++;
			//System.out.println("two");
			return true;
		}else
		if(km == Block.wood.blockID || km == Block.leaves.blockID
				|| kp == Block.wood.blockID || kp == Block.leaves.blockID)
		{
			widthZ++;
			//System.out.println("three");
			return true;
		}
		return false;
		*/
	}

	/*
	private boolean CheckLeavesAround(World world, int i, int j, int k)
	{
		int leaves1 = world.getBlockId(i + 1, j, k + 1);
		int leaves2 = world.getBlockId(i - 1, j, k + 1);
		int leaves3 = world.getBlockId(i + 1, j, k - 1);
		int leaves4 = world.getBlockId(i - 1, j, k - 1);

		int leaves5 = world.getBlockId(i + 1, j, k);
		int leaves6 = world.getBlockId(i - 1, j, k);
		int leaves7 = world.getBlockId(i, j, k - 1);
		int leaves8 = world.getBlockId(i, j, k + 1);

		if(leaves1 == Block.leaves.blockID)
		{
			LogX = 1;
			LogZ = 1;
			return true;
		}
		if(leaves2 == Block.leaves.blockID)
		{
			LogX = -1;
			LogZ = 1;
			return true;
		}
		if(leaves3 == Block.leaves.blockID)
		{
			LogX = 1;
			LogZ = -1;
			return true;
		}
		if(leaves4 == Block.leaves.blockID)
		{
			LogX = -1;
			LogZ = -1;
			return true;
		}
		if(leaves5 == Block.leaves.blockID)
		{
			LogX = 1;
			LogZ = 0;
			return true;
		}
		if(leaves6 == Block.leaves.blockID)
		{
			LogX = -1;
			LogZ = 0;
			return true;
		}
		if(leaves7 == Block.leaves.blockID)
		{
			LogX = 0;
			LogZ = -1;
			return true;
		}
		if(leaves8 == Block.leaves.blockID)
		{
			LogX = 0;
			LogZ = 1;
			return true;
		}
		return false;
	}
	*/
}
