package unisannino.redstoneblock;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockRedstoneBlock extends Block
{
    public BlockRedstoneBlock(int i, boolean flag)
    {
        super(i, Material.iron);

        if (flag)
        {
            setTickRandomly(true);
        }

        glowing = flag;
        setLightOpacity(255);
        useNeighborBrightness[i] = true;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public int getRenderColor(int i)
    {
    	return glowing ? 0xd14f4a : 0xb3312c;
    }

    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return glowing ? 0xd14f4a : 0xb3312c;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if (j == 2)
        {
            return blockIndexInTexture + (16 * 8 + 6);
        }

        if (j == 3)
        {
            if (i == 0 || i == 1)
            {
                return blockIndexInTexture - 16 ;
            }
            else
            {
                return blockIndexInTexture - 17;
            }
        }
        else
        {
            return blockIndexInTexture;
        }
    }

    @Override
    public int tickRate()
    {
        return 3;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        int rsb = this.blockID;

        int m = world.getBlockMetadata(i, j - 1, k);
        int m1 = world.getBlockMetadata(i, j - 2, k);
        int m2 = world.getBlockMetadata(i - 1, j - 1, k);
        int m3 = world.getBlockMetadata(i + 1, j - 1, k);
        int m4 = world.getBlockMetadata(i, j - 1, k - 1);
        int m5 = world.getBlockMetadata(i, j - 1, k + 1);

    	int id = world.getBlockId(i, j - 1, k);
    	int id1 = world.getBlockId(i, j - 2, k);
    	int id2 = world.getBlockId(i - 1, j - 1, k);
    	int id3 = world.getBlockId(i + 1, j - 1, k);
    	int id4 = world.getBlockId(i, j - 1, k - 1);
    	int id5 = world.getBlockId(i, j - 1, k + 1);

        if (l == 0)
        {
            updateRSBState(world, i, j, k);
        }
        else if( l == 2 && id == rsb && id1 == rsb && m == 0 && m1 == 0)
        {
            boolean flag = id2 == rsb && id3 == rsb && m2 == 0 && m3 == 0;
            boolean flag1 = id4 == rsb && id5 == rsb && m4 == 0&& m5 == 0;

            if (flag || flag1)
            {
                world.setBlock(i, j, k, 0);
                world.setBlock(i, j - 1, k, 0);
                world.setBlock(i, j - 2, k, 0);

                if (flag)
                {
                    world.setBlock(i - 1, j - 1, k, 0);
                    world.setBlock(i + 1, j - 1, k, 0);
                }
                else
                {
                    world.setBlock(i, j - 1, k - 1, 0);
                    world.setBlock(i, j - 1, k + 1, 0);
                }

                EntityRSBGolem golem = new EntityRSBGolem(world);
                golem.getMadebyPlayer(true);
                golem.setLocationAndAngles(i + 0.5D, j - 1.95D, k + 0.5D, 0.0F, 0.0F);
                world.spawnEntityInWorld(golem);

                for (int pow = 0; pow < 120; pow++)
                {
                    world.spawnParticle("snowballpoof", (double)i + world.rand.nextDouble(), (double)(j - 2) + world.rand.nextDouble() * 3.9D, (double)k + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                }

                world.notifyBlockChange(i, j, k, 0);
                world.notifyBlockChange(i, j - 1, k, 0);
                world.notifyBlockChange(i, j - 2, k, 0);

                if (flag)
                {
                    world.notifyBlockChange(i - 1, j - 1, k, 0);
                    world.notifyBlockChange(i + 1, j - 1, k, 0);
                }
                else
                {
                    world.notifyBlockChange(i, j - 1, k - 1, 0);
                    world.notifyBlockChange(i, j - 1, k + 1, 0);
                }
            }
        }
        else
        {
            super.onBlockAdded(world, i, j, k);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        int  m = world.getBlockMetadata(i, j, k);

        if (m == 0)
        {
            updateRSBState(world, i, j, k);
        }
        else
        {
            super.onNeighborBlockChange(world, i, j, k, l);
        }
    }

    private void updateRSBState(World world, int i, int j, int k)
    {
        boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k);

        if (flag)
        {
            world.setBlockWithNotify(i, j, k, Mod_RSB_Core.RedstoneBlockA.blockID);
        }
        else
        {
            world.setBlockWithNotify(i, j, k, Mod_RSB_Core.RedstoneBlock.blockID);
        }
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            return Mod_RSB_Core.RedstoneBlock.blockID;
        }
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if (glowing)
        {
            glowingblock(world, i, j, k);
        }
    }

    @Override
    public void onEntityWalking(World world, int i, int j, int k, Entity entity)
    {
        int m = world.getBlockMetadata(i, j, k);

        if (m == 0 || m == 1)
        {
            if (glowing)
            {
                entity.setFire(300);
            }
        }
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    private void glowingblock(World world, int i, int j, int k)
    {
        Random random = world.rand;
        double d = 0.0625D;

        for (int l = 0; l < 6; l++)
        {
            double d1 = i + random.nextFloat();
            double d2 = j + random.nextFloat();
            double d3 = k + random.nextFloat();

            if (l == 0 && !world.isBlockOpaqueCube(i, j + 1, k))
            {
                d2 = (j + 1) + d;
            }

            if (l == 1 && !world.isBlockOpaqueCube(i, j - 1, k))
            {
                d2 = (j + 0) - d;
            }

            if (l == 2 && !world.isBlockOpaqueCube(i, j, k + 1))
            {
                d3 = (k + 1) + d;
            }

            if (l == 3 && !world.isBlockOpaqueCube(i, j, k - 1))
            {
                d3 = (k + 0) - d;
            }

            if (l == 4 && !world.isBlockOpaqueCube(i + 1, j, k))
            {
                d1 = (i + 1) + d;
            }

            if (l == 5 && !world.isBlockOpaqueCube(i - 1, j, k))
            {
                d1 = (i + 0) - d;
            }

            if (d1 < i || d1 > (i + 1) || d2 < 0.0D || d2 > (j + 1) || d3 < k || d3 > (k + 1))
            {
                world.spawnParticle("reddust", d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public static boolean canFallBelow(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j, k);

        if (l == 0)
        {
            return true;
        }

        if (l == Block.fire.blockID)
        {
            return true;
        }

        Material material = Block.blocksList[l].blockMaterial;

        if (material == Material.water)
        {
            return true;
        }

        return material == Material.lava;
    }

    private boolean glowing;
    public static boolean fallInstantly = false;
}
