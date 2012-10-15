package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.*;

public class BlockStarSandlayer extends Block
{
    protected BlockStarSandlayer(int i, int j)
    {
        super(i, j, Material.circuits);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        setTickRandomly(true);
        rand = new Random();
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k) & 7;

        if (l >= 3)
        {
            return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)i + minX, (double)j + minY, (double)k + minZ, (double)i + maxX, (float)j + 0.5F, (double)k + maxZ);
        }
        else
        {
            return null;
        }
    }

    public int getRenderColor(int i)
    {
        return 0x41cd34;
        //0xfcedba;
    }

    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return 0x41cd34;
        //0xfcedba;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k) & 7;
        float f = (float)(2 * (1 + l)) / 16F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j - 1, k);

        if (l == 0 || !Block.blocksList[l].isOpaqueCube())
        {
            return false;
        }
        else
        {
            return world.getBlockMaterial(i, j - 1, k).isSolid();
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        func_314_h(world, i, j, k);

        if (rand.nextInt(10) == 0)
        {
            world.setBlockWithNotify(i, j, k, 0);
        }
    }

    private boolean func_314_h(World world, int i, int j, int k)
    {
        if (!canPlaceBlockAt(world, i, j, k))
        {
            world.setBlockWithNotify(i, j, k, 0);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        int i1 = Mod_DenEnderman_Core.StarPowder.shiftedIndex;
        float f = 0.7F;
        double d = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double)i + d, (double)j + d1, (double)k + d2, new ItemStack(i1, 1, 0));
        entityitem.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entityitem);
        world.setBlockWithNotify(i, j, k, 0);
        entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
    }

    public int idDropped(int i, Random random, int j)
    {
        return Mod_DenEnderman_Core.StarPowder.shiftedIndex;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        world.setBlockWithNotify(i, j, k, 0);
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if (l == 1)
        {
            return true;
        }
        else
        {
            return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
        }
    }
    private Random rand;
}
