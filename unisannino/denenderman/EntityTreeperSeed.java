package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.*;

public class EntityTreeperSeed extends EntityFarmerSpawner
{
    public EntityTreeperSeed(World world)
    {
        super(world);
    }

    public EntityTreeperSeed(World world, EntityLiving entityliving)
    {
        super(world, entityliving);
    }

    public EntityTreeperSeed(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

    @Override
    protected EntityFarmers getFarmer()
    {
    	return new EntityTreeper(this.worldObj);
    }
}
