package unisannino.denenderman;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "D.Enderman_unisannino", name = "Den-Endermans", version = "ver 2.2.0")
@NetworkMod(channels = {"DenEnderman", "SeedBullet"}, packetHandler = Mod_DenEnderman_Packet.class , clientSideRequired = true, serverSideRequired = false)
public class Mod_DenEnderman_Core
{
	@SidedProxy(clientSide = "unisannino.denenderman.Mod_DenEnderman_ClientProxy", serverSide = "unisannino.denenderman.Mod_DenEnderman_CommonProxy")
	public static Mod_DenEnderman_CommonProxy proxy;

	@Instance("D.Enderman_unisannino")
	static Mod_DenEnderman_Core instance;



	public static int denenblockID = 161;
	public static int lavenderID = 162;
	public static int starSandID = 164;

	public static int denEnderPearlID = 561;
	public static int treeperSeedID = 563;
	public static int starPowderID = 564;
	public static int uniuniSoulID = 565;
	public static int fSeedsID = 566;
	public static int appleBombID = 567;
	public static int seedGunID = 568;
	public static int seedBulletID = 569;

	public static boolean crying = true;
	public static boolean toggleXies = false;
	public static boolean onepiece = false;
	//public static boolean canbreakleaves = false;
	public static boolean geneMelons = true;
	public static boolean canTouchOwnerOnly = false;

	public static String canPickupDE ="37, 38";
	public static String canPickupTR ="39, 40";


	public static  Block denenderBlock;
	public static  Block lavender;
	public static Block starSand;

	public static Item denEnderPearl;
	public static Item treeperSeed;
	public static Item starPowder;
	public static Item uniuniSoul;
	public static Item farmerSeeds;
	public static Item appleBomb;
	public static Item seedGun;
	public static Item seedBullet;

	public static String version;

