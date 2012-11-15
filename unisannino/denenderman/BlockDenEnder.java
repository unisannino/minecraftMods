package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.*;

public class BlockDenEnder extends BlockContainer
{
    public BlockDenEnder(int i)
    {
        super(i, Material.sponge);
        random = new Random();
        setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setRequiresSelfNotify();
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        //int check = world.getBlockId(i, j + 1, k);
        if (!world.isBlockNormalCube(i, j + 1, k))
        {
            return true;
        }

        return false;
    }

    @Override
    public int getBlockTextureFromSide(int i)
    {
        if (i == 1)
        {
            return 158;
        }

        if (i == 0)
        {
            return 159 + 16;
        }
        else
        {
            return 158;
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        InsertDenenderPearl(world, i, j, k);
    }

    @Override
    public int tickRate()
    {
        return 100;
    }

    private void InsertDenenderPearl(World world, int i, int j, int k)
    {
        ItemStack pearl = new ItemStack(Mod_DenEnderman_Core.denEnderPearl, 1);
        TileEntityDenEnder myDBlock = (TileEntityDenEnder) world.getBlockTileEntity(i, j, k);

        if (random.nextInt(2000) == 0)
        {
            if (myDBlock != null)
            {
                for (int empty = 0; empty < myDBlock.getSizeInventory(); empty++)
                {
                    ItemStack itemstackDE = myDBlock.getStackInSlot(empty);

                    if (itemstackDE == null)
                    {
                        myDBlock.setInventorySlotContents(empty, pearl.copy());
                        //System.out.println("Lucky! DenEnderPearl!");
                        break;
                    }
                }
            }
        }
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityDenEnder deblock = (TileEntityDenEnder)par1World.getBlockTileEntity(par2, par3, par4);
        System.out.println(deblock.hashCode());
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
                EntityItem entityitem = new EntityItem(par1World, (float)par2 + f, (float)par3 + f1, (float)par4 + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
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
            //ModLoader.openGUI(par5EntityPlayer, new GuiDenEnderBlock(par5EntityPlayer.inventory, tileentitydenender));
        	par5EntityPlayer.openGui(Mod_DenEnderman_Core.instance, 2, world, i, j, k);
            return true;
        }
    }

    private Random random;
    private IInventory playerinvently;
    private boolean spawncheck[];
}
