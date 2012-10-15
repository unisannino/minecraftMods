package unisannino.denenderman;

import java.util.Random;

import net.minecraft.src.*;

import cpw.mods.fml.common.IWorldGenerator;

public class Mod_DenEnderman_WorldGene implements IWorldGenerator
{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.provider.dimensionId == 0)
		{
			this.generateSurface(world, random, chunkX << 4, chunkZ << 4);
		}
	}

    public void generateSurface(World var1, Random var2, int var3, int var4)
    {
    	if(Mod_DenEnderman_Core.geneMelons)
    	{
        	WorldChunkManager wcm = var1.getWorldChunkManager();
        	BiomeGenBase bgb = wcm.getBiomeGenAt(var3, var4);
        	if(bgb instanceof BiomeGenBeach)
        	{
                if (var2.nextInt(4) == 0)
                {
                    int x = var3 + var2.nextInt(16) + 8;
                    int y = var2.nextInt(128);
                    int z = var4 + var2.nextInt(16) + 8;
                    (new WorldGenMelon()).generate(var1, var2, x, y, z);
                }
        	}
    	}
    }

}
