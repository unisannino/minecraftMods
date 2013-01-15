package unisannino.redstoneblock;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedNoCube extends Block
{
    public BlockRedNoCube(int i)
    {
        super(i, Material.iron);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        setLightOpacity(255);
        useNeighborBrightness[i] = true;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        setBlockBoundsBasedOnState(world, i, j, k);
        return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);

        if (l == 2)
        {
            boolean flag = canConnectFenceTo(world, i, j, k - 1);
            boolean flag1 = canConnectFenceTo(world, i, j, k + 1);
            boolean flag2 = canConnectFenceTo(world, i - 1, j, k);
            boolean flag3 = canConnectFenceTo(world, i + 1, j, k);
            float f = 0.375F;
            float f1 = 0.625F;
            float f2 = 0.375F;
            float f3 = 0.625F;

            if (flag)
            {
                f2 = 0.0F;
            }

            if (flag1)
            {
                f3 = 1.0F;
            }

            if (flag2)
            {
                f = 0.0F;
            }

            if (flag3)
            {
                f1 = 1.0F;
            }

            return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(i + f, j, k + f2, i + f1, j + 1.5F, k + f3);
        }
        else if (l == 0)
        {
            setBlockBoundsBasedOnState(world, i, j, k);
        }

        return super.getCollisionBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k);

        if (l == 2)
        {
            boolean flag = canConnectFenceTo(iblockaccess, i, j, k - 1);
            boolean flag1 = canConnectFenceTo(iblockaccess, i, j, k + 1);
            boolean flag2 = canConnectFenceTo(iblockaccess, i - 1, j, k);
            boolean flag3 = canConnectFenceTo(iblockaccess, i + 1, j, k);
            float f = 0.375F;
            float f1 = 0.625F;
            float f2 = 0.375F;
            float f3 = 0.625F;

            if (flag)
            {
                f2 = 0.0F;
            }

            if (flag1)
            {
                f3 = 1.0F;
            }

            if (flag2)
            {
                f = 0.0F;
            }

            if (flag3)
            {
                f1 = 1.0F;
            }

            setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
        }
        else if (l == 0)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
        else if (l == 7)
        {
            float f = 0.4375F;
            float f1 = 0.5625F;
            float f2 = 0.4375F;
            float f3 = 0.5625F;
            boolean flag = canThisPaneConnectToThisBlockID(iblockaccess.getBlockId(i, j, k - 1));
            boolean flag1 = canThisPaneConnectToThisBlockID(iblockaccess.getBlockId(i, j, k + 1));
            boolean flag2 = canThisPaneConnectToThisBlockID(iblockaccess.getBlockId(i - 1, j, k));
            boolean flag3 = canThisPaneConnectToThisBlockID(iblockaccess.getBlockId(i + 1, j, k));

            if (flag2 && flag3 || !flag2 && !flag3 && !flag && !flag1)
            {
                f = 0.0F;
                f1 = 1.0F;
            }
            else if (flag2 && !flag3)
            {
                f = 0.0F;
            }
            else if (!flag2 && flag3)
            {
                f1 = 1.0F;
            }

            if (flag && flag1 || !flag2 && !flag3 && !flag && !flag1)
            {
                f2 = 0.0F;
                f3 = 1.0F;
            }
            else if (flag && !flag1)
            {
                f2 = 0.0F;
            }
            else if (!flag && flag1)
            {
                f3 = 1.0F;
            }

            setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
        }
        else if (l == 8)
        {
            setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            //setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
        }
    }

    @Override
    public void addCollidingBlockToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = 0.5F;
        float f3 = 1.0F;

        if (l > 8)
        {
            f = 0.5F;
            f1 = 1.0F;
            f2 = 0.0F;
            f3 = 0.5F;
        }

        setBlockBounds(0.0F, f, 0.0F, 1.0F, f1, 1.0F);
        super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);

        if (l == 3 || l == 9)
        {
            setBlockBounds(0.5F, f2, 0.0F, 1.0F, f3, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (l == 4 || l == 10)
        {
            setBlockBounds(0.0F, f2, 0.0F, 0.5F, f3, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (l == 5 || l == 11)
        {
            setBlockBounds(0.0F, f2, 0.5F, 1.0F, f3, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (l == 6 || l == 12)
        {
            setBlockBounds(0.0F, f2, 0.0F, 1.0F, f3, 0.5F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (l == 7)
        {
            boolean flag = canThisPaneConnectToThisBlockID(par1World.getBlockId(par2, par3, par4 - 1));
            boolean flag1 = canThisPaneConnectToThisBlockID(par1World.getBlockId(par2, par3, par4 + 1));
            boolean flag2 = canThisPaneConnectToThisBlockID(par1World.getBlockId(par2 - 1, par3, par4));
            boolean flag3 = canThisPaneConnectToThisBlockID(par1World.getBlockId(par2 + 1, par3, par4));

            if (flag2 && flag3 || !flag2 && !flag3 && !flag && !flag1)
            {
                setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
                super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
            }
            else if (flag2 && !flag3)
            {
                setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
                super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
            }
            else if (!flag2 && flag3)
            {
                setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
                super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
            }

            if (flag && flag1 || !flag2 && !flag3 && !flag && !flag1)
            {
                setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
                super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
            }
            else if (flag && !flag1)
            {
                setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
                super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
            }
            else if (!flag && flag1)
            {
                setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
                super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
            }
        }
        else if (l == 8)
        {
            setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (l == 0 || l == 2)
        {
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }

        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }

    @Override
    public int getBlockTextureFromSide(int i)
    {
        return Mod_RSB_Core.RedstoneBlock.blockIndexInTexture;
    }

    //add
    public int getSideTextureIndex()
    {
        return Mod_RSB_Core.RedstoneBlock.blockIndexInTexture;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderColor(int i)
    {
    	/*
        if (Mod_RSB_Core.ChooseTextures)
        {
            return 0xb3312c;
        }
        else
        {
            return 0xffffff;
        }
        */

        return 0xb3312c;
    }

    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k);

    	/*
        if (Mod_RSB_Core.ChooseTextures)
        {
            return 0xb3312c;
        }
        else
        {
            return 0xffffff;
        }
        */

        return 0xb3312c;
    }

    /*
    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        int i1 = world.getBlockId(i, j - 1, k);
        if(i1 == mod_RedstoneBlock.RedstoneStepone.blockID)
        {
        	world.setBlockWithNotify(i, j, k, 0);
        	world.setBlockWithNotify(i, j - 1, k, mod_RedstoneBlock.RedstoneStep.blockID);
        }
    }
    */

    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        int i1 = world.getBlockId(i, j - 1, k);
        int l = world.getBlockMetadata(i, j - 1, k);

        //System.out.println(l);
        /*
        if (i1 == Mod_RSB_Core.RedstoneNoCube.blockID && l == 0)
        {
            world.setBlockWithNotify(i, j, k, 0);
            world.setBlockAndMetadataWithNotify(i, j - 1, k, Mod_RSB_Core.RedstoneBlock.blockID, 1);
        }
        */

        super.onBlockAdded(world, i, j, k);
    }

    /*
    @Override
    public int idDropped(int i, Random random, int j)
    {
        return Mod_RSB_Core.RedstoneNoCube.blockID;
    }
    */

    @Override
    public int damageDropped(int i)
    {
        return i == 0 || i == 2  || i == 7 ? i : i == 1 ? 1 : 0;
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int m = world.getBlockMetadata(i, j, k);
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

        if (m == 0)
        {
        }
        else if (m == 1)
        {
            if (l == 0)
            {
                world.setBlockMetadataWithNotify(i, j, k, 5);
            }

            if (l == 1)
            {
                world.setBlockMetadataWithNotify(i, j, k, 4);
            }

            if (l == 2)
            {
                world.setBlockMetadataWithNotify(i, j, k, 6);
            }

            if (l == 3)
            {
                world.setBlockMetadataWithNotify(i, j, k, 3);
            }
        }
        else if (m == 9)
        {
            if (l == 0)
            {
                world.setBlockMetadataWithNotify(i, j, k, 11);
            }

            if (l == 1)
            {
                world.setBlockMetadataWithNotify(i, j, k, 10);
            }

            if (l == 2)
            {
                world.setBlockMetadataWithNotify(i, j, k, 12);
            }

            if (l == 3)
            {
                world.setBlockMetadataWithNotify(i, j, k, 9);
            }
        }
        else
        {
            super.onBlockPlacedBy(world, i, j, k, entityliving);
        }
    }

    public void onBlockPlaced(World par1World, int par2, int par3, int par4, int par5)
    {
        int m = par1World.getBlockMetadata(par2, par3, par4);

        if (par5 == 0)
        {
            if (m == 0)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 8);
            }
            else if (m == 1)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 9);
                System.out.println(par1World.getBlockMetadata(par2, par3, par4));
            }
        }
    }

    /*
    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
    	int m = iblockaccess.getBlockMetadata(i, j, k);
        if(this != mod_RedstoneBlock.RedstoneStepone)
        {
            super.shouldSideBeRendered(iblockaccess, i, j, k, l);
        }
        if(l == 1)
        {
            return true;
        }
        if(!super.shouldSideBeRendered(iblockaccess, i, j, k, l))
        {
            return false;
        }
        if(l == 0)
        {
            return true;
        } else
        {
            return iblockaccess.getBlockId(i, j, k) != blockID;
        }
    }
    */

    /*
    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if(this != mod_RedstoneBlock.RedstoneStepone)
        {
            super.shouldSideBeRendered(iblockaccess, i, j, k, l);
        }
        if(l == 1)
        {
            return true;
        }
        if(!super.shouldSideBeRendered(iblockaccess, i, j, k, l))
        {
            return false;
        }
        if(l == 0)
        {
            return true;
        } else
        {
            return iblockaccess.getBlockId(i, j, k) != blockID;
        }
    }
    */

    /*
    @Override
    protected ItemStack createStackedBlock(int i)
    {
        return new ItemStack(mod_RedstoneBlock.RedstoneStepone.blockID, 1, i);
    }
    */

    public int getRenderType()
    {
        return Mod_RSB_Core.ModelID;
    }

    public boolean canConnectFenceTo(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockId(i, j, k);
        int m = iblockaccess.getBlockMetadata(i, j, k);

        if ((l == blockID && m == 2) || l == Block.fenceGate.blockID)
        {
            return true;
        }

        Block block = Block.blocksList[l];

        if (block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock())
        {
            return block.blockMaterial != Material.pumpkin;
        }
        else
        {
            return false;
        }
    }

    public final boolean canThisPaneConnectToThisBlockID(int i)
    {
        return Block.opaqueCubeLookup[i] || i == blockID || i == Block.glass.blockID;
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 13; ++var4)
        {
        	if(var4 == 1 || var4 == 2 || var4 == 3 || var4 == 7)
        	{

                par3List.add(new ItemStack(par1, 1, var4));
        	}
        }
    }
}