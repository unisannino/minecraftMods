package unisannino.denenderman;

import java.util.Random;
import net.minecraft.src.*;
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
    public int func_82790_a(ItemStack par1ItemStack, int par2)
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
		return Mod_DenEnderman_Core.Lavender.blockID;
	}

	@Override
	public int getPlantMetadata(World world, int x, int y, int z)
	{
		return 0;
	}
}
