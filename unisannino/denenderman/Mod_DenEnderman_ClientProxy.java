package unisannino.denenderman;

import net.minecraft.src.*;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.IGuiHandler;

public class Mod_DenEnderman_ClientProxy extends Mod_DenEnderman_CommonProxy
{

	@Override
	public void registRenderingInfo()
	{
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityDenEnderman.class, new RenderDenEnderman());
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityUniuni.class, new RenderUniuni());
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityTreeper.class, new RenderTreeper());
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityDenEnderPearl.class, new RenderThrowingItemColor(Mod_DenEnderman_Core.denEnderPearl.getIconFromDamage(0), new ItemStack(Mod_DenEnderman_Core.denEnderPearl)));
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityTreeperSeed.class, new RenderThrowingItemColor(Mod_DenEnderman_Core.treeperSeed.getIconFromDamage(0), new ItemStack(Mod_DenEnderman_Core.treeperSeed)));
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityUniuniSoul.class, new RenderThrowingItemColor(Mod_DenEnderman_Core.uniuniSoul.getIconFromDamage(0), new ItemStack(Mod_DenEnderman_Core.uniuniSoul)));
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntityAppleBomb.class, new RenderThrowingItemColor(Mod_DenEnderman_Core.appleBomb.getIconFromDamage(0), new ItemStack(Mod_DenEnderman_Core.appleBomb)));
		RenderingRegistry.registerEntityRenderingHandler(unisannino.denenderman.EntitySeedBullet.class, new RenderSeedBullet());
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
}
