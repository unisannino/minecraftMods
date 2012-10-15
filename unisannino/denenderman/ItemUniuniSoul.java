package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.*;

public class ItemUniuniSoul extends ItemEgg
{
    public ItemUniuniSoul(int i)
    {
        super(i);
        maxStackSize = 64;
    }

    @Override
    public int getColorFromDamage(int i, int j)
    {
        return 0x41cd34;
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!entityplayer.capabilities.isCreativeMode)
        {
            itemstack.stackSize--;
        }

        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new EntityUniuniSoul(world, entityplayer));
        }

        return itemstack;
    }
}
