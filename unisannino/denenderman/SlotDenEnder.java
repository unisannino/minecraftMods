package unisannino.denenderman;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDenEnder extends Slot
{
    public SlotDenEnder(TileEntityDenEnder tileentitydenender, int i, int j, int k)
    {
        super(tileentitydenender, i, j, k);
        denender = tileentitydenender;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }
    private TileEntityDenEnder denender;
}
