package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.*;

public class TileEntityDenEnder extends TileEntity
    implements IInventory
{
    public TileEntityDenEnder()
    {
        DenderContents = new ItemStack[54];
    }

    public int getSizeInventory()
    {
        return DenderContents.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return DenderContents[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if (DenderContents[i] != null)
        {
            if (DenderContents[i].stackSize <= j)
            {
                ItemStack itemstack = DenderContents[i];
                DenderContents[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = DenderContents[i].splitStack(j);

            if (DenderContents[i].stackSize == 0)
            {
                DenderContents[i] = null;
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
        if (DenderContents[par1] != null)
        {
            ItemStack itemstack = DenderContents[par1];
            DenderContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        DenderContents[i] = itemstack;

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
        DenderContents = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;

            if (j >= 0 && j < DenderContents.length)
            {
                DenderContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < DenderContents.length; i++)
        {
            if (DenderContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                DenderContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
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

    private ItemStack DenderContents[];
}
