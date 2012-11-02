package unisannino.denenderman;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

import net.minecraft.src.*;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.*;

@Mod(modid = "D.Enderman_unisannino", name = "Den-Endermans", version = "ver 2.0.1")
@NetworkMod(channels = {"DenEnderman", "SeedBullet"}, packetHandler = Mod_DenEnderman_Packet.class , clientSideRequired = true, serverSideRequired = false)
public class Mod_DenEnderman_Core
{
	@SidedProxy(clientSide = "unisannino.denenderman.Mod_DenEnderman_ClientProxy", serverSide = "unisannino.denenderman.Mod_DenEnderman_CommonProxy")
	public static Mod_DenEnderman_CommonProxy proxy;

	@Instance("D.Enderman_unisannino")
	static Mod_DenEnderman_Core instance;

	public static int DenenblockID = 161;
	public static int LavenderID = 162;
	public static int StarSandID = 164;

	public static int DenEnderPearlID = 561;
	public static int TreeperSeedID = 563;
	public static int StarPowderID = 564;
	public static int UniuniSoulID = 565;
	public static int FSeedsID = 566;
	public static int AppleBombID = 567;
	public static int SeedGunID = 568;

	public static boolean crying = true;
	public static boolean ToggleXies = false;
	public static boolean onepiece = false;
	//public static boolean canbreakleaves = false;
	public static boolean geneMelons = true;

	public static String canPickupDE ="37, 38";
	public static String canPickupTR ="39, 40";


	public static  Block DenenderBlock;
	public static  Block Lavender;
	public static Block StarSand;

	public static Item DenEnderPearl;
	public static Item TreeperSeed;
	public static Item StarPowder;
	public static Item UniuniSoul;
	public static Item FarmerSeeds;
	public static Item AppleBomb;
	public static Item SeedGun;

	public static String version;

	public static Material starsand;

	public String lavenderTex = "/denender/terrainde.png";
	public String seedgunTex = "/denender/itemsde.png";
	/*
	int sgtex = ModLoader.addOverride("/gui/items.png", "/denender/seedgun.png");
	int Ltex = ModLoader.addOverride("/terrain.png", "/denender/lavender.png");
	*/

	public static boolean Forge;

	/*
	static
	{
		try
		{
			Method forge = Class.forName("net.minecraft.src.forge.MinecraftForgeClient").getMethod("preloadTexture", new Class[]{String.class});
			forge.invoke(null, new Object[] {"/denender/terrainde.png" });
			forge.invoke(null, new Object[] {"/denender/itemsde.png" });
			Forge = true;
			System.out.println("Den-Enderman: Found Minecraft Forge.");

		}catch(ClassNotFoundException e)
		{
			System.out.println("Den-Enderman: Not Found Minecraft Forge.");
			Forge = false;
		}catch(Exception e)
		{
			System.out.println("Den-Enderman: Exception. Something happened by your minecraft!");
		}
	}
	*/
	@PreInit
	public void preload(FMLPreInitializationEvent event)
	{
		Configuration conf = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			conf.load();

			DenenblockID = conf.get("DenEnderBlockID", Configuration.CATEGORY_BLOCK, 161).getInt();
			LavenderID = conf.get("LavenderID", Configuration.CATEGORY_BLOCK, 162).getInt();
			StarSandID = conf.get("StarSandID", Configuration.CATEGORY_BLOCK, 163).getInt();

			DenEnderPearlID = conf.get("DenEnderPearlID", Configuration.CATEGORY_ITEM, 561).getInt();
			TreeperSeedID = conf.get("TreeperSeedID", Configuration.CATEGORY_ITEM, 563).getInt();
			StarPowderID = conf.get("SterPowderID", Configuration.CATEGORY_ITEM, 564).getInt();
			UniuniSoulID = conf.get("UniuniSoulID", Configuration.CATEGORY_ITEM, 565).getInt();
			FSeedsID = conf.get("FarmerSeedID", Configuration.CATEGORY_ITEM, 566).getInt();
			AppleBombID = conf.get("AppleBombID", Configuration.CATEGORY_ITEM, 567).getInt();
			SeedGunID = conf.get("SeedGunID", Configuration.CATEGORY_ITEM, 568).getInt();

			Property cryingP = conf.get("isCrying", Configuration.CATEGORY_GENERAL, true);
			cryingP.comment = "If you choose \"false\", DenEnderman stop crying.";
			crying = cryingP.getBoolean(true);

			Property ToggleXiesP = conf.get("Xies", Configuration.CATEGORY_GENERAL, false);
			ToggleXiesP.comment = "If you cooperate Xies, you choose \" true\" .";
			ToggleXies = ToggleXiesP.getBoolean(false);

			Property onepieceP = conf.get("Weare", Configuration.CATEGORY_GENERAL, false);
			onepieceP.comment = "The coin in my pocket, and you wanna be my friend?";
			onepiece = onepieceP.getBoolean(false);

			Property geneMelonsP = conf.get("generateMelons", Configuration.CATEGORY_GENERAL, true);
			geneMelonsP.comment = "If you choose true, Generate melons on surface of ground";
			geneMelons = geneMelonsP.getBoolean(false);
			/*
			@MLProp(info = "If you choose \"true\", Treeper break leaves around logs.(it is coarse processing)")
			public static boolean canbreakleaves = false;
			*/

			Property canPickupDEP = conf.get("canPickDenEnder", Configuration.CATEGORY_GENERAL, "37, 38");
			canPickupDEP.comment = "DenEnderman can pick up items that you wrote here.";
			canPickupDE  = canPickupDEP.value;
			Property canPickupTRP = conf.get("canPickTreeper", Configuration.CATEGORY_GENERAL, "39, 40");
			canPickupTRP.comment = "Treeper can pick up items that you wrote here.";
			canPickupTR = canPickupTRP.value;

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
		addGui();
		registBlocksForge();
		registItemsForge();
		addRecipes();
		addEntities();
		checkBooleans();

		MinecraftForge.EVENT_BUS.register(new Mod_DenEnderman_Event());
	}

