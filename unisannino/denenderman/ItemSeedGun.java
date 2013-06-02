package unisannino.denenderman;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSeedGun extends ItemBow
{
	private boolean cansht;
	private int gunTick;
	private int countFire;
	@SideOnly(Side.CLIENT)
	private Minecraft mc;


    public ItemSeedGun(int par1)
    {
        super(par1);
        maxStackSize = 1;
        setMaxDamage(288);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
    	this.stopShooting(par1ItemStack, par3EntityPlayer);
    }

    @Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean flag)
	{

    	if(par3Entity != null && par3Entity instanceof EntityPlayer)
    	{
			EntityPlayer player = (EntityPlayer)par3Entity;

			if(par2World.isRemote)
			{
				if (par1ItemStack != null && player.isUsingItem() && player.getItemInUse() == par1ItemStack && player.getItemInUseDuration() % 3 == 0)
				{
					this.cansht = true;
				}
			}

			if(this.cansht)
			{
				if(gunTick % 3 == 0)
				{
					shoot(par1ItemStack, par2World, player);
				}
				else if(this.gunTick > 100)
				{
					this.gunTick = 0;
				}

				gunTick++;
			}

			/*
			if (this.cansht)
			{

				if((par1ItemStack.getItemDamage() + this.countFire) >= this.getMaxDamage())
				{
					this.countFire = this.getMaxDamage();

					this.stopShooting(par1ItemStack, player);

				}

				if(par2World.isRemote)
				{
					mc = FMLClientHandler.instance().getClient();

					if(!mc.gameSettings.keyBindUseItem.pressed || player.isDead)
					{
						this.stopShooting(par1ItemStack, player);

					}
				}

				if(gunTick % 3 == 0)
				{
					shoot(par1ItemStack, par2World, player);
				}
				else if(this.gunTick > 100)
				{
					this.gunTick = 0;
				}

				gunTick++;

			}
			*/


    	}

		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, flag);
	}

    private void shoot(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (this.isInfinity(par1ItemStack, par3EntityPlayer) || this.hasBullet(par3EntityPlayer))
        {
        	spawnNomalBullets(par1ItemStack, par2World, par3EntityPlayer);
        }
    }

    private void spawnNomalBullets(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EntitySeedBullet seeds = new EntitySeedBullet(par2World, par3EntityPlayer);

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
        int j1 = 0;

        if (j > 0)
        {
        	j1 = (int)(j * 0.5 + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

        if (k > 0)
        {
            seeds.setBulletKB(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
        {
            seeds.setFire(100);
        }

        this.countFire++;

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F));

        if (!this.isInfinity(par1ItemStack, par3EntityPlayer) )
        {
        	if(this.hasBullet(par3EntityPlayer))
        	{
        		seeds.setBulletType(this.searchBullets(par3EntityPlayer));
                par3EntityPlayer.inventory.consumeInventoryItem(Mod_DenEnderman_Core.seedBullet.itemID);
        	}
        }
        else
        {
        	if(this.hasBullet(par3EntityPlayer))
        	{
        		seeds.setBulletType(this.searchBullets(par3EntityPlayer));
        	}
        }

        seeds.setBulletDamage(seeds.type2Damage[seeds.getBulletType()] + j1);

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(seeds);
        }
    }

    private void stopShooting(ItemStack items, EntityPlayer player)
    {
    	this.cansht = false;

    	items.damageItem(this.countFire, player);

    	this.gunTick = 0;
    	this.countFire = 0;
    }

    private boolean isInfinity(ItemStack items, EntityPlayer players)
    {
    	return EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, items) > 0 || players.capabilities.isCreativeMode;
    }

    private boolean hasBullet(EntityPlayer players)
    {
    	return players.inventory.hasItem(Mod_DenEnderman_Core.seedBullet.itemID);
    }

    private int searchBullets(EntityPlayer players)
    {
		ItemStack items;
		int itemdmg = 1;

		for(int i = 0; i  < players.inventory.mainInventory.length;i++)
		{
			 items = players.inventory.getStackInSlot(i);
			 if(items != null && items.itemID == Mod_DenEnderman_Core.seedBullet.itemID)
			 {
				 itemdmg = items.getItemDamage();
				 break;
			 }
		}

		return itemdmg;
    }


    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
    	this.cansht = true;
    	par2World.playAuxSFX(1002, (int)par3EntityPlayer.posX, (int)par3EntityPlayer.posY, (int)par3EntityPlayer.posZ, 0);
        return par1ItemStack;
    }

    @Override
    public boolean isFull3D()
    {
        return true;
    }

	@Override
	public void registerIcons(IconRegister icoreg)
	{
		this.itemIcon = icoreg.registerIcon("denender:seedgun");
	}
}
