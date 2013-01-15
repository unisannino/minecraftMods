package unisannino.redstoneblock;

import java.util.logging.Level;

import unisannino.denenderman.Mod_DenEnderman_CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.MLProp;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "RSB_unisannino", name ="Red Stone Block", version = "ver 1.10.0")
@NetworkMod()
public class Mod_RSB_Core
{

	@SidedProxy(clientSide = "unisannino.redstoneblock.Mod_RSB_ClientProxy", serverSide = "unisannino.redstoneblock.Mod_RSB_CommonProxy")
	public static Mod_RSB_CommonProxy proxy;

	@Instance("RSB_unisannino")
	public static Mod_RSB_Core instance;

	/*
	 * RedStoneBlock
	 */

	public static int redstoneblockID = 151;
	public static int redstoneblockaID = 152;

	public static int redboatID = 560;

	public static  Block RedstoneBlock;
	public static  Block RedstoneBlockA;
	public static  Block RedstoneStairs;
	public static Item Redboat;

	public static int ModelID = RenderingRegistry.getNextAvailableRenderId();

	/*
	 * RedFurnace
	 */

	public static int RedFurnaceID = 154;

	public static int MinecartRFID = 570;

	public static  Block RedFurnaceIdle;

	public static Item MinecartRF;

	@PreInit
	public void preload(FMLPreInitializationEvent event)
	{
		Configuration conf = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			conf.load();

			this.redstoneblockID = conf.get("RedStoneBlockID", Configuration.CATEGORY_BLOCK, 1151).getInt();
			this.redstoneblockaID = conf.get("RedStoneBlockGlowID", Configuration.CATEGORY_BLOCK, 1152).getInt();
			this.RedFurnaceID = conf.get("RedFurnaceID", Configuration.CATEGORY_BLOCK, 1154).getInt();

			this.redboatID = conf.get("RedStoneBlockBoatlID", Configuration.CATEGORY_ITEM, 560).getInt();
			this.MinecartRFID = conf.get("MinecartRedFurnaceID", Configuration.CATEGORY_ITEM, 570).getInt();
		}
		catch(Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Can't read configurations.");
		}
		finally
		{
			conf.save();
		}
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		registBlocks();
		registItems();
		addRecipes();
		addEntities();
		this.addGui();
		registTex();
	}

	private void registBlocks()
	{
		int rsbid = redstoneblockID;
		int rsbaid = redstoneblockaID;

		RedstoneBlock = new BlockRedstoneBlock(rsbid, false).setHardness(5F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setLightValue(0.625F).setBlockName("RedstoneBlock");
		this.RedstoneBlock.requiresSelfNotify[this.RedstoneBlock.blockID] = true;
		RedstoneBlockA = new BlockRedstoneBlock(redstoneblockaID, true).setHardness(5F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setLightValue(1.0F).setBlockName("RedstoneBlockA");
		this.RedstoneBlockA.requiresSelfNotify[this.RedstoneBlockA.blockID] = true;
		//RedstoneNoCube = new BlockRedNoCube(NoCubeID/*, false*/).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setBlockName("RedstoneStepone");

		GameRegistry.registerBlock(RedstoneBlock, "Block of Redstone");
		GameRegistry.registerBlock(RedstoneBlockA, "Block of Redstone Glowing");

		LanguageRegistry.addName(new ItemStack(Item.itemsList[this.RedstoneBlock.blockID], 1, 0), "Block of Redstone");
		LanguageRegistry.addName(new ItemStack(Item.itemsList[this.RedstoneBlock.blockID], 1, 1), "double slab");
		LanguageRegistry.addName(new ItemStack(Item.itemsList[this.RedstoneBlock.blockID], 1, 2), "Redstone Falling Block");
		LanguageRegistry.addName(new ItemStack(Item.itemsList[this.RedstoneBlock.blockID], 1, 3), "Redstone Lift Block");

		LanguageRegistry.instance().addNameForObject(new ItemStack(Item.itemsList[this.RedstoneBlock.blockID], 1, 0), "ja_JP", "レッドストーンブロック");

		int sfid = RedFurnaceID;
		RedFurnaceIdle = new BlockSFurnace(RedFurnaceID/*, false*/).setHardness(3.5F).setLightValue(0.875F).setBlockName("SFurnaceIdle");
		GameRegistry.registerBlock(RedFurnaceIdle, "Red comet Furnace");
		LanguageRegistry.addName(RedFurnaceIdle, "Red comet Furnace");
		LanguageRegistry.instance().addNameForObject(RedFurnaceIdle,"ja_JP", "赤い彗星のかまど");
	}

	private void registItems()
	{
		int rbid = redboatID;
		Redboat = (new ItemRedBoat(rbid)).setIconCoord(8, 8).setItemName("Redboat");
		LanguageRegistry.addName(Redboat, "Red comet boat");
		LanguageRegistry.instance().addNameForObject(Redboat, "ja_JP", "赤い彗星のボート");

		int mcrfid = MinecartRFID;
		MinecartRF = new ItemMinecartRF(mcrfid).setItemName("minecartRF").setIconCoord(7, 10);
		LanguageRegistry.addName(MinecartRF, "Red comet Minecart");
		LanguageRegistry.instance().addNameForObject(MinecartRF,"ja_JP", "赤い彗星の動力付きトロッコ");
	}

	private void addRecipes()
	{

		GameRegistry.addRecipe(new ItemStack(this.RedstoneBlock, 1, 0), new Object[] { "RR", "RR", 'R', Item.redstone});
		GameRegistry.addRecipe(new ItemStack(Item.redstone, 4), new Object[] { "R", 'R', new ItemStack(this.RedstoneBlock, 1, 0)});


		GameRegistry.addRecipe(new ItemStack(this.Redboat, 1), new Object[] { "R R", "RRR", 'R', new ItemStack(this.RedstoneBlock, 1 , 0)});

		GameRegistry.addRecipe(new ItemStack(this.RedstoneBlock, 1, 2), new Object[] { "R R", " R ", "R R", 'R', Item.redstone});
		GameRegistry.addRecipe(new ItemStack(this.RedstoneBlock, 1, 3), new Object[] { "RRR", "RBR", "RRR", 'R', Item.redstone , 'B', new ItemStack(this.RedstoneBlock, 1 , 0)});

		GameRegistry.addShapelessRecipe(new ItemStack(Item.redstone, 5), new Object[]
				{
					new ItemStack(this.RedstoneBlock, 1 , 2)
				});
		GameRegistry.addShapelessRecipe(new ItemStack(Item.redstone, 17), new Object[]
				{
					new ItemStack(this.RedstoneBlock, 1 , 3)
				});
		GameRegistry.addShapelessRecipe(new ItemStack(this.RedstoneBlock, 5), new Object[]
				{
					this.Redboat
				});


		GameRegistry.addRecipe(new ItemStack(this.RedFurnaceIdle, 1), new Object[]
				{
					"RRR", "RFR", "RRR", 'R', Item.redstone, 'F', Block.stoneOvenIdle
				});
		GameRegistry.addRecipe(new ItemStack(this.MinecartRF, 1), new Object[]
				{
					"R", "M", 'R', this.RedFurnaceIdle, 'M', Item.minecartEmpty
				});
	}

	private void addEntities()
	{
		proxy.registRenderingInfo();

		EntityRegistry.registerModEntity(EntityRedBoat.class, "RedBoat", 1, this, 80, 3, true);

		EntityRegistry.registerGlobalEntityID(EntityRSBGolem.class, "RSBGolem", EntityRegistry.findGlobalUniqueEntityId());

        GameRegistry.registerTileEntity(unisannino.redstoneblock.TileEntitySFurnace.class, "RedFurnace");

		EntityRegistry.registerModEntity(unisannino.redstoneblock.EntityMinecartRF.class, "MinecartRF", 2, this, 80, 3, true);
	}

	private void addGui()
	{
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
	}

	private void registTex()
	{
		RedstoneBlock.blockIndexInTexture = Block.blockSteel.blockIndexInTexture;
		RedstoneBlockA.blockIndexInTexture =  Block.blockSteel.blockIndexInTexture;
	}

	public static int redstonetex;
	public static int redstoneatex;
}
