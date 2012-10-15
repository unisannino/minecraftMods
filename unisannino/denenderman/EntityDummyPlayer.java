package unisannino.denenderman;

import net.minecraft.src.*;

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
	public boolean canCommandSenderUseCommand(String var1)
	{
		return false;
	}

}
