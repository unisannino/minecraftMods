package unisannino.denenderman;

import java.util.*;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.Item;
import net.minecraft.src.ItemSeeds;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class EntityAIMoveDEBlock extends EntityAIBase
{

    private EntityFarmers theFarmers;
    private double blockPosX;
    private double blockPosY;
    private double blockPosZ;
    private float moveSpeed;
    private World theWorld;
    private Minecraft mc = FMLClientHandler.instance().getClient();

    public EntityAIMoveDEBlock(EntityFarmers par1EntityFarmers, float par2)
    {
        this.theFarmers = par1EntityFarmers;
        this.moveSpeed = par2;
        this.theWorld = par1EntityFarmers.worldObj;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if(this.theFarmers.inventory.getFirstEmptyStack() == -1)
        {
            Vec3 var1 = this.findPossibleDEBlock();

            if (!(var1 == null))
            {
                this.blockPosX = var1.xCoord;
                this.blockPosY = var1.yCoord;
                this.blockPosZ = var1.zCoord;

        		String s = new StringBuilder(this.theFarmers.getFarmersName()).append(" is searching DenenderBlock...").toString();
        		if(mc.thePlayer != null)
        		{
        			if(!this.theFarmers.worldObj.isRemote && !this.theFarmers.sayLogs)
        			{
            			mc.thePlayer.addChatMessage(s);
            			this.theFarmers.sayLogs = true;
        			}
        		}

                return true;
            }
        }

        return false;

    }

    @Override
    public boolean continueExecuting()
    {
    	return this.theFarmers.getNavigator().noPath();
    }

    @Override
    public void startExecuting()
    {
        this.theFarmers.getNavigator().tryMoveToXYZ(this.blockPosX, this.blockPosY, this.blockPosZ, this.moveSpeed);
    }

    private Vec3 findPossibleDEBlock()
    {
        Random var1 = this.theFarmers.getRNG();

        for (int var2 = 0; var2 < 10; ++var2)
        {
            int var3 = MathHelper.floor_double(this.theFarmers.posX + (double)var1.nextInt(20) - 10.0D);
            int var4 = MathHelper.floor_double(this.theFarmers.boundingBox.minY + (double)var1.nextInt(6) - 3.0D);
            int var5 = MathHelper.floor_double(this.theFarmers.posZ + (double)var1.nextInt(20) - 10.0D);

            if (this.theWorld.getBlockId(var3, var4, var5) == Mod_DenEnderman_Core.denenderBlock.blockID)
            {
                return this.theWorld.getWorldVec3Pool().getVecFromPool((double)var3, (double)var4, (double)var5);
            }
        }
        return null;
    }

}
