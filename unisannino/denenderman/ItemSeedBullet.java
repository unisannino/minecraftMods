package unisannino.denenderman;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSeedBullet extends Item
{
	public static int[] colorTable = {0x9ACD32, 0x4A4A4A, 0xF5F5DC, 0x61402A};


	public ItemSeedBullet(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setMaxStackSize(32);
		this.setCreativeTab(CreativeTabs.tabCombat);
	}

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return colorTable[par1ItemStack.getItemDamage()];
    }

    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int j = 0; j < colorTable.length; ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }

	@Override
	public void registerIcons(IconRegister icoreg)
	{
		this.itemIcon = icoreg.registerIcon("denender:sbullet");
	}

}