	private void addGui()
	{
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
	}

	private void checkBooleans()
	{
		//System.out.println("Den Enderman: Treepercanbreakleaves is " + this.canbreakleaves);
		System.out.println("Den Enderman: the Coin in the Pocket is " + this.onepiece);
		System.out.println("Den Enderman: GeneMelons is " + this.geneMelons);
		System.out.println(this.canPickupDE);
		System.out.println(this.canPickupTR);
	}

	public void registBlocksForge()
	{
		int deid = DenenblockID;
		int lid = LavenderID;
		int ssid  = StarSandID;
		int Ltex = 0;

		MinecraftForgeClient.preloadTexture("/denender/terrainde.png");

		DenenderBlock = new BlockDenEnder(deid).setHardness(0.6F).setStepSound(Block.soundGrassFootstep).setBlockName("Denender");
		Lavender = new BlockFlowerForge(lid, Ltex).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setBlockName("lavender");
		StarSand = new BlockStarSandlayer(ssid, 18).setHardness(0.1F).setStepSound(Block.soundSandFootstep).setBlockName("layeredsand").setLightOpacity(0);

		GameRegistry.registerBlock(DenenderBlock);
		GameRegistry.registerBlock(Lavender);
		GameRegistry.registerBlock(StarSand);
		LanguageRegistry.addName(DenenderBlock, "DenEnder Block");
		LanguageRegistry.addName(Lavender, "Lavender");
		LanguageRegistry.addName(StarSand, "Uni Sand");
		LanguageRegistry.instance().addNameForObject(DenenderBlock,"ja_JP", "田園ダーブロック");
		LanguageRegistry.instance().addNameForObject(Lavender,"ja_JP", "ラベンダー");
		LanguageRegistry.instance().addNameForObject(StarSand,"ja_JP", "うにうにの粉");
		GameRegistry.registerTileEntity(TileEntityDenEnder.class, "DenEnder");
	}

	private void registItemsForge()
	{
		int depid = DenEnderPearlID;
		int tsid = TreeperSeedID;
		int spid = StarPowderID;
		int uusid = UniuniSoulID;
		int fsid =  FSeedsID;
		int abid =  AppleBombID;
		int sgid = SeedGunID;

		MinecraftForgeClient.preloadTexture("/denender/itemsde.png");

		DenEnderPearl = new ItemDenEnderPearl(depid).setIconCoord(11, 6).setItemName("DenEnderPearl");
		TreeperSeed = new ItemTreeperSeed(tsid).setIconCoord(13, 3).setItemName("Treeper");
		UniuniSoul = new ItemUniuniSoul(uusid).setIconCoord(7, 3).setItemName("UniuniSoul");
		StarPowder = new ItemStarPowder(spid).setIconCoord(14, 0).setItemName("UniPowder");
		FarmerSeeds = new ItemFarmerSeeds(fsid).setIconCoord(13, 3).setItemName("FarmerSeeds");
		AppleBomb = new ItemAppleBomb(abid).setIconCoord(10, 0).setItemName("AppleBomb");
		SeedGun = new ItemSeedGun(sgid).setIconCoord(0, 0).setItemName("SeedGun");

		LanguageRegistry.addName(DenEnderPearl, "DenEnder Pearl");
		LanguageRegistry.addName(TreeperSeed, "Treeper Seed");
		LanguageRegistry.addName(StarPowder, "Uniuni Powderball");
		LanguageRegistry.addName(UniuniSoul, "Uniuni Soul");
		LanguageRegistry.addName(FarmerSeeds, "Lavender Seed");
		LanguageRegistry.addName(AppleBomb, "Apple Bomb");
		LanguageRegistry.addName(SeedGun, "Seed Gun");
		//ModLoader.addName(new ItemStack(Item.itemsList[this.SeedGun.shiftedIndex], 1, 1), "Tane-Ga-Shima");

		LanguageRegistry.instance().addNameForObject(DenEnderPearl,"ja_JP", "田園ダーパール");
		LanguageRegistry.instance().addNameForObject(TreeperSeed,"ja_JP", "ツリーパーの種");
		LanguageRegistry.instance().addNameForObject(StarPowder,"ja_JP", "うにうに粉の玉");
		LanguageRegistry.instance().addNameForObject(UniuniSoul,"ja_JP", "うにうにの魂");
		LanguageRegistry.instance().addNameForObject(FarmerSeeds,"ja_JP", "ラベンダーの種");
		LanguageRegistry.instance().addNameForObject(AppleBomb,"ja_JP", "リンゴ爆弾");
		LanguageRegistry.instance().addNameForObject(SeedGun,"ja_JP", "種鉄砲");
		//ModLoader.addName(new ItemStack(Item.itemsList[this.SeedGun.shiftedIndex], 1, 1),"ja_JP", "種子島");
	}

