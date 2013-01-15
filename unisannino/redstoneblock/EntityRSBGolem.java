package unisannino.redstoneblock;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityRSBGolem extends EntityIronGolem
{
    private int field_48120_c;
    private int field_48118_d;
    private int waittime;

    protected EntityAIStayGolem sit;

    public EntityRSBGolem(World par1World)
    {
        super(par1World);
        sit = new EntityAIStayGolem(this);
        texture = "/mob/villager_golem.png";
        setSize(1.4F, 2.9F);
        isImmuneToFire = true;
        getNavigator().setAvoidsWater(true);
        moveSpeed = 0.28F;
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, sit);
        tasks.addTask(3, new EntityAILeapAtTarget(this, moveSpeed + 0.2F));
        tasks.addTask(4, new EntityAIAttackOnCollide(this, moveSpeed + 0.2F, true));
        tasks.addTask(5, new EntityAIFollowOwnerGolem(this, moveSpeed, 10F, 2.0F));
        tasks.addTask(6, new EntityAIWander(this, moveSpeed));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));

        targetTasks.addTask(1, new EntityAIOwnerHurtByTargetGolem(this));
        targetTasks.addTask(2, new EntityAIOwnerHurtTargetGolem(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityMob.class, 16F, 0, false, true));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntitySlime.class, 16F, 0, false, true));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
        dataWatcher.addObject(17, "");
        dataWatcher.addObject(18, Byte.valueOf((byte)0));
        dataWatcher.addObject(19, new Integer(getHealth()));
    }



    @Override
    protected void updateAITick()
    {
        dataWatcher.updateObject(19, Integer.valueOf(getHealth()));
    }

    @Override
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public int getBrightnessForRender(float par1)
    {
        return 0xf000f0;
    }

    @Override
    public float getBrightness(float par1)
    {
        return 1.0F;
    }

    @Override
    public int getMaxHealth()
    {
        return 100;
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if(isRain())
        {
        	moveSpeed = 0.21F;
        }
        if(isInWater())
        {
        	attackEntityFrom(DamageSource.drown, 1);
        }
        if(isBurning())
        {
        	waittime++;
        	if(waittime > 50 && health < getMaxHealth())
        	{
            	health++;
            	worldObj.playSoundAtEntity(this, "fire.ignite", 1.0F, 1.0F);
            	waittime = 0;
        	}
        }
        if(getOwnerName() != "")
        {
            for (int i = 0; i < 2; i++)
            {
                worldObj.spawnParticle("flame", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D);
            }
            if (rand.nextInt(24) == 0)
            {
                worldObj.playSoundEffect(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
            }
        }

        if (field_48120_c > 0)
        {
            field_48120_c--;
        }

        if (field_48118_d > 0)
        {
            field_48118_d--;
        }

        if (motionX * motionX + motionZ * motionZ > 2.5E-007D && rand.nextInt(5) == 0)
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(posY - 0.2D - (double)yOffset);
            int k = MathHelper.floor_double(posZ);
            int l = worldObj.getBlockId(i, j, k);

            if (l > 0)
            {
                worldObj.spawnParticle((new StringBuilder()).append("tilecrack_").append(l).toString(), posX + ((double)rand.nextFloat() - 0.5D) * (double)width, boundingBox.minY + 0.1D, posZ + ((double)rand.nextFloat() - 0.5D) * (double)width, 4D * ((double)rand.nextFloat() - 0.5D), 0.5D, ((double)rand.nextFloat() - 0.5D) * 4D);
            }
        }
    }

    private boolean isRain()
    {
    	return worldObj.canLightningStrikeAt(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
    }

    @Override
    public boolean canAttackClass(Class par1Class)
    {
        return this.getBit1Flag() && EntityPlayer.class.isAssignableFrom(par1Class) ? false : super.canAttackClass(par1Class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("PlayerCreated", getBit1Flag());
        if (getOwnerName() == null)
        {
            par1NBTTagCompound.setString("Owner", "");
        }
        else
        {
            par1NBTTagCompound.setString("Owner", getOwnerName());
        }

        par1NBTTagCompound.setBoolean("Sitting", getSitState());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        getMadebyPlayer(par1NBTTagCompound.getBoolean("PlayerCreated"));
        String s = par1NBTTagCompound.getString("Owner");

        if (s.length() > 0)
        {
            setOwnerName(s);
            func_48138_b(true);
        }

        sit.func_48407_a(true);
    }

    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (!func_48139_F_())
        {
            if (itemstack != null && itemstack.itemID == Item.redstone.itemID)
            {
                itemstack.stackSize--;

                if (itemstack.stackSize <= 0)
                {
                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                }

                if (!worldObj.isRemote)
                {
                    if (rand.nextInt(5) == 0)
                    {
                        func_48138_b(true);
                        setPathToEntity(null);
                        setAttackTarget(null);
                        sit.func_48407_a(true);
                        setOwnerName(par1EntityPlayer.username);
                        particles(true);
                        worldObj.playSoundAtEntity(this, "fire.ignite", 1.0F, 1.0F);
                        worldObj.setEntityState(this, (byte)7);
                    }
                    else
                    {
                    	particles(false);
                    	worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                        worldObj.setEntityState(this, (byte)6);
                    }
                }

                return true;
            }else if(itemstack != null && itemstack.itemID == Mod_RSB_Core.RedstoneBlock.blockID && itemstack.getItemDamage() == 0)
            {
                itemstack.stackSize--;

                if (itemstack.stackSize <= 0)
                {
                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                }

                if (!worldObj.isRemote)
                {
                    func_48138_b(true);
                    setPathToEntity(null);
                    setAttackTarget(null);
                    sit.func_48407_a(true);
                    setOwnerName(par1EntityPlayer.username);
                    particles(true);
                    worldObj.playSoundAtEntity(this, "fire.ignite", 1.0F, 1.0F);
                    worldObj.setEntityState(this, (byte)7);
                }
            }
        }
        else
        {
            if(itemstack != null && itemstack.itemID == Block.bedrock.blockID)
            {
            	setOwnerName("");
            	func_48138_b(false);
            }

            byte amount = 0;
            if (itemstack != null && itemstack.itemID == Item.coal.itemID)
            {
            	amount = 1;
                if (dataWatcher.getWatchableObjectInt(19) < getMaxHealth())
                {
                    itemstack.stackSize--;
                    worldObj.playSoundAtEntity(this, "fire.ignite", 1.0F, 1.0F);
                    heal(amount);
                    particles(true);

                    if (itemstack.stackSize <= 0)
                    {
                        par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                    }

                    return true;
                }
            }

            if (itemstack != null && (itemstack.itemID == Item.redstone.itemID
            		|| itemstack.itemID == Item.blazeRod.itemID) )
            {
            	amount = 2;
                if (dataWatcher.getWatchableObjectInt(19) < getMaxHealth())
                {
                    itemstack.stackSize--;
                    worldObj.playSoundAtEntity(this, "fire.ignite", 1.0F, 1.0F);
                    if(health + amount < getMaxHealth())
                    {
                        heal(amount);
                        particles(true);
                    }else
                    {
                    	health = getMaxHealth();
                    	particles(true);
                    }

                    if (itemstack.stackSize <= 0)
                    {
                        par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                    }

                    return true;
                }
            }

            if (itemstack != null && itemstack.itemID == Mod_RSB_Core.RedstoneBlock.blockID && itemstack.getItemDamage() == 0)
            {
            	amount = 18;
                if (dataWatcher.getWatchableObjectInt(19) < getMaxHealth())
                {
                    itemstack.stackSize--;
                    worldObj.playSoundAtEntity(this, "fire.ignite", 1.0F, 1.0F);
                    if(health + amount < getMaxHealth())
                    {
                        heal(amount);
                        particles(true);
                    }else
                    {
                    	health = getMaxHealth();
                    	particles(true);
                    }

                    if (itemstack.stackSize <= 0)
                    {
                        par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                    }

                    return true;
                }
            }

            if (par1EntityPlayer.username.equalsIgnoreCase(getOwnerName()) && !worldObj.isRemote)
            {
                sit.func_48407_a(!getSitState());
                isJumping = false;
                setPathToEntity(null);
            }
        }

        return super.interact(par1EntityPlayer);
    }

    protected void particles(boolean par1)
    {
        String s = "lava";

        if (!par1)
        {
            s = "largesmoke";
        }

        for (int i = 0; i < 7; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        field_48120_c = 10;
        worldObj.setEntityState(this, (byte)4);
        boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5 + rand.nextInt(15));

        if (flag)
        {
        	if(rand.nextInt(100) == 0)
        	{
            	attackEntityFrom(DamageSource.inFire, 5);
        	}
            par1Entity.motionY += 0.4D;
            par1Entity.setFire(10);
        }

        worldObj.playSoundAtEntity(this, "mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }

    @Override
    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 4)
        {
            field_48120_c = 10;
            worldObj.playSoundAtEntity(this, "mob.irongolem.throw", 1.0F, 1.0F);
        }
        else if (par1 == 11)
        {
            field_48118_d = 400;
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    public int func_48114_ab()
    {
        return field_48120_c;
    }

    public void func_48116_a(boolean par1)
    {
        field_48118_d = par1 ? 400 : 0;
        worldObj.setEntityState(this, (byte)11);
    }

    @Override
    protected String getLivingSound()
    {
        return "none";
    }

    @Override
    protected String getHurtSound()
    {
        return "mob.irongolem.hit";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.irongolem.death";
    }

    @Override
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        worldObj.playSoundAtEntity(this, "mob.irongolem.walk", 1.0F, 1.0F);
    }

    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
        int k = 4;

        for (int l = 0; l < k; l++)
        {
            dropItem(Mod_RSB_Core.RedstoneBlock.blockID, 1);
        }
    }

    public int func_48117_D_()
    {
        return field_48118_d;
    }

    public boolean getBit1Flag()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }


    public void getMadebyPlayer(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 1)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -2)));
        }
    }

    public String getOwnerName()
    {
        return dataWatcher.getWatchableObjectString(17);
    }

    public void setOwnerName(String par1Str)
    {
        dataWatcher.updateObject(17, par1Str);
    }

    public void func_48138_b(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(18);

        if (par1)
        {
            dataWatcher.updateObject(18, Byte.valueOf((byte)(byte0 | 4)));
        }
        else
        {
            dataWatcher.updateObject(18, Byte.valueOf((byte)(byte0 & -5)));
        }
    }

    public EntityLiving getGolemOwner()
    {
        return worldObj.getPlayerEntityByName(getOwnerName());
    }

    public boolean getSitState()
    {
        return (dataWatcher.getWatchableObjectByte(18) & 1) != 0;
    }

    public boolean func_48139_F_()
    {
        return (dataWatcher.getWatchableObjectByte(18) & 4) != 0;
    }

    public void func_48140_f(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(18);

        if (par1)
        {
            dataWatcher.updateObject(18, Byte.valueOf((byte)(byte0 | 1)));
        }
        else
        {
            dataWatcher.updateObject(18, Byte.valueOf((byte)(byte0 & -2)));
        }
    }




}
