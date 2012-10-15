package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.*;

public class ItemAppleBomb extends ItemEgg
{
    public ItemAppleBomb(int i)
    {
        super(i);
        maxStackSize = 64;
        this.setCreativeTab(CreativeTabs.tabCombat);
    }

    @Override
    public int getColorFromDamage(int i, int j)
    {
        return 0x0000FF;
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
        	EntityAppleBomb entity = new EntityAppleBomb(world, entityplayer);
            world.spawnEntityInWorld(entity);
            entity.setThrowableHeading(entity.motionX, entity.motionY + 1.4, entity.motionZ, 0.5F, 1.5F);
        }

        return itemstack;
    }
}
