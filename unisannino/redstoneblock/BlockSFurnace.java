package unisannino.redstoneblock;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSFurnace extends BlockContainer
{
    protected BlockSFurnace(int i/*, boolean flag*/)
    {
        super(i, Material.rock);
        furnaceRand = new Random();
        blockIndexInTexture = 45;
        isActive = false;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return Mod_RSB_Core.RedFurnaceIdle.blockID;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        this.setDefaultDirection(world, i, j, k);
    }

    private void setDefaultDirection(World world, int i, int j, int k)
    {
        if (world.isRemote)
        {
            return;
        }

        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;

        if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
        {
            byte0 = 3;
        }

        if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
        {
            byte0 = 2;
        }

        if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
        {
            byte0 = 5;
        }

        if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
        {
            byte0 = 4;
        }

        world.setBlockMetadataWithNotify(i, j, k, byte0);
    }

    @Override
    public int getRenderColor(int i)
    {
    	/*
        if (Mod_RSB_Core.ChooseTexturesRedFurnace)
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
        if (Mod_RSB_Core.ChooseTexturesRedFurnace)
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
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        TileEntity tileentity = iblockaccess.getBlockTileEntity(i, j, k);
        TileEntitySFurnace sfurn = (TileEntitySFurnace)tileentity;

        //if (Mod_RSB_Core.ChooseTexturesRedFurnace)
        {
            if (l == 1)
            {
                return blockIndexInTexture + 17;
            }

            if (l == 0)
            {
                return blockIndexInTexture + 17;
            }

            int i1 = iblockaccess.getBlockMetadata(i, j, k);

            if (l != i1)
            {
                return blockIndexInTexture;
            }

            if (sfurn.isBurning())
            {
                return blockIndexInTexture + 16;
            }
            else
            {
                return blockIndexInTexture - 1;
            }
        }
        /*
        else
        {
            if (l == 1)
            {
                return Mod_RSB_Core.furnacehuta;
            }

            if (l == 0)
            {
                return Mod_RSB_Core.furnacehuta;
            }

            int i1 = iblockaccess.getBlockMetadata(i, j, k);

            if (l != i1)
            {
                return Mod_RSB_Core.furnaceyoko;
            }

            if (sfurn.isBurning())
            {
                return Mod_RSB_Core.furnaceshomenfire;
            }
            else
            {
                return Mod_RSB_Core.furnaceshomen;
            }
        }
        */
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        int l = world.getBlockMetadata(i, j, k);
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        TileEntitySFurnace sfurn = (TileEntitySFurnace)tileentity;

        if (!sfurn.isBurning())
        {
            return;
        }

        float f = i + 0.5F;
        float f1 = j + 0.0F + (random.nextFloat() * 6F) / 16F;
        float f2 = k + 0.5F;
        float f3 = 0.52F;
        float f4 = random.nextFloat() * 0.6F - 0.3F;

        if (l == 4)
        {
            world.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 5)
        {
            world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 2)
        {
            world.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 3)
        {
            world.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public int getBlockTextureFromSide(int i)
    {
        //if (Mod_RSB_Core.ChooseTexturesRedFurnace)

        {
            if (i == 1)
            {
                return blockIndexInTexture + 17;
            }

            if (i == 0)
            {
                return blockIndexInTexture + 17;
            }

            if (i == 3)
            {
                return blockIndexInTexture - 1;
            }
            else
            {
                return blockIndexInTexture;
            }
        }
        /*
        else
        {
            if (i == 1)
            {
                return Mod_RSB_Core.furnacehuta;
            }

            if (i == 0)
            {
                return Mod_RSB_Core.furnacehuta;
            }

            if (i == 3)
            {
                return Mod_RSB_Core.furnaceshomen;
            }
            else
            {
                return Mod_RSB_Core.furnaceyoko;
            }
        }
        */
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntitySFurnace tileentitysfurnace = (TileEntitySFurnace)par1World.getBlockTileEntity(par2, par3, par4);
            par5EntityPlayer.openGui(Mod_RSB_Core.instance, 1, par1World, par2, par3, par4);
            //ModLoader.openGUI(par5EntityPlayer, new GuiSFurnace(par5EntityPlayer.inventory, tileentitysfurnace));
            return true;
        }
    }

    private void displayGUISFurnace(TileEntitySFurnace tileentitysfurnace)
    {
    }

    public static void updateSFurnaceBlockState(boolean flag, World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        keepFurnaceInventory = true;
        /*
        if(flag)
        {
        	//isActive = true;
            //world.setBlockWithNotify(i, j, k, mod_SFurnace.SFurnaceActive.blockID);
        } else
        {
        	//isActive = false;
        	//playtimer(world, i, j + 1 ,k);
            //world.setBlockWithNotify(i, j, k, mod_SFurnace.SFurnaceIdle.blockID);
        }
        */
        keepFurnaceInventory = false;
        world.notifyBlockChange(i, j, k, Mod_RSB_Core.RedFurnaceIdle.blockID);
        //world.setBlockMetadataWithNotify(i, j, k, l);
        tileentity.validate();
        world.setBlockTileEntity(i, j, k, tileentity);
    }

    @Override
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntitySFurnace();
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

        if (l == 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, 2);
        }

        if (l == 1)
        {
            world.setBlockMetadataWithNotify(i, j, k, 5);
        }

        if (l == 2)
        {
            world.setBlockMetadataWithNotify(i, j, k, 3);
        }

        if (l == 3)
        {
            world.setBlockMetadataWithNotify(i, j, k, 4);
        }
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        if (!keepFurnaceInventory)
        {
            TileEntitySFurnace tileentitysfurnace = (TileEntitySFurnace)par1World.getBlockTileEntity(par2, par3, par4);
            label0:

            for (int l = 0; l < tileentitysfurnace.getSizeInventory(); l++)
            {
                ItemStack itemstack = tileentitysfurnace.getStackInSlot(l);

                if (itemstack == null)
                {
                    continue;
                }

                float f = furnaceRand.nextFloat() * 0.8F + 0.1F;
                float f1 = furnaceRand.nextFloat() * 0.8F + 0.1F;
                float f2 = furnaceRand.nextFloat() * 0.8F + 0.1F;

                do
                {
                    if (itemstack.stackSize <= 0)
                    {
                        continue label0;
                    }

                    int i1 = furnaceRand.nextInt(21) + 10;

                    if (i1 > itemstack.stackSize)
                    {
                        i1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= i1;
                    EntityItem entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                    float f3 = 0.05F;
                    entityitem.motionX = (float)furnaceRand.nextGaussian() * f3;
                    entityitem.motionY = (float)furnaceRand.nextGaussian() * f3 + 0.2F;
                    entityitem.motionZ = (float)furnaceRand.nextGaussian() * f3;
                    par1World.spawnEntityInWorld(entityitem);
                }
                while (true);
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    /*    public  static boolean playtimer(World world, int i, int j, int k)
        {
        	TileEntityNote tileentitynote = (TileEntityNote)world.getBlockTileEntity(i, j, k);
            if(world.multiplayerWorld)
            {
            	return true;
            }else
            if(tileentitynote != null)
            {
            	world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "note.pling", 3F, 10F);
            }
            return true;
        }*/

    private Random furnaceRand;
    public boolean isActive;
    private int meta;
    private static boolean keepFurnaceInventory = false;
}
