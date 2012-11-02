package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.*;

public class TileEntityDenEnder extends TileEntity implements IInventory
{
    private ItemStack mainInv[];

    public TileEntityDenEnder()
    {
        mainInv = new ItemStack[54];
    }

    public int getSizeInventory()
    {
        return mainInv.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return mainInv[i];
    }

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

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        mainInv[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    public String getInvName()
    {
        return "Crops";
    }

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

    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }

        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    public void openChest()
    {
    }

    public void closeChest()
    {
    }
}
