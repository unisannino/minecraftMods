package unisannino.denenderman;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosionAppleBomb extends Explosion
{

	private World worldObjAb;
	private Random explosionRNGAb = new Random();

	public ExplosionAppleBomb(World par1World, Entity par2Entity, double par3, double par5, double par7, float par9)
	{
		super(par1World, par2Entity, par3, par5, par7, par9);
		this.worldObjAb = par1World;
	}

    public void doExplosionA()
    {
        int k = MathHelper.floor_double(explosionX - explosionSize - 1.0D);
        int i1 = MathHelper.floor_double(explosionX + explosionSize + 1.0D);
        int k1 = MathHelper.floor_double(explosionY - explosionSize - 1.0D);
        int l1 = MathHelper.floor_double(explosionY + explosionSize + 1.0D);
        int i2 = MathHelper.floor_double(explosionZ - explosionSize - 1.0D);
        int j2 = MathHelper.floor_double(explosionZ + explosionSize + 1.0D);

        List list = worldObjAb.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getAABBPool().getAABB(k, k1, i2, i1, l1, j2));
        Vec3 vec3d = this.worldObjAb.getWorldVec3Pool().getVecFromPool(explosionX, explosionY, explosionZ);

        for (int k2 = 0; k2 < list.size(); k2++)
        {
            Entity entity = (Entity)list.get(k2);
            double d4 = entity.getDistance(explosionX, explosionY, explosionZ) / explosionSize;

            if (d4 <= 1.0D)
            {
                double d6 = entity.posX - explosionX;
                double d8 = entity.posY - explosionY;
                double d10 = entity.posZ - explosionZ;
                double d11 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
                d6 /= d11;
                d8 /= d11;
                d10 /= d11;
                double d12 = worldObjAb.getBlockDensity(vec3d, entity.boundingBox);
                double d13 = (1.0D - d4) * d12;
                entity.attackEntityFrom(DamageSource.setExplosionSource(this), (int)(((d13 * d13 + d13) / 2D) * 8D * explosionSize + 1.0D));
                double d14 = d13;
                entity.motionX += d6 * d14 * 2.0F;
                entity.motionY += d8 * d14 * 2.0F;
                entity.motionZ += d10 * d14 * 2.0F;
            }
        }
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB(boolean par1)
    {
        this.worldObjAb.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObjAb.rand.nextFloat() - this.worldObjAb.rand.nextFloat()) * 0.2F) * 0.7F);

        if (this.explosionSize >= 2.0F && this.isSmoking)
        {
            this.worldObjAb.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
        }
        else
        {
            this.worldObjAb.spawnParticle("largeexplode", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
        }

        Iterator iterator;
        ChunkPosition chunkposition;
        int i;
        int j;
        int k;
        int l;

        if (this.isSmoking)
        {
            iterator = this.affectedBlockPositions.iterator();

            while (iterator.hasNext())
            {
                chunkposition = (ChunkPosition)iterator.next();
                i = chunkposition.x;
                j = chunkposition.y;
                k = chunkposition.z;
                l = this.worldObjAb.getBlockId(i, j, k);

                if (par1)
                {
                    double d0 = (double)((float)i + this.worldObjAb.rand.nextFloat());
                    double d1 = (double)((float)j + this.worldObjAb.rand.nextFloat());
                    double d2 = (double)((float)k + this.worldObjAb.rand.nextFloat());
                    double d3 = d0 - this.explosionX;
                    double d4 = d1 - this.explosionY;
                    double d5 = d2 - this.explosionZ;
                    double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / (double)this.explosionSize + 0.1D);
                    d7 *= (double)(this.worldObjAb.rand.nextFloat() * this.worldObjAb.rand.nextFloat() + 0.3F);
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    this.worldObjAb.spawnParticle("explode", (d0 + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5);
                    this.worldObjAb.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
                }

                if (l > 0)
                {
                    Block block = Block.blocksList[l];

                    if (block.canDropFromExplosion(this))
                    {
                        block.dropBlockAsItemWithChance(this.worldObjAb, i, j, k, this.worldObjAb.getBlockMetadata(i, j, k), 1.0F / this.explosionSize, 0);
                    }

                    block.onBlockExploded(this.worldObjAb, i, j, k, this);
                }
            }
        }

        if (this.isFlaming)
        {
            iterator = this.affectedBlockPositions.iterator();

            while (iterator.hasNext())
            {
                chunkposition = (ChunkPosition)iterator.next();
                i = chunkposition.x;
                j = chunkposition.y;
                k = chunkposition.z;
                l = this.worldObjAb.getBlockId(i, j, k);
                int i1 = this.worldObjAb.getBlockId(i, j - 1, k);

                if (l == 0 && Block.opaqueCubeLookup[i1] && this.explosionRNGAb.nextInt(3) == 0)
                {
                    this.worldObjAb.setBlock(i, j, k, Block.fire.blockID);
                }
            }
        }
    }

}
