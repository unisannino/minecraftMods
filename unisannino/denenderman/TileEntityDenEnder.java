package unisannino.denenderman;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDenEnder extends TileEntity implements IInventory
{
    private ItemStack mainInv[];

    public TileEntityDenEnder()
    {
        mainInv = new ItemStack[54];
    }

    @Override
	public int getSizeInventory()
    {
        return mainInv.length;
    }

    @Override
	public ItemStack getStackInSlot(int i)
    {
        return mainInv[i];
    }

    @Override
	public ItemStack decrStackSize(int i, int j)
    {
        if (mainInv[i] != null)
        {
            if (mainInv[i].stackSize <= j)
            {
                ItemStack itemstack = mainInv[i];
                mainInv[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = mainInv[i].splitStack(j);

            if (mainInv[i].stackSize == 0)
            {
                mainInv[i] = null;
            }

            onInventoryChanged();
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    @Override
	public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (mainInv[par1] != null)
        {
            ItemStack itemstack = mainInv[par1];
            mainInv[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        mainInv[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    @Override
	public String getInvName()
    {
        return "Crops";
    }

    @Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        mainInv = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;

            if (j >= 0 && j < mainInv.length)
            {
                mainInv[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < mainInv.length; i++)
        {
            if (mainInv[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                mainInv[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
	public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getFirstEmptyStack()
    {
        for (int var1 = 0; var1 < this.mainInv.length; ++var1)
        {
            if (this.mainInv[var1] == null)
            {
                return var1;
            }
        }

        return -1;
    }

    @Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }

        return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
    }

    @Override
	public void openChest()
    {
    }

    @Override
	public void closeChest()
    {
    }
}
