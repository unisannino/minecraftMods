package unisannino.redstoneblock;

import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Mod_RSB_ClientProxy extends Mod_RSB_CommonProxy
{
	@Override
	public void registRenderingInfo()
	{
		RenderingRegistry.registerEntityRenderingHandler(unisannino.redstoneblock.EntityRedBoat.class, new RenderRedBoat());
		RenderingRegistry.registerEntityRenderingHandler(unisannino.redstoneblock.EntityRSBGolem.class, new RenderRSBGolem());

        RenderingRegistry.registerEntityRenderingHandler(unisannino.redstoneblock.EntityMinecartRF.class, new RenderMinecartRF());
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
}
