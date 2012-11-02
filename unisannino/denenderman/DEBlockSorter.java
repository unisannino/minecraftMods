package unisannino.denenderman;

import java.util.Comparator;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

public class DEBlockSorter implements Comparator
{
    private Entity theEntity;

    public DEBlockSorter(Entity par2Entity)
    {
        this.theEntity = par2Entity;
    }

    public int sortDEBlockbyDistance(TileEntityDenEnder deblock1, TileEntityDenEnder deblock2)
    {
        double var3 = this.theEntity.getDistance(deblock1.xCoord, deblock1.yCoord, deblock1.zCoord);
        double var5 = this.theEntity.getDistance(deblock2.xCoord, deblock2.yCoord, deblock2.zCoord);
        return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
    }

    public int compare(Object par1Obj, Object par2Obj)
    {
        return this.sortDEBlockbyDistance((TileEntityDenEnder)par1Obj, (TileEntityDenEnder)par2Obj);
    }
}