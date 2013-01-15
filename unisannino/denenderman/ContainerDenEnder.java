package unisannino.denenderman;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainerDenEnder extends Container
{

    private TileEntityDenEnder tileEntitydenender;
    private InventoryPlayer playeri;
    private int numRows;

    public ContainerDenEnder(InventoryPlayer iinventory, TileEntityDenEnder tileentitydenender)
    {
        tileEntitydenender = tileentitydenender;
        playeri = iinventory;
        numRows = tileentitydenender.getSizeInventory() / 9;
        int i = (numRows - 4) * 18;

        for (int j = 0; j < numRows; j++)
        {
            for (int i1 = 0; i1 < 9; i1++)
            {
                this.addSlotToContainer(new SlotDenEnder(tileentitydenender, i1 + j * 9, 8 + i1 * 18, 18 + j * 18));
            }
        }

        for (int k = 0; k < 3; k++)
        {
            for (int j1 = 0; j1 < 9; j1++)
            {
                this.addSlotToContainer(new Slot(iinventory, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
            }
        }

        for (int l = 0; l < 9; l++)
        {
            this.addSlotToContainer(new Slot(iinventory, l, 8 + l * 18, 161 + i));
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return tileEntitydenender.isUseableByPlayer(entityplayer);
    }

    @Override

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i < numRows * 9)
            {
                if (!mergeItemStack(itemstack1, numRows * 9, numRows * 9 + 36, true))
                {
                    return null;
                }
            }
            else if (i >= numRows * 9 && i < numRows * 9 + 27)
            {
                if (!mergeItemStack(itemstack1, numRows * 9 + 27, numRows * 9 + 36, false))
                {
                    return null;
                }
            }
            else if (i >= numRows * 9 + 27 && i < numRows * 9 + 36)
            {
                if (!mergeItemStack(itemstack1, numRows * 9, numRows * 9 + 27, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, numRows * 9, numRows * 9 + 36, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);
        this.tileEntitydenender.closeChest();
    }
}