	@PreInit
	public void preload(FMLPreInitializationEvent event)
	{
		Configuration conf = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			conf.load();


			denenblockID = conf.get("DenEnderBlockID", Configuration.CATEGORY_BLOCK, 161).getInt();
			lavenderID = conf.get("LavenderID", Configuration.CATEGORY_BLOCK, 162).getInt();
			starSandID = conf.get("StarSandID", Configuration.CATEGORY_BLOCK, 163).getInt();

			denEnderPearlID = conf.get("DenEnderPearlID", Configuration.CATEGORY_ITEM, 561).getInt();
			treeperSeedID = conf.get("TreeperSeedID", Configuration.CATEGORY_ITEM, 563).getInt();
			starPowderID = conf.get("SterPowderID", Configuration.CATEGORY_ITEM, 564).getInt();
			uniuniSoulID = conf.get("UniuniSoulID", Configuration.CATEGORY_ITEM, 565).getInt();
			fSeedsID = conf.get("FarmerSeedID", Configuration.CATEGORY_ITEM, 566).getInt();
			appleBombID = conf.get("AppleBombID", Configuration.CATEGORY_ITEM, 567).getInt();
			seedGunID = conf.get("SeedGunID", Configuration.CATEGORY_ITEM, 568).getInt();
			seedBulletID = conf.get("SeedBulletID", Configuration.CATEGORY_ITEM, 569).getInt();

			Property cryingP = conf.get("isCrying", Configuration.CATEGORY_GENERAL, true);
			cryingP.comment = "If you choose \"false\", DenEnderman stop crying.";
			crying = cryingP.getBoolean(true);

			Property ToggleXiesP = conf.get("Xies", Configuration.CATEGORY_GENERAL, false);
			ToggleXiesP.comment = "If you cooperate Xies, you choose \" true\" .";
			toggleXies = ToggleXiesP.getBoolean(false);

			Property onepieceP = conf.get("Weare", Configuration.CATEGORY_GENERAL, false);
			onepieceP.comment = "The coin in my pocket, and you wanna be my friend?";
			onepiece = onepieceP.getBoolean(false);

			Property geneMelonsP = conf.get("generateMelons", Configuration.CATEGORY_GENERAL, true);
			geneMelonsP.comment = "If you choose true, generate melons on surface of ground";
			geneMelons = geneMelonsP.getBoolean(true);

			Property canTouchOwnerOnlyP = conf.get("canTouch", Configuration.CATEGORY_GENERAL, false);
			canTouchOwnerOnlyP.comment = "If you choose true, DenEndermans no longer touch without owner";
			canTouchOwnerOnly = canTouchOwnerOnlyP.getBoolean(false);
			/*
			@MLProp(info = "If you choose \"true\", Treeper break leaves around logs.(it is coarse processing)")
			public static boolean canbreakleaves = false;
			*/

			Property canPickupDEP = conf.get("canPickDenEnder", Configuration.CATEGORY_GENERAL, "37, 38");
			canPickupDEP.comment = "DenEnderman can pick up items that you wrote here.";
			canPickupDE  = canPickupDEP.getString();
			Property canPickupTRP = conf.get("canPickTreeper", Configuration.CATEGORY_GENERAL, "39, 40");
			canPickupTRP.comment = "Treeper can pick up items that you wrote here.";
			canPickupTR = canPickupTRP.getString();

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
		int deid = denenblockID;
		int lid = lavenderID;
		int ssid  = starSandID;

		denenderBlock = new BlockDenEnder(deid).setHardness(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("denenderblock");
		lavender = new BlockFlowerDenender(lid).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("lavender");
		starSand = new BlockStarSandlayer(ssid).setHardness(0.1F).setStepSound(Block.soundSandFootstep).setUnlocalizedName("unisand").setLightOpacity(0);

		GameRegistry.registerBlock(denenderBlock, "Farmers Block");
		GameRegistry.registerBlock(lavender, "Lavender");
		GameRegistry.registerBlock(starSand, "Uni Sand");
		LanguageRegistry.addName(denenderBlock, "Farmers Block");
		LanguageRegistry.addName(lavender, "Lavender");
		LanguageRegistry.addName(starSand, "Uni Sand");
		LanguageRegistry.instance().addNameForObject(denenderBlock,"ja_JP", "ファーマーズブロック");
		LanguageRegistry.instance().addNameForObject(lavender,"ja_JP", "ラベンダー");
		LanguageRegistry.instance().addNameForObject(starSand,"ja_JP", "うにうにの粉");

		GameRegistry.registerTileEntity(TileEntityDenEnder.class, "DenEnder");
		LanguageRegistry.instance().addStringLocalization("container.denender", "Crops");
		LanguageRegistry.instance().addStringLocalization("container.denender", "ja_JP", "作物");

	}

	private void registItemsForge()
	{
		int depid = denEnderPearlID;
		int tsid = treeperSeedID;
		int spid = starPowderID;
		int uusid = uniuniSoulID;
		int fsid =  fSeedsID;
		int abid =  appleBombID;
		int sgid = seedGunID;
		int sbid = seedBulletID;

		//MinecraftForgeClient.preloadTexture("/denender/itemsde.png");

		denEnderPearl = new ItemDenEnderPearl(depid).setUnlocalizedName("DenEnderPearl");
		treeperSeed = new ItemTreeperSeed(tsid).setUnlocalizedName("Treeper");
		uniuniSoul = new ItemUniuniSoul(uusid).setUnlocalizedName("UniuniSoul");
		starPowder = new ItemStarPowder(spid).setUnlocalizedName("UniPowder");
		farmerSeeds = new ItemFarmerSeeds(fsid).setUnlocalizedName("FarmerSeeds");
		appleBomb = new ItemAppleBomb(abid).setUnlocalizedName("AppleBomb");
		seedGun = new ItemSeedGun(sgid).setUnlocalizedName("SeedGun");
		seedBullet = new ItemSeedBullet(sbid).setUnlocalizedName("SeedBullet");


		LanguageRegistry.addName(denEnderPearl, "DenEnder Pearl");
		LanguageRegistry.addName(treeperSeed, "Treeper Seed");
		LanguageRegistry.addName(starPowder, "Uniuni Powderball");
		LanguageRegistry.addName(uniuniSoul, "Uniuni Soul");
		LanguageRegistry.addName(farmerSeeds, "Lavender Seed");
		LanguageRegistry.addName(appleBomb, "Apple Bomb");
		LanguageRegistry.addName(seedGun, "Seed Gun");
		LanguageRegistry.addName(seedBullet, "Seed Bullet");

		LanguageRegistry.instance().addNameForObject(denEnderPearl,"ja_JP", "田園ダーパール");
		LanguageRegistry.instance().addNameForObject(treeperSeed,"ja_JP", "ツリーパーの種");
		LanguageRegistry.instance().addNameForObject(starPowder,"ja_JP", "うにうに粉の玉");
		LanguageRegistry.instance().addNameForObject(uniuniSoul,"ja_JP", "うにうにの魂");
		LanguageRegistry.instance().addNameForObject(farmerSeeds,"ja_JP", "ラベンダーの種");
		LanguageRegistry.instance().addNameForObject(appleBomb,"ja_JP", "リンゴ爆弾");
		LanguageRegistry.instance().addNameForObject(seedGun,"ja_JP", "種鉄砲");
		LanguageRegistry.instance().addNameForObject(seedBullet, "ja_JP", "種鉄砲の弾");

	}

	private void addEntities()
	{
		proxy.registRenderingInfo();

		EntityRegistry.registerModEntity(EntityDenEnderPearl.class, "DenEnderPearl", 1, this, 64, 10, true);
		EntityRegistry.registerModEntity(EntityTreeperSeed.class, "TreeperSeed", 2, this, 64, 10, true);
		EntityRegistry.registerModEntity(EntityUniuniSoul.class, "UniuniSoul", 3, this, 64, 10, true);
		EntityRegistry.registerModEntity(EntitySeedBullet.class, "SeedBullet", 4, this, 64, 3, true);
		EntityRegistry.registerModEntity(EntityAppleBomb.class, "ApplerBomb", 5, this, 64, 3, true);

		EntityRegistry.registerGlobalEntityID(EntityDenEnderman.class, "DenEnderman", EntityRegistry.findGlobalUniqueEntityId(), 0 , 0);
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
		GameRegistry.addShapelessRecipe(new ItemStack(this.farmerSeeds, 2), new Object[] { this.lavender});
		GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 10), new Object[] { this.starPowder});
		GameRegistry.addShapelessRecipe(new ItemStack(this.farmerSeeds, 1), new Object[]
				{
					this.starPowder, Item.enderPearl
				});
		GameRegistry.addShapelessRecipe(new ItemStack(this.appleBomb, 1), new Object[]
				{
					Item.appleRed, Item.gunpowder, this.starPowder
				});
		GameRegistry.addShapelessRecipe(new ItemStack(this.seedBullet, 1, 0), new Object[]
				{
					this.starPowder, Item.seeds
				});
		GameRegistry.addShapelessRecipe(new ItemStack(this.seedBullet, 1, 1), new Object[]
		        {
		            this.starPowder, Item.melonSeeds
		        });
		GameRegistry.addShapelessRecipe(new ItemStack(this.seedBullet, 1, 2), new Object[]
		        {
		            this.starPowder, Item.pumpkinSeeds
		        });
		GameRegistry.addShapelessRecipe(new ItemStack(this.seedBullet, 1, 3), new Object[]
		        {
		            this.starPowder, new ItemStack(Item.dyePowder, 1, 3)
		        });

