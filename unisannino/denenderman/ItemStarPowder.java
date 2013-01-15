package unisannino.denenderman;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStarPowder extends Item
{
    public ItemStarPowder(int i)
    {
        super(i);
        maxStackSize = 64;
        setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return 0x41cd34;
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
}
