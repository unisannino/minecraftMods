package unisannino.redstoneblock;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class Mod_RSB_Fuel implements IFuelHandler
{

	@Override
	public int getBurnTime(ItemStack fuel)
	{
        if (fuel.getItem() == Item.redstone)
        {
            return 2400;
        }
        if (fuel.getItem().itemID == Mod_RSB_Core.RedstoneBlock.blockID)
        {
            return 22000;
        }else
        	return 0;
	}

}
