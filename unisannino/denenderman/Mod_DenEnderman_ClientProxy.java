package unisannino.denenderman;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Mod_DenEnderman_ClientProxy extends Mod_DenEnderman_CommonProxy
{

	@Override
	public void registRenderingInfo()
	{
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityDenEnderman.class, new RenderDenEnderman());
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityUniuni.class, new RenderUniuni());
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityTreeper.class, new RenderTreeper());
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityDenEnderPearl.class, new RenderThrowingItemColor(Mod_DenEnderman_Core.denEnderPearl, 0));
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityTreeperSeed.class, new RenderThrowingItemColor(Mod_DenEnderman_Core.treeperSeed, 0));
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityUniuniSoul.class, new RenderThrowingItemColor(Mod_DenEnderman_Core.uniuniSoul, 0));
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityAppleBomb.class, new RenderThrowingItemColor(Mod_DenEnderman_Core.appleBomb, 0));
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntitySeedBullet.class, new RenderSeedBullet());
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
}