		GameRegistry.addRecipe(new ItemStack(this.denenderBlock, 1), new Object[]
				{
					"EBE", "BWB", "EBE",
					'B', Block.brick, 'E', Item.emerald,
					'W', Item.wheat
				});
		GameRegistry.addRecipe(new ItemStack(this.denEnderPearl, 1), new Object[]
				{
					"VVV", "PEM", "RCR",
					'W', Item.wheat, 'E', Item.enderPearl,
					'C', Item.cake, 'R', Item.reed,
					'V', Block.vine, 'P', Item.pumpkinSeeds,
					'M', Item.melonSeeds
				});
		GameRegistry.addRecipe(new ItemStack(this.treeperSeed, 1), new Object[]
				{
					"TTT", "PGM", "WRS",
					'T', Block.tallGrass, 'G', Item.gunpowder,
					'P', Item.pumpkinSeeds, 'M', Item.melonSeeds,
					'W', new ItemStack(Block.sapling, 1),
					'R', new ItemStack(Block.sapling, 1, 1),
					'S', new ItemStack(Block.sapling, 1, 2)
				});
		GameRegistry.addRecipe(new ItemStack(this.uniuniSoul, 1), new Object[]
				{
					"SMS", "MDM", "SMS",
					'M', Block.melon, 'S', Item.speckledMelon,
					'D', Item.diamond
				});
		GameRegistry.addRecipe(new ItemStack(this.seedGun, 1), new Object[]
				{
					" S ", "S S", "LBL",
					'S', Item.silk, 'L', Item.leather,
					'B', Item.stick,
				});
		GameRegistry.addSmelting(this.starPowder.itemID, new ItemStack(Block.sand), 0.1F);
		GameRegistry.addSmelting(this.lavender.blockID, new ItemStack(Item.dyePowder, 2, 5), 0.7F);
	}
}