	private void addEntities()
	{
		proxy.registRenderingInfo();

		EntityRegistry.registerModEntity(EntityDenEnderPearl.class, "DenEnderPearl", 1, this, 64, 10, true);
		EntityRegistry.registerModEntity(EntityTreeperSeed.class, "TreeperSeed", 2, this, 64, 10, true);
		EntityRegistry.registerModEntity(EntityUniuniSoul.class, "UniuniSoul", 3, this, 64, 10, true);
		EntityRegistry.registerModEntity(EntitySeedBullet.class, "SeedBullet", 4, this, 64, 10, true);
		EntityRegistry.registerModEntity(EntityAppleBomb.class, "ApplerBomb", 5, this, 64, 10, true);

		EntityRegistry.registerGlobalEntityID(EntityDenEnderman.class, "DenEnderman", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(EntityUniuni.class, "Uniuni", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(EntityTreeper.class, "Treeper", EntityRegistry.findGlobalUniqueEntityId());

		/*
		EntityRegistry.registerGlobalEntityID(EntityDenEnderPearl.class, "DenEnderPearl", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(EntityTreeperSeed.class, "TreeperSeed", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(EntityUniuniSoul.class, "UniuniSoul", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(EntitySeedBullet.class, "SeedBullet", EntityRegistry.findGlobalUniqueEntityId());
		*/

		LanguageRegistry.instance().addStringLocalization("entity.DenEnderman.name", "en_US", "DenEnderman");
		LanguageRegistry.instance().addStringLocalization("entity.Treeper.name", "en_US", "Treeper");
		LanguageRegistry.instance().addStringLocalization("entity.Uniuni.name", "en_US", "Uniuni");
		LanguageRegistry.instance().addStringLocalization("entity.DenEnderman.name", "ja_JP", "田園ダーマン");
		LanguageRegistry.instance().addStringLocalization("entity.Treeper.name", "ja_JP", "ツリーパー");
		LanguageRegistry.instance().addStringLocalization("entity.Uniuni.name", "ja_JP", "うにうにちゃん");
	}

	private void addRecipes()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(this.FarmerSeeds, 2), new Object[] { this.Lavender});
		GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 10), new Object[] { this.StarPowder});
		GameRegistry.addShapelessRecipe(new ItemStack(this.FarmerSeeds, 1), new Object[]
				{
					this.StarPowder, Item.enderPearl
				});
		GameRegistry.addShapelessRecipe(new ItemStack(this.AppleBomb, 1), new Object[]
				{
					Item.appleRed, Item.gunpowder, this.StarPowder
				});

		GameRegistry.addRecipe(new ItemStack(this.DenenderBlock, 1), new Object[]
				{
					"WWW", "SEH", "PGM",
					'S', Item.seeds, 'W', Item.wheat, 'H',
					Item.hoeGold, 'E', Item.enderPearl,
					'M', Block.melon, 'P', Block.pumpkin, 'G', Block.blockGold,
				});
		GameRegistry.addRecipe(new ItemStack(this.DenEnderPearl, 1), new Object[]
				{
					"VVV", "PEM", "RCR",
					'W', Item.wheat, 'E', Item.enderPearl,
					'C', Item.cake, 'R', Item.reed,
					'V', Block.vine, 'P', Item.pumpkinSeeds,
					'M', Item.melonSeeds
				});
		GameRegistry.addRecipe(new ItemStack(this.TreeperSeed, 1), new Object[]
				{
					"TTT", "PGM", "WRS",
					'T', Block.tallGrass, 'G', Item.gunpowder,
					'P', Item.pumpkinSeeds, 'M', Item.melonSeeds,
					'W', new ItemStack(Block.sapling, 1),
					'R', new ItemStack(Block.sapling, 1, 1),
					'S', new ItemStack(Block.sapling, 1, 2)
				});
		GameRegistry.addRecipe(new ItemStack(this.UniuniSoul, 1), new Object[]
				{
					"SMS", "MDM", "SMS",
					'M', Block.melon, 'S', Item.speckledMelon,
					'D', Item.diamond
				});
		GameRegistry.addRecipe(new ItemStack(this.SeedGun, 1), new Object[]
				{
					" S ", "S S", "LBL",
					'S', Item.silk, 'L', Item.leather,
					'B', Item.stick,
				});
		GameRegistry.addSmelting(this.StarPowder.shiftedIndex, new ItemStack(Block.sand), 0.1F);
		GameRegistry.addSmelting(this.Lavender.blockID, new ItemStack(Item.dyePowder, 2, 5), 0.7F);
	}
}
