package unisannino.redstoneblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFallingBlock extends EntityFallingSand
{
    public int blockID;
    public int fallTime;
    private int startgame;

    public EntityFallingBlock(World world)
    {
        super(world);
        fallTime = 0;
    }

    public EntityFallingBlock(World world, double d, double d1, double d2,
            int i, int j)
    {
        super(world);
        fallTime = 0;
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
        nbttagcompound.setInteger("fall", fallTime);
        nbttagcompound.setInteger("block", blockID);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        fallTime = nbttagcompound.getInteger("fall");
        blockID = nbttagcompound.getInteger("block");
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
            ItemStack itemstack = new ItemStack(blockID, 1 , 2) ;
            EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, itemstack);
            entityitem.delayBeforeCanPickup = 10;
            worldObj.spawnEntityInWorld(entityitem);
            setDead();
        }

        return true;
    }

    @Override
    public void onUpdate()
    {
        if (startgame == 0)
        {
            ItemStack itemstack = new ItemStack(blockID, 1 , 2) ;
            EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, itemstack);
            entityitem.delayBeforeCanPickup = 10;
            worldObj.spawnEntityInWorld(entityitem);
            setDead();
            return;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        fallTime++;
        motionY -= 0.04D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.98D;
        motionY *= 0.18D;
        motionZ *= 0.98D;
        int var1 = MathHelper.floor_double(posX);
        int var2 = MathHelper.floor_double(posY);
        int var3 = MathHelper.floor_double(posZ);

        if (fallTime == 1 && worldObj.getBlockId(var1, var2, var3) == blockID)
        {
            worldObj.setBlockWithNotify(var1, var2, var3, 0);
        }

        if (onGround)
        {
            motionX *= 0.7D;
            motionZ *= 0.7D;
            motionY *= -0.5D;

            if (worldObj.getBlockId(var1, var2, var3) != Block.pistonMoving.blockID)
            {
                setDead();

                if ((!worldObj.canPlaceEntityOnSide(this.blockID, var1, var2, var3, true, 1, (Entity)null) || BlockSand.canFallBelow(worldObj, var1, var2 - 1, var3) || !worldObj.setBlockAndMetadataWithNotify(var1, var2, var3, blockID, 2)) && !worldObj.isRemote)
                {
                    ItemStack itemstack = new ItemStack(blockID, 1 , 2) ;
                    EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, itemstack);
                    entityitem.delayBeforeCanPickup = 10;
                    worldObj.spawnEntityInWorld(entityitem);
                    //dropItem(blockID, 1);
                }
            }
        }
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
