package unisannino.denenderman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.client.Minecraft;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

public class EntityFarmers extends EntityAnimal
{
    protected int heartpop;
    private int lovePoint;

    protected Minecraft mc = FMLClientHandler.instance().getClient();
    protected InventoryFarmers inventory;
    protected PathEntity pathToCrop;
    protected int favoriteItem;
    protected int likeItem;
    private List<Integer> canpickup;
    protected List<TileEntityDenEnder> serchedDE;
    private List<Integer> cantputItem;

    public IInventory myDBlock;
    public TileEntity myTile;
    @SideOnly(Side.CLIENT)
    protected boolean sayLogs;
    @SideOnly(Side.CLIENT)
    public boolean findItems;

    protected DEHomeCollection homecollection;
	protected DEHomePosition homePos;

	protected List<TileEntityDenEnder> deblockList;

    public EntityFarmers(World par1World)
    {
        super(par1World);
        this.inventory = new InventoryFarmers(this);
        this.inventory.currentItem = 0;
        this.pathToCrop = null;
        this.favoriteItem = Item.wheat.shiftedIndex;
        this.likeItem = Item.wheat.shiftedIndex;
        this.serchedDE = new ArrayList<TileEntityDenEnder>();
        this.canpickup = new ArrayList<Integer>();
        this.cantputItem = new ArrayList<Integer>();
        this.sayLogs = false;
        this.findItems = false;
        this.homecollection = new DEHomeCollection(this.worldObj);
        this.deblockList = new ArrayList<TileEntityDenEnder>();

        addConfigPickupItems();
    }

    protected void addConfigPickupItems()
    {
    }

    @Override
    public EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
    {
        return null;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer par1EntityPlayer)
    {
        return 0;
    }

    protected boolean hasPathCrops()
    {
        return pathToCrop != null;
    }

    /*
     * 微妙
     */
    public boolean canEntityItemBeSeen(Entity entity)
    {
        return worldObj.rayTraceBlocks(this.worldObj.func_82732_R().getVecFromPool(posX, posY + (double)getEyeHeight(), posZ), this.worldObj.func_82732_R().getVecFromPool(entity.posX, entity.posY + ((entity.boundingBox.minY - entity.boundingBox.minY) / 2), entity.posZ)) == null;
    }

    @Override
    protected boolean canDespawn()
    {
        return false;
    }

    /*
    @Override
    public void setPathToEntity(PathEntity par1PathEntity)
    {
    	super.setPathToEntity(par1PathEntity);

    }
    */

    /*
    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }
    */
    @Override
    public void onUpdate()
    {
        this.homecollection.tick();
        super.onUpdate();
    }


    @Override
    public void onLivingUpdate()
    {
        if (!worldObj.isRemote)
        {
        	if(entityToAttack != null)
        	{
        		faceEntity(entityToAttack, 100F, 100F);
        	}
            pickupitem();

            if (inventory.getFirstEmptyStack() == -1)
            {
                //searchDBlock();
            }

            if (heartpop > 0)
            {
                heartpop--;
                String s = "heart";

                if (heartpop % 10 == 0)
                {
                    double d = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;
                    double d2 = rand.nextGaussian() * 0.02D;
                    worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
                }
            }
        }

        Mod_DenEnderman_Packet.getPacket(this);
        super.onLivingUpdate();
    }

    private void pickupitem()
    {
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(1.0D, 0.0D, 1.0D));

        if (list != null)
        {
            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);

