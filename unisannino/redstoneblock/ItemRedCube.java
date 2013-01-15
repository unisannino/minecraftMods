package unisannino.redstoneblock;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemRedCube extends ItemBlock
{
    private Block blockRef;

    public ItemRedCube(int i)
    {
        super(i);
        blockRef = Block.blocksList[getBlockID()];
        setMaxDamage(0);
        setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
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
        return (new StringBuilder()).append(super.getItemName()).append(".").append(ItemRedCube.nameNocube[itemstack.getItemDamage()]).toString();
    }

    public static final String[] nameNocube =
    {
        "Block of Redstone", "double slab", "Redstone Falling Block", "Redstone Lift Block"
        //"Redstone Chest"
    };


}
