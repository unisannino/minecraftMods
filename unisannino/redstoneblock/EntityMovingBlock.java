package unisannino.redstoneblock;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityMovingBlock extends EntityFallingSand
{
    public int moveTime;
    private int direction;
    private int startgame;

    public EntityMovingBlock(World world)
    {
        super(world);
        preventEntitySpawning = true;
        moveTime = 0;
    }

    public EntityMovingBlock(World world, double d, double d1, double d2,
            int i, int m, int j)
    {
        super(world);
        moveTime = 0;
        blockID = i;
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
        yOffset = height / 2.0F;
        setPosition(d, d1, d2);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = d;
        prevPosY = d1;
        prevPosZ = d2;
        direction = m;
        startgame = j;
    }

    @Override
    protected void entityInit()
    {
        dataWatcher.addObject(17, new Integer(0));
        dataWatcher.addObject(18, new Integer(1));
        dataWatcher.addObject(19, new Integer(0));
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return entity.boundingBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Direction", direction);
        nbttagcompound.setInteger("move", moveTime);
        nbttagcompound.setInteger("block", blockID);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        direction = nbttagcompound.getInteger("Direction");
        moveTime = nbttagcompound.getInteger("move");
        blockID = nbttagcompound.getInteger("block");

        if (this.blockID == 0)
        {
            this.blockID = Mod_RSB_Core.RedstoneBlock.blockID;
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (worldObj.isRemote || isDead)
        {
            return true;
        }

        setForwardDirection(-getForwardDirection());
        setTimeSinceHit(10);
        setDamageTaken(getDamageTaken() + i * 10);
        setBeenAttacked();

        if (getDamageTaken() > 40)
        {
            ItemStack itemstack = new ItemStack(Mod_RSB_Core.RedstoneBlock.blockID, 1 , 3) ;
            EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, itemstack);
            entityitem.delayBeforeCanPickup = 10;
            worldObj.spawnEntityInWorld(entityitem);
            setDead();
        }

        return true;
    }

    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
            if (!worldObj.isRemote)
            {
            	ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
            	if(itemstack != null && itemstack.itemID == Item.redstone.itemID && moveTime > 900)
            	{
                    itemstack.stackSize--;

                    if (itemstack.stackSize <= 0)
                    {
                        par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                    }

                    moveTime = 0;

            	}else if(riddenByEntity == null || riddenByEntity == par1EntityPlayer)
                {
                    par1EntityPlayer.mountEntity(this);
                    return true;
                }
            }

            return super.interact(par1EntityPlayer);
        }

    @Override
    public void onUpdate()
    {
    	//System.out.println(moveTime +" " + startgame);

        if (startgame == 0)
        {
            ItemStack itemstack = new ItemStack(Mod_RSB_Core.RedstoneBlock.blockID, 1 , 3) ;
            EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, itemstack);
            entityitem.delayBeforeCanPickup = 10;
            worldObj.spawnEntityInWorld(entityitem);
            setDead();
            return;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        moveTime++;

        switch (direction)
        {
            case 0:
            {
                //System.out.println("0");
                motionZ += 0.04D;
                moveEntity(motionX, motionY, motionZ);
                motionX *= 0.98D;
                motionY *= 0.98D;
                motionZ *= 0.18D;
                break;
            }

            case 1:
            {
                //System.out.println("1");
                motionX -= 0.04D;
                moveEntity(motionX, motionY, motionZ);
                motionX *= 0.18D;
                motionY *= 0.98D;
                motionZ *= 0.98D;
                break;
            }

            case 2:
            {
                //System.out.println("2");
                motionZ -= 0.04D;
                moveEntity(motionX, motionY, motionZ);
                motionX *= 0.98D;
                motionY *= 0.98D;
                motionZ *= 0.18D;
                break;
            }

            case 3:
            {
                //System.out.println("3");
                motionX += 0.04D;
                moveEntity(motionX, motionY, motionZ);
                motionX *= 0.18D;
                motionY *= 0.98D;
                motionZ *= 0.98D;
                break;
            }
        }

        int var1 = MathHelper.floor_double(posX);
        int var2 = MathHelper.floor_double(posY);
        int var3 = MathHelper.floor_double(posZ);

        if (moveTime == 1 && worldObj.getBlockId(var1, var2, var3) == Mod_RSB_Core.RedstoneBlock.blockID)
        {
            worldObj.setBlockWithNotify(var1, var2, var3, 0);
        }

        if (checkstop(var1, var2, var3))
        {
            moveTime++;
            if (worldObj.getBlockId(var1, var2, var3) != Block.pistonMoving.blockID)
            {
                setDead();
            	if(riddenByEntity != null)
            	{
            		riddenByEntity.motionY += 0.5;
            		riddenByEntity.moveEntity(0, riddenByEntity.motionY, 0);
            	}

                if ((!worldObj.canPlaceEntityOnSide(this.blockID, var1, var2, var3, true, 1, (Entity)null)|| !worldObj.setBlockAndMetadataWithNotify(var1, var2, var3, blockID, 3)) && !worldObj.isRemote)
                {
                    ItemStack itemstack = new ItemStack(blockID, 1 , 3) ;
                    EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, itemstack);
                    entityitem.delayBeforeCanPickup = 10;
                    worldObj.spawnEntityInWorld(entityitem);
                    //dropItem(blockID, 1);
                }
            }
        }else
        	if (!worldObj.isRemote && moveTime > 1000)
            {
                ItemStack itemstack = new ItemStack(blockID, 1 , 3) ;
                EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, itemstack);
                entityitem.delayBeforeCanPickup = 10;
                worldObj.spawnEntityInWorld(entityitem);
                setDead();
            }
    }

    private boolean checkstop(int i, int j, int k)
    {
        switch (direction)
        {
            case 0:
            {
                int c = worldObj.getBlockId(i, j, k + 1);
                return c != 0;
            }

            case 1:
            {
                int c = worldObj.getBlockId(i - 1, j, k);
                return c != 0;
            }

            case 2:
            {
                int c = worldObj.getBlockId(i, j, k - 1);
                return c != 0;
            }

            case 3:
            {
                int c = worldObj.getBlockId(i + 1, j, k);
                return c != 0;
            }
        }

        return false;
    }

    public int getForwardDirection()
    {
        return dataWatcher.getWatchableObjectInt(18);
    }

    public void setForwardDirection(int i)
    {
        dataWatcher.updateObject(18, Integer.valueOf(i));
    }

    public void setDamageTaken(int i)
    {
        dataWatcher.updateObject(19, Integer.valueOf(i));
    }

    public int getDamageTaken()
    {
        return dataWatcher.getWatchableObjectInt(19);
    }

    public void setTimeSinceHit(int i)
    {
        dataWatcher.updateObject(17, Integer.valueOf(i));
    }
}