                if (!entity.isDead && entity instanceof EntityItem && this.health > 0)
                {
                    int i1 = 0;
                    EntityItem pickupitem = (EntityItem)entity;

                    if (checkItemID(pickupitem.item.itemID) || this instanceof EntityUniuni)
                    {
                        i1 = pickupitem.item.stackSize;

                        if (!(pickupitem.delayBeforeCanPickup == 0 && inventory.addItemStackToInventory(pickupitem.item)))
                        {
                            return;
                        }

                        if (pickupitem.item.stackSize <= 0)
                        {
                            pickupitem.setDead();
                        }

                        worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, entity, this, 0.1F));
                    }
                }
            }
        }
    }

    protected boolean checkItemID(int i)
    {
        for (int j = 0; j < canpickup.size(); j++)
        {
            if ((Integer)canpickup.get(j) == i)
            {
                //System.out.println(i+" is can pick up");
                return true;
            }
        }
        //System.out.println(i+" is cannot pick up");
        return false;
    }

    protected boolean checkItemCanPut(int i)
    {
        for (int j = 0; j < cantputItem.size(); j++)
        {
            if ((Integer)cantputItem.get(j) == i)
            {
                return false;
            }
        }

        return true;
    }

    protected void setcanPickup(int i, int j)
    {
        canpickup.add(i, j);
    }

    protected void setcanPickup(int i)
    {
        canpickup.add(i);
    }

    protected void setcantPutItem(int i, int j)
    {
        cantputItem.add(i, j);
    }

    /*
    protected void updateEntityActionState()
    {
        super.updateEntityActionState();

        if (entityToAttack instanceof EntityItem && inventory.getFirstEmptyStack() == -1)
        {
            entityToAttack = null;
        }
    }
    */

    /*
    @Override
    protected Entity findPlayerToAttack()
    {
        if (inventory.getFirstEmptyStack() > -1)
        {
            List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(8D, 2D, 8D));

            if (!list.isEmpty())
            {
                int i = rand.nextInt(list.size());
                EntityItem entityitem = (EntityItem) list.get(i);

                if (!entityitem.isDead && entityitem.onGround && entityitem.delayBeforeCanPickup <= 0 && canEntityItemBeSeen(entityitem))
                {
                    if (checkItemID(entityitem.item.itemID) || this instanceof EntityUniuni)
                    {
                        return entityitem;
                    }
                }
            }
        }

        return super.findPlayerToAttack();
    }
    */

    @Override
    public int getMaxHealth()
    {
        return 0;
    }

    @Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        if(!this.worldObj.isRemote)
        {
            inventory.dropAllItems();
        }
    }

    @Override
    protected void dropFewItems(boolean flag, int i)
    {
        dropItem(getDropItemId(), 1);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
        nbt.setInteger("Friend", lovePoint);

        //おうちの座標
        if(this.homePos != null)
        {
            nbt.setDouble("homePosX", this.homePos.posX);
            nbt.setDouble("homePosY", this.homePos.posY);
            nbt.setDouble("homePosZ", this.homePos.posZ);
            nbt.setBoolean("isinHome", this.homePos.isinHome);
        }
        /*
    	NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < canpickup.size(); i++)
        {
            if (canpickup.get(i) > -1)
            {
            	NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setInteger("Slot", i);
                nbttagcompound1.setInteger("pickup", canpickup.get(i));
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("PickUpList", nbttaglist);
        */
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Inventory");
        inventory.readFromNBT(nbttaglist);
        lovePoint = nbt.getInteger("Friend");

        double x = nbt.getDouble("homePosX");
        double y = nbt.getDouble("homePosY");
        double z = nbt.getDouble("homePosZ");
        this.setDEHomePos(x, y, z);

        if(this.homePos != null)
        {
        	this.homePos.isinHome = nbt.getBoolean("isinHome");
        }

        /*
        NBTTagList nbttaglist1 = nbttagcompound.getTagList("PickUpList");

        for (int i = 0; i < nbttaglist1.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist1.tagAt(i);
            int j = nbttagcompound1.getInteger("Slot");
            int k = nbttagcompound1.getInteger("pickup");

            if(k > -1)
            {
            	continue;
            }
            if(j >= 0)
            {
                canpickup.add(j, k);
            }
        }
        */
    }

    @Override
    public boolean isWheat(ItemStack par1ItemStack)
    {
        return par1ItemStack.itemID == likeItem;
    }

    protected boolean isFavorite(ItemStack par1ItemStack)
    {
        return par1ItemStack.itemID == favoriteItem;
    }

    @Override
    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (itemstack != null && isFavorite(itemstack) && getGrowingAge() == 0)
        {
            itemstack.stackSize--;

            if (itemstack.stackSize <= 0)
            {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
            }

            heartpop = 600;
            heal(1);

            for (int i = 0; i < 7; i++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("heart", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }

            if (lovePoint < 100)
            {
                lovePoint++;
                if(!this.worldObj.isRemote)
                {
                    String s = new StringBuilder(getEntityString()).append("Now LovePoint: ").append(lovePoint).toString();
                    entityplayer.addChatMessage(s);
                }
                return true;
            }

            if(lovePoint + rand.nextInt(199) > 200)
            {
            	if(entityplayer.inventory.addItemStackToInventory(new ItemStack(Block.blockDiamond, 1)))
            	{
                    if(!this.worldObj.isRemote)
                    {
                        String s = new StringBuilder(getEntityString()).append(" has sent a present for you!").toString();
                        entityplayer.addChatMessage(s);
                    }
                    lovePoint = 0;
            	}
            }

            return true;
        }
        else if (itemstack != null && itemstack.itemID == Item.hoeDiamond.shiftedIndex)
        {
            //ModLoader.openGUI(entityplayer, new GuiChest(entityplayer.inventory,  inventory));
        	entityplayer.openGui(Mod_DenEnderman_Core.instance, this.entityId, this.worldObj, (int)this.posX, (int)this.posY, (int)this.posZ);
        	this.inventory.getEntityId();
            return true;
        }
        else
        {
            return false;
        }
    }

    public void dropFarmerItemWithRandomChoice(ItemStack itemstack, boolean flag)
    {
        if (itemstack == null)
        {
            return;
        }

        EntityItem entityitem = new EntityItem(worldObj, posX, (posY - 0.3D) + (double)getEyeHeight(), posZ, itemstack);
        entityitem.delayBeforeCanPickup = 40;
        joinEntityItemWithWorld(entityitem);
    }

    protected void joinEntityItemWithWorld(EntityItem entityitem)
    {
        worldObj.spawnEntityInWorld(entityitem);
    }

    public void onItemStackChanged(ItemStack itemstack)
    {
    }

    @Deprecated
    public void facetoPath(int x, int y, int z, float f, float f1)
    {
        double d = (double)x - posX;
        double d2 = (double)z - posZ;
        double d1 = (double)y - (posY + (double)getEyeHeight());

        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f2 = (float)((Math.atan2(d2, d) * 180D) / Math.PI) - 90F;
        float f3 = (float)(-((Math.atan2(d1, d3) * 180D) / Math.PI));
        rotationPitch = updateRotationF(rotationPitch, f3, f1);
        rotationYaw = updateRotationF(rotationYaw, f2, f);
    }

    @Override
    public void faceEntity(Entity par1Entity, float par2, float par3)
    {
        double d = par1Entity.posX - posX;
        double d2 = par1Entity.posZ - posZ;
        double d1;

        if (par1Entity instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)par1Entity;
            d1 = (posY + (double)getEyeHeight()) - (entityliving.posY + (double)entityliving.getEyeHeight());
        }
        else
        {
            d1 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2D - (posY + (double)getEyeHeight());
        }

        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float)((Math.atan2(d2, d) * 180D) / Math.PI) - 90F;
        float f1 = (float)(-((Math.atan2(d1, d3) * 180D) / Math.PI));
        if(par1Entity instanceof EntityItem)
        {
            rotationPitch = updateRotationF(rotationPitch, f1, par3);
        }else
            rotationPitch = -updateRotationF(rotationPitch, f1, par3);
        rotationYaw = updateRotationF(rotationYaw, f, par2);
    }

    private float updateRotationF(float par1, float par2, float par3)
    {
        float f;

        for (f = par2 - par1; f < -180F; f += 360F) { }

        for (; f >= 180F; f -= 360F) { }

        if (f > par3)
        {
            f = par3;
        }

        if (f < -par3)
        {
            f = -par3;
        }

        return par1 + f;
    }

    @Override
    protected void attackEntity(Entity entity, float f)
    {
        if (entity instanceof EntityPlayer)
        {
            if (f < 3F)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                rotationYaw = (float)((Math.atan2(d1, d) * 180D) / Math.PI) - 90F;
                hasAttacked = true;
            }

            EntityPlayer entityplayer = (EntityPlayer)entity;

            if (entityplayer.getCurrentEquippedItem() == null || !isWheat(entityplayer.getCurrentEquippedItem()))
            {
                entityToAttack = null;
            }
        }
    }

    protected boolean getchanceItems(Item item, int i, int j)
    {
        int k = rand.nextInt(i) + j;
        return k == 0 ? true : inventory.addItemStackToInventory(new ItemStack(item, k));
    }

    @Deprecated
    protected boolean getBlock(World world, int i, int j, int k)
    {
        TileEntityDenEnder tedenender = (TileEntityDenEnder)world.getBlockTileEntity(i, j, k);
        serchedDE.add(tedenender);

        if (tedenender == null)
        {
            return true;
        }

        if (world.isRemote)
        {
            return false;
        }
        else
        {
            myDBlock = (IInventory)tedenender;
            return true;
        }
    }

    @Deprecated
    protected void putDBlock()
    {
        if (myDBlock != null)
        {
            if (inventory.getFirstEmptyStack() == -1)
            {
                for (int i = 0; i < inventory.getSizeInventory(); i++)
                {
                    //System.out.println(i);
                    ItemStack itemstackF = inventory.getStackInSlot(i);
                    if (itemstackF != null)
                    {
                    	if(!checkItemCanPut(itemstackF.itemID))
                    	{
                    		continue;
                    	}
                        boolean putcrop = false;

                        for (int j = 0; j < myDBlock.getSizeInventory(); j++)
                        {
                            ItemStack itemstackDE = myDBlock.getStackInSlot(j);

                            if (itemstackDE == null)
                            {
                                myDBlock.setInventorySlotContents(j, itemstackF);
                                inventory.setInventorySlotContents(i, null);
                                putcrop = true;
                                break;
                            }
                        }

                        if (putcrop)
                        {
                            worldObj.playSoundAtEntity(this, "random.pop", 0.5F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                            String s = "Putted Inventory Items!!";
                			if(!this.worldObj.isRemote)
                			{
                    			mc.thePlayer.addChatMessage(s);
                			}
                        }
                        else
                        {
                            String s = "Oh No! Slot of DenEnderBlock is full!";
                			if(!this.worldObj.isRemote)
                			{
                    			mc.thePlayer.addChatMessage(s);
                			}
                            return;
                        }
                    }
                }
            }

            myTile = null;
            myDBlock = null;
            serchedDE.clear();
            sayLogs = false;
        }
    }

    @Deprecated
    private void searchDBlock()
    {
    	if(!sayLogs)
    	{
    		String s = new StringBuilder(getEntityString()).append(" is searching DenenderBlock...").toString();
    		if(mc.thePlayer != null)
    		{
    			if(!this.worldObj.isRemote)
    			{

        			mc.thePlayer.addChatMessage(s);
    			}
        		sayLogs = true;
    		}
    	}
		for (double checkXZ = 10; checkXZ > 0; checkXZ--)
		{
			for (int height = 4; height > -1; height--)
			{
				int i = MathHelper.floor_double((posX - (rand.nextDouble() * 9D)) + checkXZ);
				int j = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * height);
				int k = MathHelper.floor_double((posZ - (rand.nextDouble() * 9D)) + checkXZ);

				if (worldObj.getBlockId(i, j, k) == Mod_DenEnderman_Core.DenenderBlock.blockID)
				{
					pathToCrop = worldObj.getEntityPathToXYZ(this, i, j, k, 16F, true, false, false, true);
					setPathToEntity(pathToCrop);
				}
			}
		}
        for (double checkheight = 4; checkheight > 0; checkheight--)
        {
            int i = MathHelper.floor_double(posX - (rand.nextDouble() * 2D));
            int l = MathHelper.floor_double((posY - 1.0D) + rand.nextDouble() * checkheight);
            int j1 = MathHelper.floor_double(posZ - (rand.nextDouble() * 2D));

            TileEntity tileentity = worldObj.getBlockTileEntity(i, l, j1);
            //System.out.println(tileentity != null);

            if (tileentity != null && tileentity instanceof TileEntityDenEnder)
            {
                myTile = tileentity;
            }

            if (myTile != null)
            {
                if (worldObj.getBlockTileEntity(i, l, j1) == myTile)
                {
                    if (myDBlock == null)
                    {
                        getBlock(worldObj, i, l, j1);

                        if (myDBlock != null)
                        {
                        	facetoPath(i, l, j1, 100F, 100F);
                        }
                    }

                    putDBlock();
                }
                else
                {
                    myTile = null;

                    if (myDBlock != null)
                    {
                        myDBlock = null;
                        pathToCrop = null;
                    }
                }
            }
        }
    }

	public void handlePacketdata(int[] items)
	{
		if(items != null)
		{
			int i = 0;
			if(items.length < this.inventory.mainInventory.length * 3)
			{
				return;
			}

			for(int j = 0; j < this.inventory.mainInventory.length; j++)
			{
				if(items[i + 2] != 0)
				{
					ItemStack is = new ItemStack(items[i], items[i + 2], items[i + 1]);
					this.inventory.mainInventory[j] = is;
				}else
				{
					this.inventory.mainInventory[j] = null;
				}
				i += 3;
			}
		}
	}

	public int[] buildIntDataList()
	{
		int[] itemsdata = new int[this.inventory.mainInventory.length * 3];
		int i = 0;
		for(ItemStack is : this.inventory.mainInventory)
		{
			if(is != null)
			{
				itemsdata[i++] = is.itemID;
				itemsdata[i++] = is.getItemDamage();
				itemsdata[i++] = is.stackSize;
			}else
			{
				itemsdata[i++] = 0;
				itemsdata[i++] = 0;
				itemsdata[i++] = 0;
			}
		}
		return itemsdata;
	}

	public void setDEHomePos(double x, double y, double z)
	{
		this.homePos = new DEHomePosition(x, y, z);
	}

	public DEHomePosition getDEHomePos()
	{
		return this.homePos;
	}


	public String getFarmersName()
	{
		return this.getEntityName();
	}
}
