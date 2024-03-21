package com.mactso.lootchestutility.config;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.lootchestutility.Main;
import com.mactso.lootchestutility.manager.ChestLootManager;
import com.mactso.lootchestutility.manager.LootManager;
import com.mactso.lootchestutility.manager.LootTableListManager;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;



@Mod.EventBusSubscriber(modid = Main.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig {


	private static final Logger LOGGER = LogManager.getLogger();
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	private static List<? extends String> lootItemsList;
	private static List<? extends String> bonusChestLootList;
	private static List<? extends String> bonusLootTableList;
	private static List<? extends String> omitLootBiomesList;
	private static int bonusLootEnchantmentLevelModifier;
	private static int      oddsDropExperienceBottle;
	private static boolean usingCore = false;



	static
	{
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static int getDebugLevel() {
		return debugLevel;
	}

	public static void setDebugLevel(int newValue) {
		if (newValue <0 || newValue > 2) // TODO: this should be redundant 
			newValue = 0;
		 debugLevel = newValue;
	}

	public static boolean isUsingCore() {
		return usingCore;
	}

	public static void setUsingCore(boolean usingCore) {
		MyConfig.usingCore = usingCore;
	}
	
	public static int getLootBoostDistance() {
		return lootBoostDistance;
	}

	public static void setLootBoostDistance(int lootBoostDistance) {
		MyConfig.lootBoostDistance = lootBoostDistance;
	}

	public static boolean isUseLootDrops() {
		return useLootDrops;
	}
	
	public static boolean isGenerateReports() {
		return generateReports;
	}

	public static boolean isGeneratePotions() {
		return generatePotions;
	}
	
	public static boolean isOmittedLootBiome (String resourcelocation) {
		if (omitLootBiomesList.contains(resourcelocation)) {
			return true;
		}
		return false;
	}

	public static List<? extends String> getOmittedLootBiome() {
		return omitLootBiomesList;
	}
	
	public static void setUseLootDrops(boolean newValue) {
		MyConfig.useLootDrops = newValue;
		COMMON.useLootDrops.set(newValue);
	}

	
	public static int getBonusLootEnchantmentLevelModifier() {
		return bonusLootEnchantmentLevelModifier;
	}

	public static void setBonusLootEnchantmentLevelModifier(int bonusLootEnchantmentLevelModifier) {
		MyConfig.bonusLootEnchantmentLevelModifier = bonusLootEnchantmentLevelModifier;
	}

	public static int getOddsDropExperienceBottle() {
		return oddsDropExperienceBottle;
	}
	private static int      debugLevel;
	private static boolean generateReports;
	private static boolean generatePotions;
	private static int      lootBoostDistance;
	private static boolean  useLootDrops;


	@SubscribeEvent
	public static <ModConfig> void onModConfigEvent(final ModConfigEvent configEvent)
	{

		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC)
		{
			bakeConfig();
		}
	}	



	public static void pushValues() {
		COMMON.debugLevel.set(debugLevel);
		COMMON.generateReports.set(generateReports);
		COMMON.generatePotions.set(generatePotions);
		COMMON.lootBoostDistance.set(lootBoostDistance);
		COMMON.useLootDrops.set(useLootDrops);
		COMMON.lootItemsList.set(lootItemsList);
		COMMON.bonusChestLootList.set(bonusChestLootList);
		COMMON.omitLootBiomesList.set(omitLootBiomesList);
		COMMON.bonusLootTableList.set(bonusLootTableList);
		COMMON.bonusLootEnchantmentLevelModifier.set(bonusLootEnchantmentLevelModifier);
		COMMON.oddsDropExperienceBottle.set(oddsDropExperienceBottle);
	}
	

	public static void setOddsDropExperienceBottle(int newOdds) {
		COMMON.oddsDropExperienceBottle.set(newOdds);
		oddsDropExperienceBottle = newOdds;
	}
	
	// remember need to push each of these values separately once we have commands.
	// this copies file changes into the running program variables.
	
	public static void bakeConfig()
	{
		debugLevel = COMMON.debugLevel.get();
		generateReports = COMMON.generateReports.get();
		generatePotions = COMMON.generatePotions.get();

		lootBoostDistance = COMMON.lootBoostDistance.get();
		lootItemsList = COMMON.lootItemsList.get();
		LootManager.init(extract(lootItemsList));
		bonusChestLootList = COMMON.bonusChestLootList.get();
		omitLootBiomesList = COMMON.omitLootBiomesList.get();
		ChestLootManager.init(extract(bonusChestLootList));
		bonusLootTableList = COMMON.bonusLootTableList.get();
		LootTableListManager.init(extract(bonusLootTableList));
		bonusLootEnchantmentLevelModifier = COMMON.bonusLootEnchantmentLevelModifier.get();
		
		useLootDrops = COMMON.useLootDrops.get();
		oddsDropExperienceBottle = COMMON.oddsDropExperienceBottle.get();

		
		if (debugLevel > 0) {
			System.out.println("Harder Farther Debug Level: " + debugLevel );
		}
	}
	


	private static String[] extract(List<? extends String> value)
	{
		return value.toArray(new String[value.size()]);
	}
	
	public static class Common {

		public final IntValue debugLevel;
		
		public final BooleanValue generateReports;
		public final BooleanValue generatePotions;

		public final IntValue oddsDropExperienceBottle;
		
		public final BooleanValue useLootDrops;
		public final IntValue lootBoostDistance;
		public final ConfigValue<List<? extends String>> lootItemsList;
		public final ConfigValue<List<? extends String>> bonusChestLootList; 
		public final ConfigValue<List<? extends String>> bonusLootTableList; 
		public final ConfigValue<List<? extends String>> omitLootBiomesList;
		public final IntValue bonusLootEnchantmentLevelModifier;
		
		

		public Common(ForgeConfigSpec.Builder builder) {

			List<String> defLootItemsList = Arrays.asList(
					"r,23,minecraft:netherite_scrap,1,1","r,1,minecraft:nether_wart,1,2",
					"r,1,minecraft:music_disc_far,1,1", 
					"u,2,minecraft:nether_wart,1,1", "u,3,minecraft:golden_carrot,1,1",
					"u,12,minecraft:diamond,1,1", "u,5,minecraft:emerald,1,3",
					"u,3,minecraft:oak_planks,1,5","u,1,minecraft:book,1,1",
					"u,1,minecraft:gold_ingot,1,1", "u,2,minecraft:chicken,1,2", 
					"u,5,minecraft:glowstone_dust,1,2", "u,1,minecraft:lead,1,1",
					"u,5,minecraft:stone_axe,1,2", "u,3,minecraft:stone_pickaxe,1,1", 
					"u,1,minecraft:iron_axe,1,1", "u,1,minecraft:beetroot_seeds,1,1", 
					"c,3,minecraft:leather_boots,1,1", "c,2,minecraft:gold_nugget,1,3",
					"c,2,minecraft:candle,1,2", "c,5,minecraft:baked_potato,1,2",
					"c,2,minecraft:fishing_rod,1,1", "c,5,minecraft:cooked_cod,1,3",
					"c,3,minecraft:string,1,2",	"c,3,minecraft:iron_nugget,1,3",
					"c,3,minecraft:honey_bottle,1,2",	"c,3,minecraft:stick,1,3",
					"c,1,minecraft:emerald,1,1","c,1,minecraft:paper,1,2");
			

			List<String> defBonusChestLootList = Arrays.asList(
								"01,minecraft:stone_pickaxe,1,1",
								"02,minecraft:stone_axe,1,1",
								"03,minecraft:leather_helmet,1,1",
								"04,minecraft:leather_chestplate,1,1",
								"05,minecraft:leather_leggings,1,1",
								"06,minecraft:leather_boots,1,1",
								"07,minecraft:tipped_arrow,12,18",
								"08,minecraft:emerald,2,5",
								"09,minecraft:iron_pickaxe,1,1",
								"10,minecraft:chainmail_helmet,1,1",
								"11,minecraft:chainmail_chestplate,1,1",
								"12,minecraft:chainmail_leggings,1,1",
								"13,minecraft:chainmail_boots,1,1",
								"14,minecraft:lapis_lazuli,7,11",
								"15,minecraft:honey_bottle,1,2",
								"16,minecraft:glowstone,7,9",
								"17,minecraft:iron_shovel,1,1",
								"18,minecraft:iron_axe,1,1",
								"19,minecraft:cooked_beef,1,5",
								"20,harderfarther:burnishing_stone,1,1",
								"21,minecraft:obsidian,1,3",
								"22,minecraft:emerald,1,6",
								"23,minecraft:diamond,1,1",
								"24,minecraft:iron_helmet,1,1",
								"25,minecraft:iron_chestplate,1,1",
								"26,minecraft:iron_leggings,1,1",
								"27,minecraft:iron_boots,1,1",
								"28,minecraft:iron_axe,1,1",
								"29,minecraft:glowstone_dust,11,23",
								"30,minecraft:moss_block,1,1",
								"31,minecraft:nautilus_shell,1,1",
								"32,minecraft:cooked_mutton,1,1",
								"33,minecraft:amethyst_block,13,18",
								"34,minecraft:budding_amethyst,1,1",
								"35,minecraft:potion,1,1",
								"36,minecraft:glow_squid_spawn_egg,1,1",
								"37,minecraft:golden_apple,1,1",
								"38,minecraft:jack_o_lantern,1,6",
								"39,minecraft:end_rod,1,3",
								"40,harderfarther:burnishing_stone,1,2",
								"41,minecraft:end_stone_bricks,11,20",
								"42,minecraft:amethyst_shard,11,17",
								"43,minecraft:diamond_helmet,1,1",
								"44,minecraft:diamond_chestplate,1,1",
								"45,minecraft:diamond_leggings,1,1",
								"46,minecraft:diamond_boots,1,1",
								"47,minecraft:glow_lichen,1,7",
								"48,minecraft:tnt,2,5",
								"49,minecraft:ice,31,37",
								"50,minecraft:infested_cobblestone,31,64",
								"51,minecraft:red_mushroom_block,31,64",
								"52,minecraft:mushroom_stem,31,64",
								"53,minecraft:brown_mushroom_block,31,64",
								"54,minecraft:chipped_anvil,1,1",
								"55,minecraft:turtle_egg,1,2",
								"56,minecraft:blaze_spawn_egg,1,2",
								"57,minecraft:llama_spawn_egg,1,2",
								"58,minecraft:evoker_spawn_egg,1,1",
								"59,minecraft:zombie_spawn_egg,1,3",
								"60,minecraft:drowned_spawn_egg,3,5",
								"61,minecraft:strider_spawn_egg,1,2",
								"62,minecraft:fox_spawn_egg,1,3",
								"63,minecraft:ocelot_spawn_egg,1,3",
								"64,minecraft:parrot_spawn_egg,1,2",
								"65,minecraft:terracotta,31,64",
								"66,minecraft:coal_block,11,16",
								"67,minecraft:packed_ice,24,48",
								"68,minecraft:green_stained_glass,49,64",
								"69,minecraft:sea_lantern,11,16",
								"70,minecraft:piston,9,16",
								"71,minecraft:bone_block,31,64",
								"72,minecraft:diamond,3,7",
								"73,minecraft:gold_nugget,33,64",
								"74,minecraft:iron_nugget,33,64",
								"75,minecraft:gunpowder,24,48",
								"76,minecraft:powder_snow_bucket,1,1",
								"77,minecraft:green_concrete_powder,56,64",
								"78,minecraft:brain_coral_fan,1,1",
								"79,minecraft:creeper_head,1,1",
								"80,minecraft:zombie_head,1,1",
								"81,minecraft:wither_skeleton_skull,1,1",
								"82,minecraft:skeleton_skull,1,1",
								"83,minecraft:firework_rocket,31,60",
								"84,minecraft:netherite_helmet,1,1",
								"85,minecraft:netherite_boots,1,1",
								"86,minecraft:endermite_spawn_egg,1,5",
								"87,minecraft:elder_guardian_spawn_egg,1,1",
								"88,minecraft:mooshroom_spawn_egg,1,1",
								"89,minecraft:ghast_spawn_egg,1,1",
								"90,minecraft:beacon,1,1",
								"91,minecraft:ender_chest,1,1",
								"92,minecraft:dragon_breath,11,15",
								"93,minecraft:dragon_head,1,1",
								"94,minecraft:diamond_pickaxe,1,1",
								"95,minecraft:diamond_axe,1,1",
								"96,minecraft:diamond_shovel,1,1",
								"97,minecraft:dragon_egg,1,1",
								"98,minecraft:elytra,1,1",
								"99,minecraft:end_portal_frame,1,1"
							 );

			List<String> defBonusLootTableList = Arrays.asList(
					"minecraft:chests/end_city_treasure",
					"minecraft:chests/simple_dungeon",
					"minecraft:chests/village/village_weaponsmith",
					"minecraft:chests/abandoned_mineshaft",
					"minecraft:chests/nether_bridge",
					"minecraft:chests/stronghold_crossing",
					"minecraft:chests/stronghold_corridor",
					"minecraft:chests/desert_pyramid",
					"minecraft:chests/jungle_temple",
					"minecraft:chests/igloo_chest",
					"minecraft:chests/woodland_mansion",
					"minecraft:chests/underwater_ruin_small",
					"minecraft:chests/underwater_ruin_big",
					"minecraft:chests/buried_treasure",
					"minecraft:chests/shipwreck_treasure",
					"minecraft:chests/pillager_outpost",
					"minecraft:chests/bastion_treasure",
					"minecraft:chests/ancient_city",
					"minecraft:chests/ruined_portal"
			);

			List<String> defOmitLootBiomesList = Arrays.asList(
					"minecraft:plains",
					"birch_forest"
			);
			
			builder.push("Harder Farther Control Values");
			builder.push("Debug Settings");			
			debugLevel = builder
					.comment("Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log")
					.translation(Main.MODID + ".config." + "debugLevel")
					.defineInRange("debugLevel", () -> 0, 0, 2);
			builder.pop();

			generateReports = builder
					.comment("Generate LootingTableReport.txt")
					.translation(Main.MODID + ".config." + "generateReports")
					.define ("generateReports", () -> true);

			generatePotions = builder
					.comment("Generate Ogre and Life Saving Potions.txt")
					.translation(Main.MODID + ".config." + "generatePotions")
					.define ("generatePotions", () -> true);
			
			builder.push("Loot Settings");

			lootBoostDistance = builder
					.comment("loot boost distance: Meters before 100% loot boost")
					.translation(Main.MODID + ".config." + "lootBoostDistance")
					.defineInRange("lootBoostDistance", () -> 30000, 1000, 60000000);
			
			useLootDrops = builder
					.comment("Use enhanced harder farther loot?")
					.translation(Main.MODID + ".config." + "useLootDrops")
					.define ("useLootDrops", () -> true);

			
			oddsDropExperienceBottle = builder
					.comment("oddsDropExperienceBottle: Chance to drop 1 experience bottle.")
					.translation(Main.MODID + ".config." + "oddsDropExperienceBottle")
					.defineInRange("oddsDropExperienceBottle", () -> 33, 0, 100);
			
			lootItemsList = builder
					.comment("Loot Items List")
					.translation(Main.MODID + ".config" + "lootItemsList")
					.defineList("lootItemsList", defLootItemsList, Common::isString);
			
			bonusChestLootList = builder
			.comment("Loot Items List")
			.translation(Main.MODID + ".config" + "bonusChestLootList ")
			.defineList("bonusChestLootList ", defBonusChestLootList, Common::isString);

			
			bonusLootTableList = builder
			.comment("List of Loot Tables (usually containers) that will get bonus loot.  (See the LootTables.rpt for a complete list of chest loot tables available to choose) but you can use any loot table.")
			.translation(Main.MODID + ".config" + "bonusLootTableList ")
			.defineList("bonusLootTableList ", defBonusLootTableList, Common::isString);

			omitLootBiomesList = builder
					.comment("List of Biomes which do not get bonus loot.")
					.translation(Main.MODID + ".config" + "omitLootBiomesList")
					.defineList("omitLootBiomesList", defOmitLootBiomesList, Common::isString);
			
			bonusLootEnchantmentLevelModifier = builder
					.comment("Bonus Loot Enchantment Level Modifier")
					.translation(Main.MODID + ".config." + "bonusLootEnchantmentLevelModifier")
					.defineInRange("bonusLootEnchantmentLevelModifier", () -> 1, 0, 19);
			
			builder.pop();

			
		}
		

		public static boolean isString(Object o)
		{
			return (o instanceof String);
		}
	}
	
}

