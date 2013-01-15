package unisannino.denenderman;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSeedGun extends ItemBow
{
    public ItemSeedGun(int par1)
    {
        super(par1);
        maxStackSize = 1;
        setMaxDamage(384);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
    }

    @Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean flag)
	{
    	if(par3Entity != null && par3Entity instanceof EntityPlayer)
    	{
			EntityPlayer player = (EntityPlayer)par3Entity;
			if (par1ItemStack != null && player.getItemInUse() == par1ItemStack && player.getItemInUseDuration() % 3 == 0)
			{
				shoot(par1ItemStack, par2World, player);
			}
    	}
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, flag);
	}

    private void shoot(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        boolean flag = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
        boolean flag1 = par3EntityPlayer.inventory.hasItem(Item.seeds.itemID);
        boolean flag2 = par3EntityPlayer.inventory.hasItem(Item.melonSeeds.itemID);
        boolean flag3 = par3EntityPlayer.inventory.hasItem(Item.pumpkinSeeds.itemID);

        if (flag || flag1 || flag2 || flag3)
        {
        	spawnNomalBullets(par1ItemStack, par2World, par3EntityPlayer);
        }
    }

    private void spawnNomalBullets(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        boolean flag = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
        boolean flag1 = par3EntityPlayer.inventory.hasItem(Item.seeds.itemID);
        boolean flag2 = par3EntityPlayer.inventory.hasItem(Item.melonSeeds.itemID);
        boolean flag3 = par3EntityPlayer.inventory.hasItem(Item.pumpkinSeeds.itemID);

        EntitySeedBullet seeds = new EntitySeedBullet(par2World, par3EntityPlayer);
        seeds.damage = 1;

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);

        if (j > 0)
        {
            seeds.damage = seeds.damage + (int)(j * 0.5 + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

        if (k > 0)
        {
            seeds.knockback = k;
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
        {
            seeds.setFire(100);
        }

        par1ItemStack.damageItem(1, par3EntityPlayer);
        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F));

        if (!flag)
        {
        	if(flag1)
        	{
        		seeds.type = 1;
                par3EntityPlayer.inventory.consumeInventoryItem(Item.seeds.itemID);
        	}else
        	if(flag2)
        	{
        		seeds.type = 2;
        		seeds.damage++;
                par3EntityPlayer.inventory.consumeInventoryItem(Item.melonSeeds.itemID);
        	}else
        	if(flag3)
        	{
        		seeds.type = 3;
        		seeds.damage += 2;
                par3EntityPlayer.inventory.consumeInventoryItem(Item.pumpkinSeeds.itemID);
        	}
        }else
        {
        	if(flag1)
        	{
        		seeds.type = 1;
        	}else
        	if(flag2)
        	{
        		seeds.damage++;
        		seeds.type = 2;
        	}else
        	if(flag3)
        	{
        		seeds.damage += 2;
        		seeds.type = 2;
        	}else
        		seeds.type = 1;
        }

        if (!par2World.isRemote)
        {
        	Mod_DenEnderman_Packet.getPacket(seeds);
            par2World.spawnEntityInWorld(seeds);
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
    	par2World.playAuxSFX(1002, (int)par3EntityPlayer.posX, (int)par3EntityPlayer.posY, (int)par3EntityPlayer.posZ, 0);
        return par1ItemStack;
    }

    @Override
    public boolean isFull3D()
    {
        return true;
    }

	@Override
	public String getTextureFile()
	{
		return "/denender/itemsde.png";
	}
}
