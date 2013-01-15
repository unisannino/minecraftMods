package unisannino.denenderman;

import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTwardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityDenEnderman extends EntityFarmers
{
    private EntityPlayer player;
    private int teleportDelay;
    private int field_35185_e;
    public int HeadID;

    private int stackamount;
	private int randomTickDivider;
	private DEHome homeObj;

	@SideOnly(Side.CLIENT)
    protected boolean hervestSeed;

    public EntityDenEnderman(World world)
    {
        super(world);
        teleportDelay = 0;
        field_35185_e = 0;

        if (Mod_DenEnderman_Core.onepiece)
        {
            texture = "/denender/denendermanMonkey.png";
        }
        else
        {
            texture = "/denender/denenderman.png";
        }

        moveSpeed = 0.28F;
        setSize(0.6F, 2.9F);
        stepHeight = 1.0F;

        isJumping =false;

        favoriteItem = Mod_DenEnderman_Core.lavender.blockID;
        this.likeItem = Mod_DenEnderman_Core.lavender.blockID;
        this.funcItem = Item.potato.itemID;

        setcanPickup(0, Item.wheat.itemID);
        setcanPickup(1, Item.seeds.itemID);
        setcanPickup(2, Item.melon.itemID);
        setcanPickup(3, Block.pumpkin.blockID);
        setcanPickup(4, Item.melonSeeds.itemID);
        setcanPickup(5, Item.pumpkinSeeds.itemID);
        setcanPickup(6, Mod_DenEnderman_Core.lavender.blockID);

        setcantPutItem(0, Item.seeds.itemID);
        setcantPutItem(1, Item.dyePowder.itemID);

        this.randomTickDivider = 0;
        this.hervestSeed = false;

        this.getNavigator().setBreakDoors(true);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(0, new EntityAIPlantAndHervestCrops(this));
        this.tasks.addTask(0, new EntityAIPutDEBlock(this));
        this.tasks.addTask(1, new EntityAITempt(this, this.moveSpeed, this.likeItem, false));
        this.tasks.addTask(1, this.aiSit);
        this.tasks.addTask(2, new EntityAINearestTargetItems(this, this.moveSpeed));
        this.tasks.addTask(2, new EntityAIMoveCrops(this, this.moveSpeed));
        this.tasks.addTask(2, new EntityAIMoveIngates(this));
        this.tasks.addTask(2, new EntityAIMoveDEBlock(this, this.moveSpeed));
        this.tasks.addTask(3, new EntityAIRestrictOpenGate(this));
        this.tasks.addTask(4, new EntityAIOpenGate(this, true));
        this.tasks.addTask(4, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
        this.tasks.addTask(6, new EntityAIWander(this, this.moveSpeed));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, new Byte((byte)0));
    }


    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    @Override
    protected void updateAITick()
    {
        if (--this.randomTickDivider <= 0)
        {
        	this.homecollection.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.homeObj = this.homecollection.findNearestDEHome(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);

            if (this.homeObj == null)
            {
                this.detachHome();
            }
            else
            {
                ChunkCoordinates var1 = this.homeObj.getCenter();
                this.setHomeArea(var1.posX, var1.posY, var1.posZ, this.homeObj.getVillageRadius());
            }
        }

        super.updateAITick();
    }

    @Override
    protected void addConfigPickupItems()
    {
    	String s[] = Mod_DenEnderman_Core.canPickupDE.split(",");
    	for(int j = 0; j < s.length; j++)
    	{
    		int id = Integer.parseInt(s[j].trim());
    		setcanPickup(id);
    	}
    }

    @Override
    public int getMaxHealth()
    {
        return 20;
    }

    @Override
    public int getBrightnessForRender(float f)
    {
        return super.getBrightnessForRender(f);
    }

    @Override
    public float getBrightness(float f)
    {
        return super.getBrightness(f);
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        for (int k = 0; k < 2; k++)
        {
            worldObj.spawnParticle("reddust", posX + (rand.nextDouble() - 0.5D) * width, (posY + rand.nextDouble() * height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * width, 2.0D, 200.0D, 00D);
        }
    }

    @Override
    protected boolean checkItemID(int i)
    {
    	if(i > 0)
    	{
			Item item = Item.itemsList[i];
			if(item instanceof ItemFood || item instanceof ItemSeeds)
			{
				return true;
			}else
			if(item instanceof ItemBlock)
			{
    			Block block = Block.blocksList[i];
    			if(block instanceof BlockFlower)
    			{
    				return true;
    			}
			}
    	}
    	return super.checkItemID(i);
    }

    @Override
    public boolean interact(EntityPlayer par1Entityplayer)
    {
        ItemStack itemstack = par1Entityplayer.inventory.getCurrentItem();

        if (itemstack != null && itemstack.itemID == this.funcItem && par1Entityplayer.username.equalsIgnoreCase(this.getOwnerName()))
        {
            if (!par1Entityplayer.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            if (itemstack.stackSize <= 0)
            {
                par1Entityplayer.inventory.setInventorySlotContents(par1Entityplayer.inventory.currentItem, null);
            }

            this.setFaceAngry(!this.getFaceAngry());
        }

        return super.interact(par1Entityplayer);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Head", HeadID);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        HeadID = nbttagcompound.getInteger("Head");
    }

    @Override
    protected void jump()
    {
    	if(isInWater() || handleLavaMovement())
    	{
    		super.jump();
    	}
    }

    @Override
    protected String getLivingSound()
    {
        if (!Mod_DenEnderman_Core.crying)
        {
            return null;
        }
        else
        {
            return "mob.endermen.idle";
        }
    }

    @Override
    protected String getHurtSound()
    {
        if (!Mod_DenEnderman_Core.crying)
        {
            return null;
        }
        else
        {
            return "mob.endermen.hit";
        }
    }

    @Override
    protected String getDeathSound()
    {
        if (!Mod_DenEnderman_Core.crying)
        {
            return null;
        }
        else
        {
            return "mob.endermen.death";
        }
    }
    @Override
    protected int getDropItemId()
    {
        return Mod_DenEnderman_Core.denEnderPearl.itemID;
    }

    /*
     * EntityAIPAHCropsに移すかも
     */
    protected boolean canHervest(World world, int i, int j, int k)
    {
		int cropsid = world.getBlockId(i, j, k);
		int farmlandid = world.getBlockId(i, j - 1, k);
		int meta = world.getBlockMetadata(i, j, k);
        Block crops = Block.blocksList[cropsid];

		if(farmlandid == Block.tilledField.blockID)
		{
			if((cropsid == Block.crops.blockID && meta ==7) || cropsid == Block.pumpkin.blockID || cropsid == Block.melon.blockID)
			{
				return true;
			}else if(crops instanceof BlockCrops && crops.idDropped(meta, rand, 0) > 0)
            {
            	return true;
            }else
            {
            	if(Mod_DenEnderman_Core.toggleXies)
            	{
            		/*
                    try
                    {
                    	if(mod_XieClient.class.getClass() != null)
                    	{
                        	if(crops instanceof net.minecraft.src.xie.blocks.XieBlockPlant)
                        	{
                        		net.minecraft.src.xie.blocks.XieBlockPlant xiecrops = (net.minecraft.src.xie.blocks.XieBlockPlant)crops;
                    			if(ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops") != null)
                    			{
                    				int metaxie = world.getBlockMetadata(i, j, k);
                    				if(metaxie == xiecrops.maxGrowth)
                    				{
                    					return true;
                    				}
                    			}
                        	}
                    	}
                    }catch(Exception e)
                    {
                    }
                    */
            	}
            }

		}else if(crops instanceof BlockCrops && meta == 7)
		{
			BlockCrops plant = (BlockCrops)Block.blocksList[cropsid];
			//if(plant.canThisPlantGrowOnThisBlockID(l1))
			if(Block.blocksList[farmlandid].isFertile(world, i, j, k))
			{
				return true;
			}
		}
		return false;
    }

    private boolean isPlantGrowBlocks(int blockId)
    {
		try
		{
			Object result;
			Method crop = Class.forName("BlockCrops").getMethod("canThisPlantGrowOnThisBlockID", new Class[]{Integer.class});
			result = crop.invoke(null, new Object[] { blockId });
			return (Boolean) result;

		}catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			return false;

		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
    }

    @SideOnly(Side.CLIENT)
    public boolean getFaceAngry()
    {
        return this.dataWatcher.getWatchableObjectByte(18) > 0;
    }

    public void setFaceAngry(boolean par1)
    {
        this.dataWatcher.updateObject(18, Byte.valueOf((byte)(par1 ? 1 : 0)));
    }

}
