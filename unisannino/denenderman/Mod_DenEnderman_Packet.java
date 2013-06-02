package unisannino.denenderman;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class Mod_DenEnderman_Packet implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		World world = Mod_DenEnderman_Core.proxy.getClientWorld();
		List<Entity> elist = world.loadedEntityList;
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);

		if(packet.channel.equals("DenEnderman"))
		{
			int entityid = dat.readInt();
			boolean hasitems = dat.readByte() != 0;
			int[] items = new int[18 * 3];
			if(hasitems)
			{
				for(int i = 0;i < items.length; i++)
				{
					items[i] = dat.readInt();
				}
			}

			for(int j = 0;j < elist.size(); j++)
			{
				Entity entity = (Entity) elist.get(j);
				if(entity.entityId == entityid && entity instanceof EntityFarmers && !entity.isDead)
				{
					EntityFarmers farmer = (EntityFarmers) entity;
					farmer.handlePacketdata(items);
				}
			}
		}
		else
		if(packet.channel.equals("FarmBlock"))
		{
			int x = dat.readInt();
			int y = dat.readInt();
			int z = dat.readInt();
			String invname = dat.readLine();

			boolean hasitems = dat.readByte() != 0;
			int[] items = new int[54 * 3];
			if(hasitems)
			{
				for(int i = 0;i < items.length; i++)
				{
					items[i] = dat.readInt();
				}
			}

			TileEntity tentity = world.getBlockTileEntity(x, y, z);
			if(tentity instanceof TileEntityDenEnder)
			{
				TileEntityDenEnder farmblo = (TileEntityDenEnder) tentity;
				farmblo.handlePacketData(items, invname);
			}


		}
    }

	public static Packet getPacket(EntityFarmers farmers)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		int entityid = farmers.entityId;
		int[] items = farmers.buildIntDataList();
		boolean hasitems = items != null;
		try
		{
			dos.writeInt(entityid);
			dos.writeByte(hasitems ? 1 : 0);
			if(hasitems)
			{
				for(int i = 0;i < items.length;i++)
				{
					dos.writeInt(items[i]);
				}
			}
		}catch(IOException e)
		{
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "DenEnderman";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		packet.isChunkDataPacket = true;
		return packet;
	}

	public static Packet getPacket(TileEntityDenEnder farmerblo)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		int x = farmerblo.xCoord;
		int y = farmerblo.yCoord;
		int z = farmerblo.zCoord;
		String invname = farmerblo.getInvName();
		int[] items = farmerblo.buildIntDataList();
		boolean hasitems = items != null;
		try
		{
			dos.writeByte(hasitems ? 1 : 0);
			if(hasitems)
			{
				for(int i = 0;i < items.length;i++)
				{
					dos.writeInt(items[i]);
				}
			}
		}catch(IOException e)
		{
			e.getStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "FarmBlock";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		packet.isChunkDataPacket = true;
		return packet;
	}
}
