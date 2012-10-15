package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.Item;

public class ItemStarPowder extends Item
{
    public ItemStarPowder(int i)
    {
        super(i);
        maxStackSize = 64;
        setMaxDamage(0);
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
}
