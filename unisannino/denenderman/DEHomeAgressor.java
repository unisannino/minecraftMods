package unisannino.denenderman;

import net.minecraft.entity.EntityLiving;

class DEHomeAgressor
{
    public EntityLiving agressor;
    public int agressionTime;

    final DEHome villageObj;

    DEHomeAgressor(DEHome par1Village, EntityLiving par2EntityLiving, int par3)
    {
        this.villageObj = par1Village;
        this.agressor = par2EntityLiving;
        this.agressionTime = par3;
    }
}
