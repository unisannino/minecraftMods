package unisannino.denenderman;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAppleBomb extends ItemEgg
{
    public ItemAppleBomb(int i)
    {
        super(i);
        maxStackSize = 64;
        this.setCreativeTab(CreativeTabs.tabCombat);
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
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
            entity.setThrowableHeading(entity.motionX, entity.motionY + 1.4, entity.motionZ, 0.5F, 1.5F);
            world.spawnEntityInWorld(entity);

        }

        return itemstack;
    }

    @Override
    public void registerIcons(IconRegister icoreg)
    {
        this.itemIcon = Item.appleRed.getIconFromDamage(0);
    }
}
