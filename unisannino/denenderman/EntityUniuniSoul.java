package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.*;

public class EntityUniuniSoul extends EntityFarmerSpawner
{
    public EntityUniuniSoul(World world)
    {
        super(world);
    }

    public EntityUniuniSoul(World world, EntityLiving entityliving)
    {
        super(world, entityliving);
    }

    public EntityUniuniSoul(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

    @Override
    protected EntityFarmers getFarmer()
    {
    	return new EntityUniuni(this.worldObj);
    }
}
