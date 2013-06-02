package unisannino.denenderman;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemFarmerSeeds extends Item implements IPlantable
{
    public ItemFarmerSeeds(int i)
    {
        super(i);
        maxStackSize = 64;
        setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return 0x7b2fbe;
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

	@Override
	public EnumPlantType getPlantType(World world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public int getPlantID(World world, int x, int y, int z)
	{
		return Mod_DenEnderman_Core.lavender.blockID;
	}

	@Override
	public int getPlantMetadata(World world, int x, int y, int z)
	{
		return 0;
	}

    public void registerIcons(IconRegister iconreg)
    {
    	this.itemIcon = iconreg.registerIcon("denender:farmerseed");
    }
}
