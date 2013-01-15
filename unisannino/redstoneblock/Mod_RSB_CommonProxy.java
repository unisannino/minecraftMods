package unisannino.redstoneblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.IGuiHandler;

public class Mod_RSB_CommonProxy implements IGuiHandler
{
	public void registRenderingInfo()
	{
		RenderingRegistry.registerEntityRenderingHandler(unisannino.redstoneblock.EntityRedBoat.class, new RenderRedBoat());
		RenderingRegistry.registerEntityRenderingHandler(unisannino.redstoneblock.EntityRSBGolem.class, new RenderRSBGolem());

        RenderingRegistry.registerEntityRenderingHandler(unisannino.redstoneblock.EntityMinecartRF.class, new RenderMinecartRF());
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity target = world.getBlockTileEntity(x, y, z);
		if(target instanceof TileEntitySFurnace)
		{
			return new ContainerFurnace(player.inventory, (TileEntityFurnace) target);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z)
	{
		TileEntity target = world.getBlockTileEntity(x, y, z);
		if(target instanceof TileEntitySFurnace)
		{
			return new GuiSFurnace(player.inventory, (TileEntitySFurnace) target);
		}

		return null;
	}

	public World getClientWorld()
	{
		return null;
	}

}
