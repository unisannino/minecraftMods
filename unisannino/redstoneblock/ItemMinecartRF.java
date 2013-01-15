package unisannino.redstoneblock;

import net.minecraft.block.BlockRail;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMinecartRF extends Item
{
    public ItemMinecartRF(int i)
    {
        super(i);
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return 0xb3312c;
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int i1 = par3World.getBlockId(par4, par5, par6);

        if (BlockRail.isRailBlock(i1))
        {
            if (!par3World.isRemote)
            {
                par3World.spawnEntityInWorld(new EntityMinecartRF(par3World, par4 + 0.5F, par5 + 0.5F, par6 + 0.5F));
            }

            par1ItemStack.stackSize--;
            return true;
        }
        else
        {
            return false;
        }
    }
}
