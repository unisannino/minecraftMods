package unisannino.denenderman;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class EntityDummyPlayer extends EntityPlayer
{

	public EntityDummyPlayer(EntityLiving theEntity)
	{
		super(theEntity.worldObj);

		this.rotationPitch = theEntity.rotationPitch;
		this.rotationYaw = theEntity.rotationYaw;
		this.posX = theEntity.posX;
		this.posY = theEntity.posY;
		this.posZ = theEntity.posZ;

	}

	@Override
	public void sendChatToPlayer(String var1)
	{
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override

	/**
	 * Return the coordinates for this player as ChunkCoordinates.
	 */
	public ChunkCoordinates getPlayerCoordinates() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
