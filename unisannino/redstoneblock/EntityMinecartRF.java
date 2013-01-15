package unisannino.redstoneblock;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityMinecartRF extends EntityMinecart
{
    public EntityMinecartRF(World par1World)
    {
        super(par1World);
        float speed = 12F;
        this.setMaxSpeedRail(speed);
    }

	public EntityMinecartRF(World par1World, double par2, double par3, double par4)
	{
		super(par1World, par2, par3, par4, 2);
	}

	@Override
    protected void entityInit()
    {
		super.entityInit();
		this.dataWatcher.addObject(20, new Integer(0));
    }

	@Override
	public void onUpdate()
	{
		super.onUpdate();

        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0)
        {
            this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (this.getExplode() > 1)
        {
        	int i = this.getExplode() - 1;
        	this.setExplode(i);
        }else
        if(this.getExplode() == 1 && !worldObj.isRemote)
        {
            setDead();
            worldObj.createExplosion(null, posX, posY, posZ, 4.0F, true);
        }
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer)
	{
        ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

        if (var2 != null && var2.itemID == Block.tnt.blockID)
        {
            if (--var2.stackSize == 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
            }

            this.setExplode(80);
            worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 1.0F);
            return true;
        }else
        if (var2 != null && var2.itemID == Item.coal.itemID)
        {
            if (--var2.stackSize == 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
            }

            this.fuel += 3600;
        }

        this.pushX = this.posX - par1EntityPlayer.posX;
        this.pushZ = this.posZ - par1EntityPlayer.posZ;

		return true;
	}

	@Override
    public List<ItemStack> getItemsDropped()
    {
        List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(Item.minecartEmpty));
        items.add(new ItemStack(Mod_RSB_Core.RedFurnaceIdle));

        return items;
    }

	@Override
    protected void applyDragAndPushForces()
    {
        if(isPoweredCart())
        {
            double d27 = MathHelper.sqrt_double(pushX * pushX + pushZ * pushZ);
            if(d27 > 0.01D)
            {
                pushX /= d27;
                pushZ /= d27;
                double d29 = 0.04 * 3;
                motionX *= 0.8D;
                motionY *= 0.0D;
                motionZ *= 0.8D;
                motionX += pushX * d29;
                motionZ += pushZ * d29;
            }
            else
            {
                motionX *= 0.9D;
                motionY *= 0.0D;
                motionZ *= 0.9D;
            }
        }
        motionX *= getDrag();
        motionY *= 0.0D;
        motionZ *= getDrag();
    }

	@Override
    public boolean isPoweredCart()
    {
        return true;
    }

	public int getExplode()
	{
		return this.dataWatcher.getWatchableObjectInt(20);
	}

	public void setExplode(int i)
	{
		this.dataWatcher.updateObject(20, Integer.valueOf(i));
	}

}
