package unisannino.denenderman;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

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
    protected EntityFarmers getFarmer(World world)
    {
    	return new EntityUniuni(world);
    }
}
