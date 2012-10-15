package unisannino.denenderman;

public class DEHomePosition
{
	public double posX;
	public double posY;
	public double posZ;
	protected boolean canTerepo;

	public DEHomePosition(double x, double y, double z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.canTerepo = false;
	}

}
