package unisannino.denenderman;

import java.util.List;

import net.minecraft.client.Minecraft;

import net.minecraft.src.*;

public class EntityUniuni extends EntityFarmers
{
    private boolean eatingmelon;

    public EntityUniuni(World world)
    {
        super(world);
        texture = "/denender/uniuni.png";
        setSize(0.8F, 0.6F);
        yOffset = 0.16F;
		favoriteItem = Mod_DenEnderman_Core.Lavender.blockID;
		likeItem = Item.melon.shiftedIndex;
    }

    @Override
    protected void addConfigPickupItems()
    {
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
        	if(entityToAttack instanceof EntityDenEnderman)
        	{
        		heartpop++;
        		if(rand.nextInt(100) == 0)
        		{
        			entityToAttack = null;
        		}
        	}
            int i = MathHelper.floor_double((posX - 1.0D) + rand.nextDouble() * 2D);
            int l = MathHelper.floor_double(posY);
            int j1 = MathHelper.floor_double((posZ - 1.0D) + rand.nextDouble() * 2D);
            Eating(worldObj);
            HervestSugercane(worldObj);
            HervestNether(worldObj);

            if (this.health > 0 && rand.nextInt(10) == 0)
            {
                Healing();
            }

            for (int j = 0; j < 4; j++)
            {
                int l11 = MathHelper.floor_double(posX + (double)((float)((j % 2) * 2 - 1) * 0.25F));
                int j11 = MathHelper.floor_double(posY);
                int k1 = MathHelper.floor_double(posZ + (double)((float)(((j / 2) % 2) * 2 - 1) * 0.25F));

                if (worldObj.getBlockId(l11, j11, k1) == 0 && Mod_DenEnderman_Core.StarSand.canPlaceBlockAt(worldObj, l11, j11, k1))
                {
                    worldObj.setBlockWithNotify(l11, j11, k1, Mod_DenEnderman_Core.StarSandID);
                }
            }
        }
    }

    private void Healing()
    {
        if (this.health < this.getMaxHealth() - 1)
        {
            if (inventory.consumeInventoryItem(Block.tallGrass.blockID)
                    || inventory.consumeInventoryItem(Block.vine.blockID)
                    || inventory.consumeInventoryItem(Item.wheat.shiftedIndex)
                    || inventory.consumeInventoryItem(Block.leaves.blockID)
                    || inventory.consumeInventoryItem(Item.melon.shiftedIndex))
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
        return Mod_DenEnderman_Core.UniuniSoul.shiftedIndex;
    }

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
    /*
        public boolean attackEntityFrom(DamageSource damagesource, int i)
        {
            Entity entity = damagesource.getEntity();
            if(super.attackEntityFrom(damagesource, i))
            {
                if(entity != this && entity != null)
                {
                    if(entity instanceof EntityPlayer)
                    {
                        return true;
                    }
                    entityToAttack = entity;
                }
                return true;
            }else
            	return false;
        }
        */

    /*    public boolean getCanSpawnHere()
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(boundingBox.minY);
            int k = MathHelper.floor_double(posZ);
            return  worldObj.getBlockId(i, j - 1, k) == Block.grass.blockID  && worldObj.getFullBlockLightValue(i, j, k) > 8;
        }
    */

    protected String getLivingSound()
    {
        return "mob.silverfish.say";
    }

    protected String getHurtSound()
    {
        return "mob.silverfish.hit";
    }

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
            return (double)(yOffset - 1.15F);
        }
        else
        {
            return (double)yOffset;
        }
    }

    @Override
    public boolean interact(EntityPlayer entityplayer)
    {
        super.interact(entityplayer);
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (itemstack != null && isWheat(itemstack) && getGrowingAge() == 0)
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
                worldObj.spawnParticle("heart", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
        }

        if (itemstack != null && itemstack.itemID == Block.melon.blockID)
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

            //inventory.dropAllItems();
        }

        return true;
    }

    private void Eating(World world)
    {
        if (rand.nextInt(20) == 0 || this.health < this.getMaxHealth())
        {
            for (double checkXZ = 10; checkXZ > 0; checkXZ--)
            {
                for (double checkheight = 3; checkheight > 0; checkheight--)
                {
                    int i1 = MathHelper.floor_double((posX - (rand.nextDouble() * 9D)) + checkXZ);
                    int l = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * checkheight);
                    int j1 = MathHelper.floor_double((posZ - (rand.nextDouble() * 9D)) + checkXZ);

                    if (canEat(world, i1, l, j1) && !hasPathCrops())
                    {
                        pathToCrop = world.getEntityPathToXYZ(this, i1, l, j1, 16F, true, false, false, true);
                        setPathToEntity(pathToCrop);
                    }
                    int x = MathHelper.floor_double((posX - 1.0D) + rand.nextDouble() * 2D);
                    int y = MathHelper.floor_double(posY + rand.nextDouble() * 2D);
                    int z = MathHelper.floor_double((posZ - 1.0D) + rand.nextDouble() * 2D);
                    int r = world.getBlockId(x, y, z);

                    if (canEat(world, x, y, z))
                    {
                        StepSound stepsound = Block.grass.stepSound;

                        if (eatingmelon)
                        {
                            stepsound = Block.melon.stepSound;
                        }

                        world.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
                        world.setBlockWithNotify(x, y, z, 0);
                        ItemStack itemstack = new ItemStack(Item.seeds, 1);

                        if (eatingmelon)
                        {
                            itemstack = new ItemStack(Item.melonSeeds, 1);
                        }

                        EntityItem entityitem = new EntityItem(worldObj, posX, (posY - 0.3D), posZ, itemstack);
                        entityitem.delayBeforeCanPickup = 10;
                        worldObj.spawnEntityInWorld(entityitem);

                        if (!eatingmelon)
                        {
                            heal(1);
                        }
                        else
                        {
                            heal(this.getMaxHealth());
                        }

                        eatingmelon = false;
                        pathToCrop = null;
                    }
                }
            }
        }
    }

    private boolean canEat(World world, int i, int l, int j1)
    {
        int check = worldObj.getBlockId(i, l, j1);

        if (check == Block.tallGrass.blockID
                || check == Block.vine.blockID
                || check == Block.crops.blockID
                || check == Block.leaves.blockID)
        {
            return true;
        }

        if (check ==  Block.melon.blockID)
        {
            eatingmelon = true;
            return true;
        }

        return false;
    }

    private void HervestSugercane(World world)
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
                        pathToCrop = world.getEntityPathToXYZ(this, i, l, j1, 16F, true, false, false, true);
                        setPathToEntity(pathToCrop);
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
                            world.setBlockWithNotify(x, y , z, 0);
                            world.setBlockWithNotify(x, y , z, crops.blockID);
                            crops.dropBlockAsItemWithChance(worldObj, x, y, z, 0, 1.0F, 0);
                            pathToCrop = null;
                    }
                }
            }
        }
    }

    private void HervestNether(World world)
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
                    pathToCrop = world.getEntityPathToXYZ(this, i, l, j1, 16F, true, false, false, true);
                    setPathToEntity(pathToCrop);
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
                            world.setBlockAndMetadataWithNotify(x, y, z, crops.blockID, 0);
                            crops.dropBlockAsItemWithChance(worldObj, x, y, z, 3, 1.0F, 0);
                            pathToCrop = null;
                    }
                }
            }
        }
    }
}
