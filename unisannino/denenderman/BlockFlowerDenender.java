package unisannino.denenderman;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

//import net.minecraft.src.forge.ITextureProvider;

public class BlockFlowerDenender extends BlockFlower
{
    protected BlockFlowerDenender(int par1)
    {
        super(par1, Material.plants);
        float f = 0.2F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 5.0F, 0.5F + f);
    }

	@Override
	public void registerIcons(IconRegister icoreg)
	{
		this.blockIcon = icoreg.registerIcon("denender:lavender");
	}
}
