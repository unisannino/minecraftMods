package unisannino.denenderman;

import java.util.List;

import net.minecraft.src.*;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.IGuiHandler;

public class Mod_DenEnderman_CommonProxy implements IGuiHandler
{
	public static EntityFarmers farmer;

	public void registRenderingInfo()
	{
	}


	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityDenEnder)
		{
			TileEntityDenEnder ted = (TileEntityDenEnder)te;
			return new ContainerDenEnder(player.inventory, ted);
		}
		List<Entity> list = world.loadedEntityList;
		for(int i = 0;i < list.size();i++)
		{
			Entity e = list.get(i);
			if(e instanceof EntityFarmers && ID == e.entityId)
			{

				EntityFarmers ef = (EntityFarmers) e;
				return new ContainerChest(player.inventory, ef.inventory);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityDenEnder)
		{
			TileEntityDenEnder ted = (TileEntityDenEnder)te;
			return new GuiDenEnderBlock(player.inventory, ted);
		}
		List<Entity> list = world.loadedEntityList;
		for(int i = 0;i < list.size();i++)
		{
			Entity e = list.get(i);
			if(e instanceof EntityFarmers && ID == e.entityId)
			{
				EntityFarmers ef = (EntityFarmers) e;
				return new GuiChest(player.inventory, ef.inventory);
			}
		}
		return null;
	}


	public World getClientWorld()
	{
		return null;
	}

}
