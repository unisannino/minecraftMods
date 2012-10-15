package unisannino.denenderman;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class Mod_DenEnderman_Event
{
	@ForgeSubscribe
	public void getFarmers(EntityInteractEvent event)
	{
		if(event.isCancelable())
		{
			if(event.target instanceof EntityFarmers)
			{
				Mod_DenEnderman_CommonProxy.farmer = (EntityFarmers)event.target;
			}
		}
	}

}
