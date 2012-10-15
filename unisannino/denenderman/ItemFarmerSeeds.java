package unisannino.denenderman;

import java.util.Random;
import net.minecraft.src.*;

public class ItemFarmerSeeds extends Item
{
    public ItemFarmerSeeds(int i)
    {
        super(i);
        maxStackSize = 64;
        setMaxDamage(0);
    }

    @Override
    public int getColorFromDamage(int i, int j)
    {
        return 0x7b2fbe;
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
}
