package unisannino.redstoneblock;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ModLoader;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityRedBoat extends Entity
{

    private boolean field_70279_a;
    private double field_70276_b;
    public int damageTaken;
    public int timeSinceHit;
    public int forwardDirection;
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    public boolean flymode;

    public EntityRedBoat(World world)
    {
        super(world);
        damageTaken = 0;
        timeSinceHit = 0;
        forwardDirection = 1;
        field_70279_a = true;
        this.field_70276_b = 0.07D;
        preventEntitySpawning = true;
        setSize(1.5F, 0.6F);
        yOffset = height / 2.0F;
        isImmuneToFire = true;
        flymode = false;
    }

    public EntityRedBoat(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1 + (double)yOffset, d2);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = d;
        prevPosY = d1;
        prevPosZ = d2;
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Integer(0));
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.boundingBox;
    }

    /**
     * returns the bounding box for this entity
     */
    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed()
    {
        return true;
    }

    @Override
    public double getMountedYOffset()
    {
        return (double)height * 0.0D - 0.3D;
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
    	if (!this.worldObj.isRemote && !this.isDead)
        {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + par2 * 10);
            this.setBeenAttacked();

            if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode)
            {
                this.setDamageTaken(100);
            }

            if (this.getDamageTaken() > 40)
            {
                if (this.riddenByEntity != null)
                {
                    this.riddenByEntity.mountEntity(this);
                }

                dropItemWithOffset(Mod_RSB_Core.Redboat.itemID, 1, 0.0F);
                this.setDead();
            }

            return true;
        }
        else
        {
            return true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        if (this.field_70279_a)
        {
            this.boatPosRotationIncrements = par9 + 5;
        }
        else
        {
            double var10 = par1 - this.posX;
            double var12 = par3 - this.posY;
            double var14 = par5 - this.posZ;
            double var16 = var10 * var10 + var12 * var12 + var14 * var14;

            if (var16 <= 1.0D)
            {
                return;
            }

            this.boatPosRotationIncrements = 3;
        }

        this.boatX = par1;
        this.boatY = par3;
        this.boatZ = par5;
        this.boatYaw = (double)par7;
        this.boatPitch = (double)par8;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @Override
    public void setVelocity(double d, double d1, double d2)
    {
        velocityX = motionX = d;
        velocityY = motionY = d1;
        velocityZ = motionZ = d2;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.getTimeSinceHit() > 0)
        {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0)
        {
            this.setDamageTaken(this.getDamageTaken() - 1);
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        byte var1 = 5;
        double var2 = 0.0D;

        for (int var4 = 0; var4 < var1; var4++)
        {
            double var5 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var4 + 0) / (double)var1 - 0.125D;
            double var7 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var4 + 1) / (double)var1 - 0.125D;
            AxisAlignedBB var9 = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(this.boundingBox.minX, var5, this.boundingBox.minZ, this.boundingBox.maxX, var7, this.boundingBox.maxZ);

            if (worldObj.isAABBInMaterial(var9, Material.water))
            {
                var2 += 1.0D / (double)var1;
            }

            if (worldObj.isAABBInMaterial(var9, Material.lava))
            {
                var2 += 1.0D / (double)var1;

                if (riddenByEntity != null && (riddenByEntity instanceof EntityPlayer))
                {
                	ObfuscationReflectionHelper.setPrivateValue(Entity.class, riddenByEntity, true, "isImmuneToFire");
                    //riddenByEntity.isImmuneToFire = true;
                }
            }

            if (worldObj.isAABBInMaterial(var9, Material.air))
            {
                var2 += 1.0D / (double)var1;
            }
        }

        double var24 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        double var6;
        double var8;
        double var12;
        double var26;

        if (worldObj.isRemote  && this.field_70279_a)
        {
            if (boatPosRotationIncrements > 0)
            {
                var6 = this.posX + (this.boatX - this.posX) / (double)this.boatPosRotationIncrements;
                var8 = this.posY + (this.boatY - this.posY) / (double)this.boatPosRotationIncrements;
                var26 = this.posZ + (this.boatZ - this.posZ) / (double)this.boatPosRotationIncrements;
                var12 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + var12 / (double)this.boatPosRotationIncrements);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.boatPitch - (double)this.rotationPitch) / (double)this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(var6, var8, var26);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                var6 = this.posX + this.motionX;
                var8 = this.posY + this.motionY;
                var26 = this.posZ + this.motionZ;
                this.setPosition(var6, var8, var26);

                if (this.onGround)
                {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }
        }else
        {
            if (var2 < 1.0D)
            {
                var6 = var2 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * var6;
            }
            else
            {
                if (this.motionY < 0.0D)
                {
                    this.motionY /= 2.0D;
                }

                this.motionY += 0.007000000216066837D;
            }

            if (riddenByEntity != null)
            {
                int canfly = worldObj.getBlockId((int)Math.floor(posX), (int)Math.floor(posY - 1), (int)Math.floor(posZ));

                if (flymode && inWater || onGround && flymode || flymode && canfly == Block.waterStill.blockID)
                {
                    //System.out.println("toggle fly off");
                    flymode = false;
                }

                /*
                motionX += riddenByEntity.motionX * 0.2D * 3;
                motionZ += riddenByEntity.motionZ * 0.2D * 3;
                */

                //koko de 3bai
                this.motionX += this.riddenByEntity.motionX * this.field_70276_b * 3.0D;
                this.motionZ += this.riddenByEntity.motionZ * this.field_70276_b * 3.0D;

                if (riddenByEntity instanceof EntityPlayer && onGround && !inWater && canfly == Mod_RSB_Core.redstoneblockaID  && !flymode)
                {
                    //System.out.println("toggle fly on");
                    flymode = true;
                    worldObj.playSoundAtEntity(this, "fire.ignite", 1.0F, 1.0F);
                    //mod_RedstoneBlock.startflying = false;
                }

                if (flymode && !inWater)
                {
                    motionY = 0;

                    if (posY > 2 && Keyboard.isKeyDown(ModLoader.getMinecraftInstance().gameSettings.keyBindSneak.keyCode))
                    {
                        //((EntityPlayer)riddenByEntity).addExhaustion(0.025F * (float)(2));
                        motionY = -0.15D * 2;
                        worldObj.spawnParticle("lava", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - 1.25D, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D);
                    }

                    if (posY < 126 && Keyboard.isKeyDown(ModLoader.getMinecraftInstance().gameSettings.keyBindJump.keyCode))
                    {
                        //((EntityPlayer)riddenByEntity).addExhaustion(0.025F * (float)(2));
                        motionY = 0.15D;
                        worldObj.spawnParticle("lava", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - 1.25D, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
            var6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (var6 > 0.35D)
            {
                var8 = 0.35D / var6;
                this.motionX *= var8;
                this.motionZ *= var8;
                var6 = 0.35D;
            }

            if (var6 > var24 && this.field_70276_b < 0.35D)
            {
                this.field_70276_b += (0.35D - this.field_70276_b) / 35.0D;

                if (this.field_70276_b > 0.35D)
                {
                    this.field_70276_b = 0.35D;
                }
            }
            else
            {
                this.field_70276_b -= (this.field_70276_b - 0.07D) / 35.0D;

                if (this.field_70276_b < 0.07D)
                {
                    this.field_70276_b = 0.07D;
                }
            }

            if (this.onGround)
            {
                this.motionX *= 0.5D;
                this.motionY *= 0.5D;
                this.motionZ *= 0.5D;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            {
                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }

            this.rotationPitch = 0.0F;
            var8 = (double)this.rotationYaw;
            var26 = this.prevPosX - this.posX;
            var12 = this.prevPosZ - this.posZ;

            if (var26 * var26 + var12 * var12 > 0.001D)
            {
                var8 = (double)((float)(Math.atan2(var12, var26) * 180.0D / Math.PI));
            }

            double var14 = MathHelper.wrapAngleTo180_double(var8 - (double)this.rotationYaw);

            if (var14 > 20.0D)
            {
                var14 = 20.0D;
            }

            if (var14 < -20.0D)
            {
                var14 = -20.0D;
            }

            this.rotationYaw = (float)((double)this.rotationYaw + var14);
            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (!this.worldObj.isRemote)
            {
                List var16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

                if (var16 != null && !var16.isEmpty())
                {
                    Iterator var28 = var16.iterator();

                    while (var28.hasNext())
                    {
                        Entity var18 = (Entity)var28.next();

                        if (var18 != this.riddenByEntity && var18.canBePushed() && var18 instanceof EntityBoat)
                        {
                            var18.applyEntityCollision(this);
                        }
                    }
                }

                for (int var27 = 0; var27 < 4; ++var27)
                {
                    int var29 = MathHelper.floor_double(this.posX + ((double)(var27 % 2) - 0.5D) * 0.8D);
                    int var19 = MathHelper.floor_double(this.posZ + ((double)(var27 / 2) - 0.5D) * 0.8D);

                    for (int var20 = 0; var20 < 2; ++var20)
                    {
                        int var21 = MathHelper.floor_double(this.posY) + var20;
                        int var22 = this.worldObj.getBlockId(var29, var21, var19);
                        int var23 = this.worldObj.getBlockMetadata(var29, var21, var19);

                        destroyBlocksInAABB(this.boundingBox.expand(0.2D, 1.0D, 0.2D));

                        /*
                        if (var22 == Block.snow.blockID || var22 == Block.ice.blockID)
                        {
                            this.worldObj.setBlockWithNotify(var29, var21, var19, 0);
                        }
                        else if (var22 == Block.waterlily.blockID)
                        {
                            Block.waterlily.dropBlockAsItemWithChance(this.worldObj, var29, var21, var19, var23, 0.3F, 0);
                            this.worldObj.setBlockWithNotify(var29, var21, var19, 0);
                        }
                        */
                    }
                }

                if (this.riddenByEntity != null && this.riddenByEntity.isDead)
                {
                    this.riddenByEntity = null;
                }
            }
        }
    }


    @Override
    public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
            double var1 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
            double var3 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
            this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var3);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {}

    @SideOnly(Side.CLIENT)
    @Override
    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamageTaken(int par1)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(par1));
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public int getDamageTaken()
    {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int par1)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int par1)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    /**
     * Gets the forward direction of the entity.
     */

    public int getForwardDirection()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    @SideOnly(Side.CLIENT)
    public void func_70270_d(boolean par1)
    {
        this.field_70279_a = par1;
    }


    @Override
    protected void fall(float par1)
    {
    }

    @Override
    public boolean interact(EntityPlayer entityplayer)
    {
        if (riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != entityplayer)
        {
            return true;
        }

        if (!worldObj.isRemote)
        {
            if (riddenByEntity != null)
            {
            	ObfuscationReflectionHelper.setPrivateValue(Entity.class, riddenByEntity, false, "isImmuneToFire");
                //riddenByEntity.isImmuneToFire = false;
            }

            entityplayer.mountEntity(this);
        }

        return true;
    }

    private boolean destroyBlocksInAABB(AxisAlignedBB par1AxisAlignedBB)
    {
        int var2 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int var3 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int var4 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX);
        int var6 = MathHelper.floor_double(par1AxisAlignedBB.maxY);
        int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxZ);
        boolean var8 = false;
        boolean var9 = false;

        for (int var10 = var2; var10 <= var5; ++var10)
        {
            for (int var11 = var3; var11 <= var6; ++var11)
            {
                for (int var12 = var4; var12 <= var7; ++var12)
                {
                    int var13 = this.worldObj.getBlockId(var10, var11, var12);

                    if (var13 != 0)
                    {
                        if (var13 == Block.ice.blockID || var13 == Block.waterlily.blockID || var13 == Block.snow.blockID)
                        {
                        	Block block = Block.blocksList[var13];
                            var9 = true;
                            StepSound stepsound = block.stepSound;
            				if(var13 != Block.ice.blockID)
        					{
                				worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getPitch(), stepsound.getPitch());
        						this.worldObj.setBlockWithNotify(var10, var11, var12, 0);
        					}else
        					{
        						worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
        						this.worldObj.setBlockWithNotify(var10, var11, var12, Block.waterMoving.blockID);
        					}
                        }
                        else
                        {
                            var8 = true;
                        }
                    }
                }
            }
        }

        return var8;
    }
}
