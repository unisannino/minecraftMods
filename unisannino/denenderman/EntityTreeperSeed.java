package unisannino.denenderman;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

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
    protected EntityFarmers getFarmer(World world)
    {
    	return new EntityTreeper(world);
    }
}
