package unisannino.redstoneblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemNoCube extends ItemBlock
{
    private Block blockRef;

    public ItemNoCube(int i)
    {
        super(i);
        blockRef = Block.blocksList[getBlockID()];
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getColorFromDamage(int i, int j)
    {
        return blockRef.getRenderColor(i);
    }

    public int getIconFromDamage(int i)
    {
        return blockRef.getBlockTextureFromSideAndMetadata(2, i);
    }

    public int getMetadata(int i)
    {
        return i;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
        return (new StringBuilder()).append(super.getItemName()).append(".").append(ItemNoCube.nameNocube[itemstack.getItemDamage()]).toString();
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 13; ++var4)
        {
        	if(var4 == 4 || var4 == 5 || var4 == 6 || var4 == 7 || var4 == 9 || var4 == 10 || var4 == 11 || var4 == 12 || var4 == 13)
        	{
        		continue;
        	}else
        	{
                par3List.add(new ItemStack(par1, 1, var4));
        	}
        }
    }

    public static final String[] nameNocube =
    {
        "Redstone Block Slab", "Redstone Block Stairs", "Redstone Block Fence", "", "", "", "", "Redstone Block Pane",
        "", "", "", "", ""
    };
}
