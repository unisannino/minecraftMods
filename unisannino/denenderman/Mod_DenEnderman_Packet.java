package unisannino.denenderman;

import java.io.*;
import java.util.List;

import net.minecraft.src.*;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class Mod_DenEnderman_Packet implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
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

			World world = Mod_DenEnderman_Core.proxy.getClientWorld();
			List<Entity> elist = world.loadedEntityList;
			for(int j = 0;j < elist.size(); j++)
			{
				Entity entity = (Entity) elist.get(j);
				if(entity.entityId == entityid && entity instanceof EntityFarmers && !entity.isDead)
				{
					EntityFarmers farmer = (EntityFarmers) entity;
					farmer.handlePacketdata(items);
				}
			}
		}else
		if(packet.channel.equals("SeedBullet"))
		{
			int entityid = dat.readInt();
			int seeddamage = dat.readInt();
			byte seedtype = dat.readByte();;

			World world = Mod_DenEnderman_Core.proxy.getClientWorld();
			List<Entity> elist = world.loadedEntityList;
			for(int j = 0;j < elist.size(); j++)
			{
				Entity entity = (Entity) elist.get(j);
				if(entity.entityId == entityid && entity instanceof EntitySeedBullet)
				{
					EntitySeedBullet bullet = (EntitySeedBullet) entity;
					bullet.handlePacketdata(seeddamage, seedtype);
				}
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

	public static Packet getPacket(EntitySeedBullet seed)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		int entityid = seed.entityId;
		int seeddamage = seed.damage;
		byte seedtype = seed.type;

		try
		{
			dos.writeInt(entityid);
			dos.writeInt(seeddamage);
			dos.writeByte(seedtype);
		}catch(IOException e)
		{
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "SeedBullet";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		packet.isChunkDataPacket = true;
		return packet;
	}
}
