package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.*;

//import net.minecraft.src.forge.ITextureProvider;

public class BlockFlowerForge extends BlockFlower
{
    protected BlockFlowerForge(int par1, int par2)
    {
        super(par1, par2, Material.plants);
    }

	@Override
	public String getTextureFile()
	{
		return "/denender/terrainde.png";
	}
}
