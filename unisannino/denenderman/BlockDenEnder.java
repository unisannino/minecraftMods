package unisannino.denenderman;

import java.util.Random;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDenEnder extends BlockContainer
{

    private Random random;
    private IInventory playerinvently;
    private boolean spawncheck[];

    public BlockDenEnder(int i)
    {
        super(i, Material.rock);
        random = new Random();
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
    {
        if (par6ItemStack.hasDisplayName())
        {
            ((TileEntityDenEnder)par1World.getBlockTileEntity(par2, par3, par4)).setInvName(par6ItemStack.getDisplayName());
        }
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityDenEnder deblock = (TileEntityDenEnder)par1World.getBlockTileEntity(par2, par3, par4);
        label0:

        for (int l = 0; l < deblock.getSizeInventory(); l++)
        {
            ItemStack itemstack = deblock.getStackInSlot(l);

            if (itemstack == null)
            {
                continue;
            }

            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;

            do
            {
                if (itemstack.stackSize <= 0)
                {
                    continue label0;
                }

                int i1 = random.nextInt(21) + 10;

                if (i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }

                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)random.nextGaussian() * f3;
                entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)random.nextGaussian() * f3;
                par1World.spawnEntityInWorld(entityitem);
            }
            while (true);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityDenEnder();
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        TileEntityDenEnder tileentitydenender = (TileEntityDenEnder)world.getBlockTileEntity(i, j, k);

        if (tileentitydenender == null)
        {
            return true;
        }

        if (world.isRemote)
        {
            return true;
        }
        else
        {
        	par5EntityPlayer.openGui(Mod_DenEnderman_Core.instance, 2, world, i, j, k);
            return true;
        }
    }

    public void registerIcons(IconRegister icoreg)
    {
    	this.blockIcon = icoreg.registerIcon("denender:dblock");
    }

}
