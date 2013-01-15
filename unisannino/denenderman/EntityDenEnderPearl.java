package unisannino.denenderman;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityDenEnderPearl extends EntityFarmerSpawner
{
    public EntityDenEnderPearl(World world)
    {
        super(world);
    }

    public EntityDenEnderPearl(World world, EntityLiving entityliving)
    {
        super(world, entityliving);
    }

    public EntityDenEnderPearl(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

    @Override
    protected EntityFarmers getFarmer()
    {
    	return new EntityDenEnderman(this.worldObj);
    }
}
