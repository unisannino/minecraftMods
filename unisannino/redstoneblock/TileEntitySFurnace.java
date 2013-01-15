package unisannino.redstoneblock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import cpw.mods.fml.common.registry.GameRegistry;


public class TileEntitySFurnace extends TileEntityFurnace
{

    private ItemStack sfurnaceItemStacks[];

    public TileEntitySFurnace()
    {
        sfurnaceItemStacks = new ItemStack[3];
        furnaceBurnTime = 0;
        currentItemBurnTime = 0;
        furnaceCookTime = 0;
    }

    @Override
    public int getSizeInventory()
    {
        return sfurnaceItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return sfurnaceItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (sfurnaceItemStacks[i] != null)
        {
            if (sfurnaceItemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = sfurnaceItemStacks[i];
                sfurnaceItemStacks[i] = null;
                return itemstack;
            }

            ItemStack itemstack1 = sfurnaceItemStacks[i].splitStack(j);

            if (sfurnaceItemStacks[i].stackSize == 0)
            {
                sfurnaceItemStacks[i] = null;
            }

            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    public ItemStack func_48081_b(int par1)
    {
        if (sfurnaceItemStacks[par1] != null)
        {
            ItemStack itemstack = sfurnaceItemStacks[par1];
            sfurnaceItemStacks[par1] = null;
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
        sfurnaceItemStacks[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName()
    {
        return "RedFurnace";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        sfurnaceItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if (byte0 >= 0 && byte0 < sfurnaceItemStacks.length)
            {
                sfurnaceItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        furnaceBurnTime = nbttagcompound.getShort("BurnTime");
        furnaceCookTime = nbttagcompound.getShort("CookTime");
        currentItemBurnTime = getItemBurnTime(sfurnaceItemStacks[1]);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)furnaceBurnTime);
        nbttagcompound.setShort("CookTime", (short)furnaceCookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < sfurnaceItemStacks.length; i++)
        {
            if (sfurnaceItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                sfurnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
    public int getCookProgressScaled(int i)
    {
        return (furnaceCookTime * i) / 66;
    }

    @Override
    public int getBurnTimeRemainingScaled(int i)
    {
        if (currentItemBurnTime == 0)
        {
            currentItemBurnTime = 66;
        }

        return (furnaceBurnTime * i) / currentItemBurnTime;
    }

    @Override
    public void updateEntity()
    {
        boolean flag = furnaceBurnTime > 0;
        boolean flag1 = false;

        if (furnaceBurnTime > 0)
        {
            furnaceBurnTime--;
        }

        if (!worldObj.isRemote)
        {
            if (furnaceBurnTime == 0 && canSmelt())
            {
                currentItemBurnTime = furnaceBurnTime = getItemBurnTime(sfurnaceItemStacks[1]);

                if (furnaceBurnTime > 0)
                {
                    flag1 = true;

                    if (sfurnaceItemStacks[1] != null)
                    {
                        --this.sfurnaceItemStacks[1].stackSize;

                        if (this.sfurnaceItemStacks[1].stackSize == 0)
                        {
                            Item var3 = this.sfurnaceItemStacks[1].getItem().getContainerItem();
                            this.sfurnaceItemStacks[1] = var3 == null ? null : new ItemStack(var3);
                        }
                    }
                }
            }

            if (isBurning() && canSmelt())
            {
                furnaceCookTime++;

                if (furnaceCookTime == 66)
                {
                    furnaceCookTime = 0;
                    smeltItem();
                    flag1 = true;
                }
            }
            else
            {
                furnaceCookTime = 0;
            }

            if (flag != (furnaceBurnTime > 0))
            {
                flag1 = true;
                BlockSFurnace.updateSFurnaceBlockState(furnaceBurnTime > 0, worldObj, xCoord, yCoord, zCoord);
            }
        }

        if (flag1)
        {
            onInventoryChanged();
        }
    }

    private boolean canSmelt()
    {
        if (sfurnaceItemStacks[0] == null)
        {
            return false;
        }

        ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.sfurnaceItemStacks[0]);

        if (itemstack == null)
        {
            return false;
        }

        if (sfurnaceItemStacks[2] == null)
        {
            return true;
        }

        if (!sfurnaceItemStacks[2].isItemEqual(itemstack))
        {
            return false;
        }

        if (sfurnaceItemStacks[2].stackSize < getInventoryStackLimit() && sfurnaceItemStacks[2].stackSize < sfurnaceItemStacks[2].getMaxStackSize())
        {
            return true;
        }

        return sfurnaceItemStacks[2].stackSize < itemstack.getMaxStackSize();
    }

    @Override
    public void smeltItem()
    {
        if (!canSmelt())
        {
            return;
        }

        ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.sfurnaceItemStacks[0]);

        if (sfurnaceItemStacks[2] == null)
        {
            sfurnaceItemStacks[2] = itemstack.copy();
        }
        else if (sfurnaceItemStacks[2].itemID == itemstack.itemID)
        {
            sfurnaceItemStacks[2].stackSize++;
        }

        --this.sfurnaceItemStacks[0].stackSize;

        if (this.sfurnaceItemStacks[0].stackSize == 0)
        {
            Item var2 = this.sfurnaceItemStacks[0].getItem().getContainerItem();
            this.sfurnaceItemStacks[0] = var2 == null ? null : new ItemStack(var2);
        }
    }

    public static int getItemBurnTime(ItemStack par0ItemStack)
    {

        if (par0ItemStack == null)
        {
            return 0;
        }
        else
        {
            int var1 = par0ItemStack.getItem().itemID;
            Item var2 = par0ItemStack.getItem();

            if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[var1] != null)
            {
                Block var3 = Block.blocksList[var1];

                if (var3 == Block.woodSingleSlab)
                {
                    return 150 / 3;
                }

                if (var3.blockMaterial == Material.wood)
                {
                    return 300 / 3;
                }
            }

            if (var2 instanceof ItemTool && ((ItemTool) var2).getToolMaterialName().equals("WOOD")) return 200 / 3;
            if (var2 instanceof ItemSword && ((ItemSword) var2).func_77825_f().equals("WOOD")) return 200 / 3;
            if (var2 instanceof ItemHoe && ((ItemHoe) var2).func_77842_f().equals("WOOD")) return 200 / 3;
            if (var1 == Item.stick.itemID) return 100 / 3;
            if (var1 == Item.coal.itemID) return 1600 / 3;
            if (var1 == Item.bucketLava.itemID) return 20000 / 3;
            if (var1 == Block.sapling.blockID) return 100 / 3;
            if (var1 == Item.blazeRod.itemID) return 2400 / 3;
            return GameRegistry.getFuelValue(par0ItemStack) / 3;
        }
    }
}
