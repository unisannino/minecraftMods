package unisannino.denenderman;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityDenEnder extends TileEntity implements IInventory
{
    private ItemStack mainInv[];
    private Random rand;
    private String invName;

    public TileEntityDenEnder()
    {
        mainInv = new ItemStack[54];
        this.rand = new Random();
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

	public int[] buildIntDataList()
	{
		int[] itemsdata = new int[this.mainInv.length * 3];
		int i = 0;
		for(ItemStack is : this.mainInv)
		{
			if(is != null)
			{
				itemsdata[i++] = is.itemID;
				itemsdata[i++] = is.getItemDamage();
				itemsdata[i++] = is.stackSize;
			}else
			{
				itemsdata[i++] = 0;
				itemsdata[i++] = 0;
				itemsdata[i++] = 0;
			}
		}
		return itemsdata;
	}

	@Override
    public void updateEntity()
    {
        this.generateLavender(this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord);
    }

    private void generateLavender(World world, int i, int j, int k)
	{
		if(world.isDaytime() && world.getWorldTime() == 6000)
		{
			int x = i + -4 + this.rand.nextInt(8);
			int z = k + -4 + this.rand.nextInt(8);

			if(world.getBlockId(x, j, z) == 0 && world.canBlockSeeTheSky(x, j, z) && world.getBlockId(x, j - 1, z) == Block.grass.blockID)
			{
				world.setBlock(x, j, z, Mod_DenEnderman_Core.lavender.blockID);
			}
		}
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

    public Packet getDescriptionPacket()
    {
        return Mod_DenEnderman_Packet.getPacket(this);
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
        return this.isInvNameLocalized() ? this.invName : "container.denender";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return this.invName != null && this.invName.length() > 0;
    }

    public void setInvName(String par1Str)
    {
        this.invName = par1Str;
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

        if (nbttagcompound.hasKey("CustomName"))
        {
            this.invName = nbttagcompound.getString("CustomName");
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

        if (this.isInvNameLocalized())
        {
            nbttagcompound.setString("CustomName", this.invName);
        }
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

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	public void handlePacketData(int[] items, String names)
	{
		if(items != null)
		{
			int i = 0;
			if(items.length < this.mainInv.length * 3)
			{
				return;
			}

			for(int j = 0; j < this.mainInv.length; j++)
			{
				if(items[i + 2] != 0)
				{
					ItemStack is = new ItemStack(items[i], items[i + 2], items[i + 1]);
					this.mainInv[j] = is;
				}else
				{
					this.mainInv[j] = null;
				}
				i += 3;
			}
		}

		if (names != null)
		{
			this.setInvName(names);
		}
	}


}
