package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

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
    public int func_82790_a(ItemStack par1ItemStack, int par2)
    {
        return 0x41cd34;
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
}
